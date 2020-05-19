package wms.api.dao.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wms.api.dao.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByIdAndRoleAdminIsTrue(long id);

    List<User> findAllByActiveIsTrue();

    List<User> findAllByActiveIsTrueAndRoleAdminIsTrue();

    @Query(value = "SELECT u from User u "
            + "WHERE (:name IS NULL OR u.name LIKE '%'||:name||'%') "
            + "AND (:email IS NULL OR u.email LIKE '%'||:email||'%') "
            + "AND (:manager IS NULL OR u.manager = :manager) "
            + "ORDER BY u.id ASC",
            countQuery = "SELECT count(u) FROM User u "
                    + "WHERE (:name IS NULL OR u.name LIKE '%'||:name||'%') "
                    + "AND (:email IS NULL OR u.email LIKE '%'||:email||'%')"
                    + "AND (:manager IS NULL OR u.manager = :manager)"
    )
    Page<User> searchUser(@Param("name") String name, @Param("email") String email, @Param("manager") User manager, Pageable pageable);
}
