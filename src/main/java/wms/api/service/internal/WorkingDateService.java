package wms.api.service.internal;

import org.springframework.data.domain.Page;
import wms.api.common.request.SendLocationRequest;
import wms.api.dao.entity.Request;
import wms.api.dao.entity.WorkingDate;
import wms.api.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface WorkingDateService extends BaseService<WorkingDate, Long> {

    WorkingDate handleLocation(SendLocationRequest sendLocationRequest, HttpServletRequest request);

    void exportExcelDataInMonth(String userId, String month, String year, String token, HttpServletResponse response);

    Request createRequest(String date, String reason, HttpServletRequest request);

    Request approve(String requestId, HttpServletRequest request);

    Request deny(String requestId, HttpServletRequest request);

    List<String> getReason();

    List<Request> userGetPendingRequest(HttpServletRequest request);

    Page<Request> userGetHandledRequest(HttpServletRequest request, String page);

    Page<Request> adminGetPendingRequest(HttpServletRequest request, String page);

    Long adminCountPendingRequest(HttpServletRequest request);

    Page<Request> adminGetHandledRequest(HttpServletRequest request, String page);
}
