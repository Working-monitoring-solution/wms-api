package wms.api.dao.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wms.api.dao.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByEmailContains(String email, Pageable pageable);

    Boolean existsByEmail(String email);

    Page<User> findByNameContains(String name, Pageable pageable);

    User findByEmail(String email);

    Page<User> findByEmailContainsAndNameContains(String email, String name, Pageable pageable);

    Long countAllByNameContains(String name);

    Long countAllByEmailContains(String email);

    Long countAllByEmailContainsAndNameContains(String email, String name);
}
