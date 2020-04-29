package wms.api.service.internal;

import wms.api.common.request.SendLocationRequest;
import wms.api.dao.entity.WorkingDate;
import wms.api.service.BaseService;

import javax.servlet.http.HttpServletRequest;

public interface WorkingDateService extends BaseService<WorkingDate, Long> {

    WorkingDate handleLocation(SendLocationRequest sendLocationRequest, HttpServletRequest request);
}
