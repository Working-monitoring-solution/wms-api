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
@RequestMapping(path = "/api")
public class UserController extends AbstractController<UserService, UserTransform> {

    @RequestMapping(path = "/admin/create-user", method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody CreateUserRequest createUserRequest, HttpServletRequest request) {
        return toResult(service.createUser(createUserRequest, request));
    }

    @RequestMapping(path = "/user/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody UserLoginRequest loginRequest) {
        return toResult(service.login(loginRequest));
    }

    @RequestMapping(path = "/user/change-information", method = RequestMethod.POST)
    public ResponseEntity changeUserInformation(@RequestBody ChangeInformationRequest changeInformationRequest, HttpServletRequest request) {
        return toResult(service.changeUserInformation(changeInformationRequest, request));
    }

    @CrossOrigin(origins = "http://localhost:9528")
    @RequestMapping(path = "/admin/login", method = RequestMethod.POST)
    public ResponseEntity loginAdmin(@RequestBody AdminLoginRequest loginRequest) {
        return toResult(service.loginAdmin(loginRequest));
    }
    
    @RequestMapping(path = "/admin/change-active-status", method = RequestMethod.POST)
    public ResponseEntity changeActiveStatus(@RequestParam String id, HttpServletRequest request) {
        return toResult(service.changeActiveStatus(id, request));
    }

    @RequestMapping(path = "/admin/get-all-users", method = RequestMethod.POST)
    public ResponseEntity getAllUsers(HttpServletRequest request) {
        return toResult(service.getAllUsers(request));
    }
}
