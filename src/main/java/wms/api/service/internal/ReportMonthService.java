package wms.api.service.internal;

import wms.api.common.response.UserReportResponse;
import wms.api.service.BaseService;
import wms.api.dao.entity.ReportMonth;

import javax.servlet.http.HttpServletRequest;

public interface ReportMonthService extends BaseService<ReportMonth, Long> {
    UserReportResponse getUserReportById(String userId, String month, String year, HttpServletRequest request);
}
