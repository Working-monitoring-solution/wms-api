package wms.api.service.internal;

import wms.api.common.request.AdminLoginRequest;
import wms.api.common.request.ChangeInformationRequest;
import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.dao.entity.User;
import wms.api.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends BaseService<User, Long> {

    String login(UserLoginRequest request);

    String loginAdmin(AdminLoginRequest request);

    User createUser(CreateUserRequest createUserRequest, HttpServletRequest request);

    User changeActiveStatus(String id, HttpServletRequest request);

    User changeUserInformation(ChangeInformationRequest changeInformationRequest, HttpServletRequest request);

    List<User> getAllUsers(HttpServletRequest request);

}
