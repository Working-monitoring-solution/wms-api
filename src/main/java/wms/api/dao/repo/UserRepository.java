package wms.api.dao.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import wms.api.dao.entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {



}
