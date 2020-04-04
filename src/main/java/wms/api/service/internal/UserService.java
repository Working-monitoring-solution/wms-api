package wms.api.service.internal;

import org.springframework.data.domain.Page;
import wms.api.common.request.AdminLoginRequest;
import wms.api.common.request.ChangeInformationRequest;
import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.common.response.GetAllUsersInfoResponse;
import wms.api.dao.entity.User;
import wms.api.service.BaseService;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends BaseService<User, Long> {

    String login(UserLoginRequest request);

    String loginAdmin(AdminLoginRequest request);

    User createUser(CreateUserRequest createUserRequest, HttpServletRequest request);

    User changeActiveStatus(String id, HttpServletRequest request);

    User changeUserInformation(ChangeInformationRequest changeInformationRequest, HttpServletRequest request);

    Page<User> getAllUsers(HttpServletRequest request, int page);

    Page<User> findUsersByName(HttpServletRequest request, int page, String name);

    User findByEmail(HttpServletRequest request, String email);

    Boolean adminValidateToken(HttpServletRequest request);

    User getUserInfo(HttpServletRequest request);

    GetAllUsersInfoResponse getAllUsersInfo(HttpServletRequest request);
}
