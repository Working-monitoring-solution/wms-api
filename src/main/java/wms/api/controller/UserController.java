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

    @PostMapping("/admin/change-active-status")
    public ResponseEntity changeActiveStatus(@RequestParam String id, HttpServletRequest request) {
        return toResult(service.changeActiveStatus(id, request));
    }

    @PostMapping("/admin/get-all-users")
    public ResponseEntity getAllUsers(HttpServletRequest request) {
        return toResult(service.getAllUsers(request));
    }
}
