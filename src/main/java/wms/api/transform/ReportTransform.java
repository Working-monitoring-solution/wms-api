package wms.api.transform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.api.common.response.ReportResponse;
import wms.api.dao.entity.ReportMonth;

@Component
public class ReportTransform {
    @Autowired
    UserTransform userTransform;

    public ReportResponse toReportResponse(ReportMonth reportMonth) {
        return ReportResponse.builder()
                .id(reportMonth.getId())
                .user(userTransform.toUserResponse(reportMonth.getUser()))
                .month(reportMonth.getMonth())
                .year(reportMonth.getYear())
                .unauthorizedAbsence(reportMonth.getUnauthorizedAbsence())
                .dayOff(reportMonth.getDayOff())
                .workingDate(reportMonth.getWorkingDate())
                .workLate(reportMonth.getWorkLate())
                .homeSoon(reportMonth.getHomeSoon())
                .offSiteTime(reportMonth.getOffSiteTime())
                .build();
    }
}
