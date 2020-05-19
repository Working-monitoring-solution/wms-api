package wms.api.service.internal.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import wms.api.common.response.GetUserReportResponse;
import wms.api.common.response.ReportYear;
import wms.api.dao.entity.ReportMonth;
import wms.api.dao.entity.User;
import wms.api.dao.repo.ReportMonthRepository;
import wms.api.dao.repo.UserRepository;
import wms.api.exception.WMSException;
import wms.api.service.impl.BaseServiceImpl;
import wms.api.service.internal.ReportMonthService;
import wms.api.util.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ReportMonthServiceImpl extends BaseServiceImpl<ReportMonthRepository, ReportMonth, Long> implements ReportMonthService {

    @Autowired
    UserRepository userRepository;

    @Override
    public GetUserReportResponse getUserReportById(String userId, String month, String year, HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        Long userIdInRequest = tokenService.getIdFromToken(token);
        Long userIdToLong = Utils.toLong(userId);
        User user = userRepository.findById(userIdToLong).orElseThrow(WMSException.NotFoundEntityException::new);
        User requestUser = userRepository.findById(userIdInRequest).orElseThrow(WMSException.NotFoundEntityException::new);
        if (!ObjectUtils.isEmpty(user.getManager()) && !userIdToLong.equals(userIdInRequest) && !user.getManager().equals(requestUser) && !ObjectUtils.isEmpty(requestUser.getManager())) {
            throw new WMSException.AuthorizationErrorException();
        }
        ReportMonth reportMonth = repo.getByUserAndMonthAndYear(user, Utils.toInt(month, "month"), Utils.toInt(year, "year"));
        if (ObjectUtils.isEmpty(reportMonth)) {
            throw new WMSException.NotFoundEntityException("report month");
        }
        reportMonth.setUser(null);
        List<ReportMonth> reportYear = repo.getByUserAndYearOrderByMonthAsc(user, Utils.toInt(year, "year"));
        for (ReportMonth report : reportYear) {
            report.setUser(null);
        }
        GetUserReportResponse getUserReportResponse = GetUserReportResponse.builder()
                .reportMonth(reportMonth)
                .reportYear(reportYear)
                .build();
        return getUserReportResponse;
    }
}
