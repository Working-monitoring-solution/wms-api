package wms.api.schedule;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;
import wms.api.dao.entity.User;
import wms.api.dao.entity.WorkingDate;
import wms.api.dao.repo.WorkingDateRepository;
import wms.api.exception.WMSException;
import wms.api.util.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendLocationNotificationSchedule {

    @Autowired
    WorkingDateRepository workingDateRepository;

    private List<String> deviceTokenList = new ArrayList<>();

    private void pushMessage() {
        try {
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(deviceTokenList)
                    .putData("key", "value")
                    .build();
            FirebaseMessaging.getInstance().sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new WMSException.UnknownException();
        }
    }

    @Scheduled(cron = "0 0 0 * * 2-6")
    private void getListDeviceToken() {
        deviceTokenList.clear();
        List<WorkingDate> workingDateList = workingDateRepository.getByDateAndPermissionIsFalse(Utils.toBeginDate(new Date()));
        for (WorkingDate workingDate : workingDateList) {
            User user = workingDate.getUser();
            if (!ObjectUtils.isEmpty(user) && !ObjectUtils.isEmpty(user.getDeviceToken())) {
                deviceTokenList.add(user.getDeviceToken());
            }
        }
    }

    @Scheduled(cron = "0 0/15 8-12 * * 2-6")
    private void morningScheduleCheck() {
        pushMessage();
    }

    @Scheduled(cron = "0 0/15 13-17 * * 2-6")
    private void afternoonScheduleCheck() {
        pushMessage();
    }

}
