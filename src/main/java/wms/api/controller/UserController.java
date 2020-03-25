package wms.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.service.internal.UserService;
import wms.api.transform.UserTransform;

@RestController
@RequestMapping(path = "/api/user")
public class UserController extends AbstractController<UserService, UserTransform> {

    @RequestMapping(path = "/create-user", method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody CreateUserRequest createUserRequest) {
        return toResult(service.createUser(createUserRequest));
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody UserLoginRequest loginRequest) {
        return toResult(service.login(loginRequest));
    }
}
