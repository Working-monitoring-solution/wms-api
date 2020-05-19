package wms.api.dao.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wms.api.dao.entity.Request;
import wms.api.dao.entity.User;

import java.util.Date;
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> getByUserAndStatus(User user, String status);

    Page<Request> getByUserAndStatusNotLike(User user, String status, Pageable pageable);

    Page<Request> getByManagerAndStatus(User manager, String status, Pageable pageable);

    Page<Request> getByStatus(String status, Pageable pageable);

    Long countAllByManagerAndStatus(User manager, String status);

    Long countAllByStatus(String status);

    Page<Request> getByManagerAndStatusNotLike(User manager, String status, Pageable pageable);

    Page<Request> getByStatusNotLike(String status, Pageable pageable);

    Boolean existsByUserAndDate(User user, Date date);
}
