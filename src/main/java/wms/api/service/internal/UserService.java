package wms.api.service.internal;

import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.dao.entity.User;
import wms.api.service.BaseService;

public interface UserService extends BaseService<User, Long> {

    String login(UserLoginRequest request);

    User createUser(CreateUserRequest request);
}
