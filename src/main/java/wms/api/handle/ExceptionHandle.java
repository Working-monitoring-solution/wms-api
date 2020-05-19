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

    @ExceptionHandler(WMSException.class)
    public ResponseEntity<WMSResponse<String>> handleWMSException(final HttpServletRequest request,
                                                                         final Exception e) {
        log.info(e.getMessage(), e);
        Locale locale = new Locale("en");

        WMSException wmsException = (WMSException) e;
        String code = wmsException.getCode();
        String args = wmsException.getField();

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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<WMSResponse<String>> handleRuntimeException(final HttpServletRequest request,
                                                                      final Exception e) {

        log.info(e.getMessage(), e);
        WMSResponse<String> response = new WMSResponse<String>();
        response.setCode(WMSCode.UNKNOWN_ERROR);
        response.setMessage(WMSCode.UNKNOWN_ERROR_MESSAGE);
        return new ResponseEntity(response, null,
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
}
