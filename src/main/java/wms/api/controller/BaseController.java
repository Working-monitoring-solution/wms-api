package wms.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import wms.api.common.WMSResponse;
import wms.api.util.WMSCode;

import java.util.List;

public abstract class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected <T> ResponseEntity<WMSResponse<T>> response(WMSResponse<T> response) {
        if (response == null) {
            throw new IllegalArgumentException("Please set responseBean.");
        }

        if (StringUtils.isEmpty(response.getCode())) {
            response.setCode(WMSCode.SUCCESS);
        }
        return new ResponseEntity<WMSResponse<T>>(response, HttpStatus.OK);
    }

    protected <T> ResponseEntity<WMSResponse<List<T>>> responseList(WMSResponse<List<T>> response) {
        if (response == null) {
            throw new IllegalArgumentException("Please set responseBean.");
        }

        if (StringUtils.isEmpty(response.getCode())) {
            response.setCode(WMSCode.SUCCESS);
        }
        return new ResponseEntity<WMSResponse<List<T>>>(response, HttpStatus.OK);
    }
}
