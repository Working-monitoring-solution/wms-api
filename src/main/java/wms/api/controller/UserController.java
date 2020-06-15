package wms.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wms.api.common.request.AdminLoginRequest;
import wms.api.common.request.ChangePasswordRequest;
import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.service.internal.UserService;
import wms.api.transform.UserTransform;
import wms.api.util.Utils;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
@Validated
public class UserController extends AbstractController<UserService, UserTransform> {

    @RequestMapping(value = "/admin/create-user", method = {RequestMethod.POST})
    public ResponseEntity createUser(@RequestBody CreateUserRequest createUserRequest, HttpServletRequest request) {
        return toResult(transform.toUserResponse(service.createUser(createUserRequest, request)));
    }

    @RequestMapping(value = "/user/login", method = {RequestMethod.GET})
    public ResponseEntity login(@RequestBody UserLoginRequest loginRequest) {
        return toResult(service.login(loginRequest));
    }

    @RequestMapping(value = "/user/device-token", method = {RequestMethod.PUT})
    public ResponseEntity updateDeviceToken(@RequestParam String deviceToken, HttpServletRequest request) {
        return toResult(service.updateDeviceToken(request,deviceToken));
    }

    @RequestMapping(value = "/user/change-password", method = {RequestMethod.PUT})
    public ResponseEntity changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        return toResult(transform.toUserResponse(service.changePassword(changePasswordRequest.getPassword(), changePasswordRequest.getCurrentPassword(), request)));
    }

    @RequestMapping(value = "/user/change-avatar", method = {RequestMethod.PUT})
    public ResponseEntity changeAvatar(@RequestParam(value = "avatar", required = false) MultipartFile file, HttpServletRequest request) {
        return toResult(transform.toUserResponse(service.changeAvatar(file, request)));
    }

    @RequestMapping(value = "/admin/login", method = {RequestMethod.GET})
    public ResponseEntity loginAdmin(@RequestBody AdminLoginRequest loginRequest) {
        return toResult(service.loginAdmin(loginRequest));
    }

    @RequestMapping(value = "/admin/change-active-status", method = {RequestMethod.PUT})
    public ResponseEntity changeActiveStatus(@RequestParam String id, HttpServletRequest request) {
        return toResult(transform.toUserResponse(service.changeActiveStatus(id, request)));
    }

    @RequestMapping(value = "/admin/change-role-admin-status", method = {RequestMethod.PUT})
    public ResponseEntity changeRoleAdminStatus(@RequestParam String id, HttpServletRequest request) {
        return toResult(transform.toUserResponse(service.changeRoleAdminStatus(id, request)));
    }

    @RequestMapping(value = "/admin/change-user-info", method = {RequestMethod.PUT})
    public ResponseEntity changeManager(@RequestParam String userId,
                                        @RequestParam String managerId,
                                        @RequestParam boolean status,
                                        @RequestParam String department,
                                        @RequestParam String position,
                                        HttpServletRequest request) {
        return toResult(transform.toUserResponse(service.changeUserInfo(userId, managerId, status, department, position, request)));
    }

    @RequestMapping(value = "/admin/search-users", method = {RequestMethod.GET})
    public ResponseEntity searchUsers(@RequestParam(value = "page", defaultValue = "0") String page,
                                      @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "email", required = false) String email,
                                      @RequestParam(value = "position", required = false) String position,
                                      @RequestParam(value = "department", required = false) String department,
                                      HttpServletRequest request) {
        return toResult(transform.toPageUserResponse(service.searchUser(name,
                email,
                position,
                department,
                Utils.toInt(page, "page"),
                request)));
    }

    @RequestMapping(value = "/admin/get-user-by-id", method = {RequestMethod.GET})
    public ResponseEntity findUsersById(@RequestParam String id, HttpServletRequest request) {
        return toResult(transform.toUserResponse(service.getUserById(id, request)));
    }

    @RequestMapping(value = "/admin/validate-token", method = {RequestMethod.GET})
    public ResponseEntity adminValidateToken(HttpServletRequest request) {
        return toResult(service.adminValidateToken(request));
    }

    @RequestMapping(value = "/user/get-info", method = {RequestMethod.GET})
    public ResponseEntity getUserInfo(HttpServletRequest request) {
        return toResult(transform.toUserResponse(service.getUserInfo(request)));
    }

    @RequestMapping(value = "/admin/get-all-manager", method = {RequestMethod.GET})
    public ResponseEntity getAllManger(HttpServletRequest request) {
        return toResult(service.getAllManager(request));
    }

    @RequestMapping(value = "/admin/get-department", method = {RequestMethod.GET})
    public ResponseEntity getDepartment(HttpServletRequest request) {
        return toResult(service.getDepartments(request));
    }

    @RequestMapping(value = "/admin/get-position", method = {RequestMethod.GET})
    public ResponseEntity getPosition(@RequestParam String departmentId, HttpServletRequest request) {
        return toResult(service.getPosition(departmentId, request));
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET})
    public ResponseEntity logout(HttpServletRequest request) {
        return toResult(service.logout(request));
    }
}
