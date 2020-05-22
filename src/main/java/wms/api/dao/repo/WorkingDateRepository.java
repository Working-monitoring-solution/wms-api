package wms.api.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import wms.api.dao.entity.User;
import wms.api.dao.entity.WorkingDate;

import java.util.Date;
import java.util.List;

public interface WorkingDateRepository extends JpaRepository<WorkingDate, Long> {

    WorkingDate getWorkingDateByUserAndDateAndPermissionIsFalse(User user, Date date);

    List<WorkingDate> getByUserAndDateBetween(User user, Date firstDate, Date lastDate);

    WorkingDate getByDateAndUser(Date date, User user);

    Long countByUserAndDateBetweenAndCheckInIsNotNull(User user, Date firstDate, Date lastDate);

    Long countByUserAndDateBetweenAndCheckInIsNullAndPermissionIsFalse(User user, Date firstDate, Date lastDate);

    Long countByUserAndDateBetweenAndCheckInIsNullAndPermissionIsTrue(User user, Date firstDate, Date lastDate);

    Long countByUserAndDateBetweenAndCheckInIsNotNullAndAt0800IsFalse(User user, Date firstDate, Date lastDate);

    Long countByUserAndDateBetweenAndCheckInIsNotNullAndAt1700IsFalse(User user, Date firstDate, Date lastDate);

    Long countByUserAndDateBetweenAndCheckInIsNotNullAndComeOutIsTrue(User user, Date firstDate, Date lastDate);

}
