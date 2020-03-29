package wms.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.api.common.request.AdminLoginRequest;
import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.service.internal.UserService;
import wms.api.transform.UserTransform;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api/user")
public class UserController extends AbstractController<UserService, UserTransform> {

    @RequestMapping(path = "/create-user", method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody CreateUserRequest createUserRequest, HttpServletRequest request) {
        return toResult(service.createUser(createUserRequest, request));
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody UserLoginRequest loginRequest) {
        return toResult(service.login(loginRequest));
    }

    @RequestMapping(path = "/admin/login", method = RequestMethod.POST)
    public ResponseEntity loginAdmin(@RequestBody AdminLoginRequest loginRequest) {
        return toResult(service.loginAdmin(loginRequest));
    }
    
    @RequestMapping(path = "/change-active-status", method = RequestMethod.GET)
    public ResponseEntity changeActiveStatus(@RequestParam String id, HttpServletRequest request) {
        return toResult(service.changeActiveStatus(id, request));
    }
}
