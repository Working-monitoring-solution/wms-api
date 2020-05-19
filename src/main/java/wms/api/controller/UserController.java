package wms.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wms.api.common.request.AdminLoginRequest;
import wms.api.common.request.ChangeInformationRequest;
import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.service.internal.FirebaseService;
import wms.api.service.internal.UserService;
import wms.api.transform.Transform;
import wms.api.util.Utils;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
@Validated
public class UserController extends AbstractController<UserService, Transform> {

    @Autowired
    FirebaseService firebaseService;

    @PostMapping("/admin/create-user")
    public ResponseEntity createUser(@RequestBody CreateUserRequest createUserRequest, HttpServletRequest request) {
        return toResult(service.createUser(createUserRequest, request));
    }

    @PostMapping("/user/login")
    public ResponseEntity login(@RequestBody UserLoginRequest loginRequest) {
        return toResult(service.login(loginRequest));
    }

    @PostMapping("/user/change-information")
    public ResponseEntity changeUserInformation(@RequestBody ChangeInformationRequest changeInformationRequest, HttpServletRequest request) {
        return toResult(service.changeUserInformation(changeInformationRequest, request));
    }

    @PostMapping("/admin/login")
    public ResponseEntity loginAdmin(@RequestBody AdminLoginRequest loginRequest) {
        return toResult(service.loginAdmin(loginRequest));
    }

    @PostMapping("/admin/change-active-status")
    public ResponseEntity changeActiveStatus(@RequestParam String id, HttpServletRequest request) {
        return toResult(service.changeActiveStatus(id, request));
    }

    @PostMapping("/admin/change-role-admin-status")
    public ResponseEntity changeRoleAdminStatus(@RequestParam String id, HttpServletRequest request) {
        return toResult(service.changeRoleAdminStatus(id, request));
    }

    @PostMapping("/admin/change-user-info")
    public ResponseEntity changeManager(@RequestParam String userId,
                                        @RequestParam String managerId,
                                        @RequestParam boolean status,
                                        @RequestParam String department,
                                        @RequestParam String position,
                                        HttpServletRequest request) {
        return toResult(service.changeUserInfo(userId, managerId, status, department, position, request));
    }

    @PostMapping("/admin/search-users")
    public ResponseEntity searchUsers(@RequestParam(value = "page", defaultValue = "0") String page,
                                      @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "email", required = false) String email,
                                      @RequestParam(value = "position", required = false) String position,
                                      @RequestParam(value = "department", required = false) String department,
                                      HttpServletRequest request) {
        return toResult(service.searchUser(name, email, position, department, Utils.toInt(page, "page"), request));
    }

    @PostMapping("/admin/get-user-by-id")
    public ResponseEntity findUsersById(@RequestParam String id, HttpServletRequest request) {
        return toResult(service.getUserById(id, request));
    }

    @PostMapping("/admin/validate-token")
    public ResponseEntity adminValidateToken(HttpServletRequest request) {
        return toResult(service.adminValidateToken(request));
    }

    @PostMapping("/user/get-info")
    public ResponseEntity getUserInfo(HttpServletRequest request) {
        return toResult(service.getUserInfo(request));
    }

    @GetMapping("/admin/get-all-manager")
    public ResponseEntity getAllManger(HttpServletRequest request) {
        return toResult(service.getAllManager(request));
    }

    @GetMapping("/admin/get-department")
    public ResponseEntity getDepartment(HttpServletRequest request) {
        return toResult(service.getDepartments(request));
    }

    @GetMapping("/admin/get-position")
    public ResponseEntity getPosition(@RequestParam String departmentId, HttpServletRequest request) {
        return toResult(service.getPosition(departmentId, request));
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        return toResult(service.logout(request));
    }
}
