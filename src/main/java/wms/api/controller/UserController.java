package wms.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.api.common.request.AdminLoginRequest;
import wms.api.common.request.ChangeInformationRequest;
import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.service.internal.FirebaseService;
import wms.api.service.internal.UserService;
import wms.api.transform.UserTransform;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class UserController extends AbstractController<UserService, UserTransform> {

    @Autowired
    FirebaseService firebaseService;

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

    @PostMapping("/admin/login")
    public ResponseEntity loginAdmin(@RequestBody AdminLoginRequest loginRequest) {
        return toResult(service.loginAdmin(loginRequest));
    }

    @PostMapping("/admin/change-active-status")
    public ResponseEntity changeActiveStatus(@RequestParam String id, HttpServletRequest request) {
        return toResult(service.changeActiveStatus(id, request));
    }

    @PostMapping("/admin/get-users")
    public ResponseEntity getUsers(@RequestParam int page, HttpServletRequest request) {
        return toResult(service.getAllUsers(request, page).getContent());
    }

    @PostMapping("/admin/find-users-by-name")
    public ResponseEntity findUsersByName(@RequestParam int page, @RequestParam String name, HttpServletRequest request) {
        return toResult(service.findUsersByName(request, page, name));
    }

    @PostMapping("/admin/find-users-by-email")
    public ResponseEntity findUsersByEmail(@RequestParam String email, @RequestParam int page, HttpServletRequest request) {
        return toResult(service.findUsersByEmail(request, email, page));
    }

    @PostMapping("/admin/find-users-by-email-and-name")
    public ResponseEntity findUsersByEmailandName(@RequestParam String email, @RequestParam String name, @RequestParam int page, HttpServletRequest request) {
        return toResult(service.findUsersByEmailandName(request, email, name, page));
    }

    @PostMapping("/admin/validate-token")
    public ResponseEntity adminValidateToken(HttpServletRequest request) {
        return toResult(service.adminValidateToken(request));
    }

    @PostMapping("/admin/get-all-users-info")
    public ResponseEntity getAllUsersInfo(HttpServletRequest request) {
        return toResult(service.getAllUsersInfo(request));
    }

    @PostMapping("/user/get-info")
    public ResponseEntity getUserInfo(HttpServletRequest request) {
        return toResult(service.getUserInfo(request));
    }

//  Just for testing
//    @PostMapping("/test")
//    public ResponseEntity test(@RequestBody MultipartFile file) {
//        return toResult(firebaseService.saveImage(file, "1"));
//    }
}
