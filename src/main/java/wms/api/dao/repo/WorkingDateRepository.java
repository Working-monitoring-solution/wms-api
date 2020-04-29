package wms.api.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import wms.api.dao.entity.User;
import wms.api.dao.entity.WorkingDate;

import java.util.Date;

public interface WorkingDateRepository extends JpaRepository<WorkingDate, Long> {
    WorkingDate getWorkingDateByUserAndDate(User user, Date date);
}
