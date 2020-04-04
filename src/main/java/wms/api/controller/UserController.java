package wms.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.api.common.request.AdminLoginRequest;
import wms.api.common.request.ChangeInformationRequest;
import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.service.internal.UserService;
import wms.api.transform.UserTransform;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UserController extends AbstractController<UserService, UserTransform> {

    @CrossOrigin(origins = "http://localhost:9528")
    @PostMapping("/admin/create-user")
    public ResponseEntity createUser(@RequestBody CreateUserRequest createUserRequest, HttpServletRequest request) {
        return toResult(service.createUser(createUserRequest, request));
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest loginRequest) {
        return toResult(service.login(loginRequest));
    }

    @PostMapping("/user/change-information")
    public ResponseEntity changeUserInformation(@RequestBody ChangeInformationRequest changeInformationRequest, HttpServletRequest request) {
        return toResult(service.changeUserInformation(changeInformationRequest, request));
    }

    @CrossOrigin(origins = "http://localhost:9528")
    @PostMapping("/admin/login")
    public ResponseEntity loginAdmin(@RequestBody AdminLoginRequest loginRequest) {
        return toResult(service.loginAdmin(loginRequest));
    }

    @CrossOrigin(origins = "http://localhost:9528")
    @PostMapping("/admin/change-active-status")
    public ResponseEntity changeActiveStatus(@RequestParam String id, HttpServletRequest request) {
        return toResult(service.changeActiveStatus(id, request));
    }

    @CrossOrigin(origins = "http://localhost:9528")
    @PostMapping("/admin/get-users")
    public ResponseEntity getUsers(@RequestParam int page, HttpServletRequest request) {
        return toResult(service.getAllUsers(request, page).getContent());
    }

    @CrossOrigin(origins = "http://localhost:9528")
    @PostMapping("/admin/find-users-by-name")
    public ResponseEntity findUsersByName(@RequestParam int page, @RequestParam String name, HttpServletRequest request) {
        return toResult(service.findUsersByName(request, page, name).getContent());
    }

    @CrossOrigin(origins = "http://localhost:9528")
    @PostMapping("/admin/find-users-by-email")
    public ResponseEntity findUsersByEmail(@RequestParam String email, HttpServletRequest request) {
        return toResult(service.findByEmail(request, email));
    }

    @CrossOrigin(origins = "http://localhost:9528")
    @PostMapping("/admin/validate-token")
    public ResponseEntity adminValidateToken(HttpServletRequest request) {
        return toResult(service.adminValidateToken(request));
    }

    @CrossOrigin(origins = "http://localhost:9528")
    @PostMapping("/admin/get-all-users-info")
    public ResponseEntity getAllUsersInfo(HttpServletRequest request) {
        return toResult(service.getAllUsersInfo(request));
    }

    @PostMapping("/user/get-info")
    public ResponseEntity getUserInfo(HttpServletRequest request) {
        return toResult(service.getUserInfo(request));
    }
}
