package wms.api.service.internal;

import org.springframework.data.domain.Page;
import wms.api.common.request.AdminLoginRequest;
import wms.api.common.request.ChangeInformationRequest;
import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.common.response.GetManagerResponse;
import wms.api.dao.entity.Department;
import wms.api.dao.entity.Position;
import wms.api.dao.entity.User;
import wms.api.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends BaseService<User, Long> {

    String login(UserLoginRequest request);

    String loginAdmin(AdminLoginRequest request);

    String logout(HttpServletRequest request);

    User createUser(CreateUserRequest createUserRequest, HttpServletRequest request);

    User changeActiveStatus(String id, HttpServletRequest request);

    User changeRoleAdminStatus(String id, HttpServletRequest request);

    User changeUserInfo(String userId, String managerId, boolean status, String department,
                        String position, HttpServletRequest request);

    List<GetManagerResponse> getAllManager(HttpServletRequest request);

    User changeUserInformation(ChangeInformationRequest changeInformationRequest, HttpServletRequest request);

    User getUserById(String id, HttpServletRequest request);

    User adminValidateToken(HttpServletRequest request);

    User getUserInfo(HttpServletRequest request);

    Page<User> searchUser(String name, String email, String position, String department,
                          int page, HttpServletRequest request);

    List<Department> getDepartments(HttpServletRequest request);

    List<Position> getPosition(String departmentId, HttpServletRequest request);
}
