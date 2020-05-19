package wms.api.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wms.api.dao.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
