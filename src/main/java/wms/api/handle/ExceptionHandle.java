package wms.api.handle;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import wms.api.common.JsonMapper;
import wms.api.common.WMSResponse;
import wms.api.exception.WMSException;
import wms.api.util.WMSCode;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@ControllerAdvice
@Slf4j
public class ExceptionHandle extends ResponseEntityExceptionHandler {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSource messageSource;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<WMSResponse<String>> handleNumberFormatException(final HttpServletRequest request,
                                                                           final Exception e) {

        log.info(e.getMessage(), e);
        if (e.getMessage().contains("SocketTimeoutException")) {
            WMSResponse<String> response = new WMSResponse<String>();
            response.setCode(WMSCode.CONNECTION_TIMEOUT);
            response.setMessage(WMSCode.CONNECTION_TIMEOUT_DESCRIPTION);
            return new ResponseEntity<WMSResponse<String>>(response, null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        WMSResponse<String> response = new WMSResponse<String>();
        response.setCode(WMSCode.NUMBER_FORMAT_CODE);
        response.setMessage(e.getMessage());
        logger.info(" response : {} \n", JsonMapper.writeValueAsString(response));
        return new ResponseEntity<WMSResponse<String>>(response, null, HttpStatus.OK);
    }

    @ExceptionHandler(WMSException.class)
    public ResponseEntity<WMSResponse<String>> handleVPBusinessException(final HttpServletRequest request,
                                                                         final Exception e) {
        log.info(e.getMessage(), e);
        String lang = StringUtils.isEmpty(request.getParameter("lang")) ? "vi" : request.getParameter("lang");
        Locale locale = new Locale(lang);

        WMSException vpException = (WMSException) e;
        String code = vpException.getCode();
        String args = translate((String) vpException.getData(), locale);

        // if data is existed, select the MessageSource which contains placeholder(s)
        String message = StringUtils.isEmpty(args) ?
                messageSource.getMessage(
                        "err." + code, null,
                        e.getMessage(), locale
                ) :
                messageSource.getMessage(
                        "err." + code + ".args", new Object[]{args},
                        e.getMessage(), locale
                );

        WMSResponse<String> response = new WMSResponse<>();
        response.setCode(code);
        response.setMessage(message);
        logger.info(" response : {} \n", JsonMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, null, HttpStatus.OK);
    }


    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {

        log.info(ex.getMessage(), ex);

        return new ResponseEntity<Object>(
                "Access denied", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) // (1)
    public ResponseEntity<WMSResponse<String>> handleIOException(final HttpServletRequest request,
                                                                 final Exception e) {

        log.info(e.getMessage(), e);
        WMSResponse<String> response = new WMSResponse<String>();
        response.setCode(WMSCode.CONNECTION_TIMEOUT);
        response.setMessage(WMSCode.CONNECTION_TIMEOUT_DESCRIPTION);
        return new ResponseEntity<WMSResponse<String>>(response, null, HttpStatus.GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<WMSResponse<String>> handleTimeoutException(final HttpServletRequest request,
                                                                      final Exception e) {

        log.info(e.getMessage(), e);
        WMSResponse<String> response = new WMSResponse<String>();
        response.setCode(WMSCode.CONNECTION_TIMEOUT);
        response.setMessage(WMSCode.CONNECTION_TIMEOUT_DESCRIPTION);
        return new ResponseEntity<WMSResponse<String>>(response, null,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<WMSResponse<String>> handleRuntimeException(final HttpServletRequest request,
                                                                      final Exception e) {

        log.info(e.getMessage(), e);
        WMSResponse<String> response = new WMSResponse<String>();
        response.setCode(WMSCode.UNKNOWN_ERROR);
        response.setMessage(WMSCode.UNKNOWN_ERROR_MESSAGE);
        return new ResponseEntity<WMSResponse<String>>(response, null,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WMSResponse<String>> handleException(final HttpServletRequest request,
                                                               final Exception e) {
        log.info(e.getMessage(), e);
        WMSResponse<String> response = new WMSResponse<String>();
        response.setCode(WMSCode.UNKNOWN_ERROR);
        response.setMessage(WMSCode.UNKNOWN_ERROR_MESSAGE);
        logger.info(" response : {} \n", JsonMapper.writeValueAsString(response));
        return new ResponseEntity<WMSResponse<String>>(response, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        // Build Message
        Optional<ConstraintViolation<?>> opt = ex.getConstraintViolations().stream().findFirst();
        if (!opt.isPresent()) return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);

        String field = opt.get().getPropertyPath().toString();
        String message = getMessage(request, field, opt.get().getMessage());

        WMSResponse<String> response = new WMSResponse<>();
        response.setCode(WMSCode.INVALID_INPUT_CODE);
        response.setMessage(message);
        return new ResponseEntity<>(response, null, HttpStatus.OK);
    }

    private String translate(String str, Locale loc) {
        if (StringUtils.isEmpty(str)) return "";

        Pattern p = Pattern.compile("\\{([^}]*)\\}");
        Matcher m = p.matcher(str);
        while (m.find()) {
            String ph = m.group(0);
            String key = m.group(1);
            String value = messageSource.getMessage(key, null, key, loc);
            str = str.replace(ph, value);
        }
        return str;
    }

    private String getMessage(WebRequest request, String fieldName, String messageTemplate) {
        String message = messageTemplate;
        String lang = StringUtils.isEmpty(request.getParameter("lang")) ? "vi" : request.getParameter("lang");
        String[] data = messageTemplate.split(";");
        fieldName = lastField(fieldName);
        fieldName = messageSource.getMessage(fieldName, null, fieldName, new Locale(lang));
        Object[] params = new Object[]{fieldName};
        if (data.length > 1) {
            message = data[0];
            Object[] extraParams = Arrays.stream(data, 1, data.length).toArray();
            params = Stream.of(params, extraParams).flatMap(Stream::of).toArray(Object[]::new);
        }
        return messageSource.getMessage(message, params, message, new Locale(lang));
    }

    private String lastField(String fields) {
        if (StringUtils.isEmpty(fields)) return "";

        String[] list = fields.split("\\.");
        return list[list.length - 1];
    }
}
