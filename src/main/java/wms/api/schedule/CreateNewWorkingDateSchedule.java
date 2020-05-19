package wms.api.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wms.api.dao.entity.WorkingDate;
import wms.api.dao.repo.UserRepository;
import wms.api.dao.repo.WorkingDateRepository;
import wms.api.util.Utils;

import java.util.*;

@Component
public class CreateNewWorkingDateSchedule {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkingDateRepository workingDateRepository;

    // schedule create new records workingDate of all employees for next month
    // start at first day of month
    @Scheduled(cron = "0 0 0 1 * *")
    public void createNewWorkingDateMonthly() {

        // Get all work day of next week
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 0);
        List<Integer> listWeekday = new ArrayList<>();
        for (int date = 1; date <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); date++) {
            calendar.set(Calendar.DAY_OF_MONTH, date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                listWeekday.add(date);
            }
        }

        // Create workingDate records for employees with null data
        List<WorkingDate> workingDateList = new ArrayList<>();
        userRepository.findAllByActiveIsTrue().forEach(user -> {
            listWeekday.forEach(date -> {
                calendar.set(Calendar.DAY_OF_MONTH, date);
                WorkingDate workingDate = WorkingDate.builder()
                        .user(user)
                        .date(Utils.toBeginDate(calendar.getTime()))
                        .build();
                workingDateList.add(workingDate);
            });
        });
        workingDateRepository.saveAll(workingDateList);
    }
}
