package wms.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wms.api.common.WMSResponse;
import wms.api.constant.WMSConstant;
import wms.api.exception.WMSException;
import wms.api.util.WMSCode;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public abstract class AbstractController<S, T> extends BaseController{

    @Autowired
    HttpServletRequest request;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    protected S service;

    @Autowired
    protected T transform;

    protected <V> ResponseEntity<WMSResponse<V>> toResult(Optional<V> optional) {
        if (!optional.isPresent())
            throw new WMSException.NotFoundEntityException();
        return toResult(optional.get());
    }

    protected <V> ResponseEntity<WMSResponse<V>> toResult(V t) {
        WMSResponse<V> wmsResponse = new WMSResponse<V>();
        wmsResponse.setCode(WMSCode.SUCCESS);
        String lang = StringUtils.isEmpty(request.getParameter("lang")) ? "vi" : request.getParameter("lang");
        wmsResponse.setMessage(messageSource.getMessage("err.00", null, null, new Locale(lang)));
        wmsResponse.setData(t);
        return response(wmsResponse);
    }

    protected <V> ResponseEntity<WMSResponse<List<V>>> toResult(List<V> v) {
        WMSResponse<List<V>> wmsResponse = new WMSResponse<List<V>>();
        wmsResponse.setCode(WMSCode.SUCCESS);
        String lang = StringUtils.isEmpty(request.getParameter("lang")) ? "vi" : request.getParameter("lang");
        wmsResponse.setMessage(messageSource.getMessage("err.00", null, null, new Locale(lang)));
        wmsResponse.setData(v);
        return response(wmsResponse);
    }


    protected Pageable getPageable(int page, int size) {
        if (ObjectUtils.isEmpty(page) || page < 1) {
            page = WMSConstant.CURRENT_PAGE_DEFAULT;
        }
        if (ObjectUtils.isEmpty(size) || size < 1 || size > WMSConstant.PAGE_SIZE_MAX) {
            size= WMSConstant.PAGE_SIZE_DEFAULT;
        }
        return PageRequest.of(page - 1, size);
    }
}
