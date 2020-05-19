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

    Long countByUserAndDateBetweenAndCreateAtIsNotNull(User user, Date firstDate, Date lastDate);

    Long countByUserAndDateBetweenAndCreateAtIsNullAndPermissionIsFalse(User user, Date firstDate, Date lastDate);

    Long countByUserAndDateBetweenAndCreateAtIsNullAndPermissionIsTrue(User user, Date firstDate, Date lastDate);

    Long countByUserAndDateBetweenAndCreateAtIsNotNullAndAt0800IsFalse(User user, Date firstDate, Date lastDate);

    Long countByUserAndDateBetweenAndCreateAtIsNotNullAndAt1700IsFalse(User user, Date firstDate, Date lastDate);

    Long countByUserAndDateBetweenAndCreateAtIsNotNullAndComeOutIsTrue(User user, Date firstDate, Date lastDate);

}
