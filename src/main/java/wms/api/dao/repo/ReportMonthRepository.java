package wms.api.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wms.api.dao.entity.ReportMonth;
import wms.api.dao.entity.User;

import java.util.List;

@Repository
public interface ReportMonthRepository extends JpaRepository<ReportMonth, Long> {

    ReportMonth getByUserAndMonthAndYear(User user, int month, int year);

    List<ReportMonth> getByUserAndYearOrderByMonthAsc(User user, int year);

}
