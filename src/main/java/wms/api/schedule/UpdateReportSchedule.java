package wms.api.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wms.api.dao.entity.ReportMonth;
import wms.api.dao.entity.User;
import wms.api.dao.repo.ReportMonthRepository;
import wms.api.dao.repo.UserRepository;
import wms.api.dao.repo.WorkingDateRepository;
import wms.api.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class UpdateReportSchedule {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkingDateRepository workingDateRepository;

    @Autowired
    ReportMonthRepository reportMonthRepository;

    // update report at 16pm in the last day of month
    @Scheduled(cron = "0 0 18 28-31 * *")
    public void updateReport() {
        final Calendar calendar = Calendar.getInstance();

        // check if to day is the last day of month
        if (calendar.get(Calendar.DATE) == calendar.getActualMaximum(Calendar.DATE)) {
            Date lastDayOfMonth = Utils.toEndDate(calendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            Date firstDayOfMonth = Utils.toBeginDate(calendar.getTime());
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            List<User> users = userRepository.findAllByActiveIsTrue();
            List<ReportMonth> reportMonths = new ArrayList<>();
            users.forEach(user -> {
                int unauthorizedAbsence = workingDateRepository.countByUserAndDateBetweenAndCheckInIsNullAndPermissionIsFalse(user, firstDayOfMonth, lastDayOfMonth).intValue();
                int dayOff = workingDateRepository.countByUserAndDateBetweenAndCheckInIsNullAndPermissionIsTrue(user, firstDayOfMonth, lastDayOfMonth).intValue();
                int workDate = workingDateRepository.countByUserAndDateBetweenAndCheckInIsNotNull(user, firstDayOfMonth, lastDayOfMonth).intValue();
                int workLate = workingDateRepository.countByUserAndDateBetweenAndCheckInIsNotNullAndAt0800IsFalse(user, firstDayOfMonth, lastDayOfMonth).intValue();
                int homeSoon = workingDateRepository.countByUserAndDateBetweenAndCheckInIsNotNullAndAt1700IsFalse(user, firstDayOfMonth, lastDayOfMonth).intValue();
                int offSiteTime = workingDateRepository.countByUserAndDateBetweenAndCheckInIsNotNullAndComeOutIsTrue(user, firstDayOfMonth, lastDayOfMonth).intValue();
                ReportMonth reportMonth = ReportMonth.builder()
                        .user(user)
                        .month(month)
                        .year(year)
                        .dayOff(dayOff)
                        .unauthorizedAbsence(unauthorizedAbsence)
                        .workingDate(workDate)
                        .workLate(workLate)
                        .homeSoon(homeSoon)
                        .offSiteTime(offSiteTime)
                        .build();
                reportMonths.add(reportMonth);
            });
            reportMonthRepository.saveAll(reportMonths);
        }
    }
}
