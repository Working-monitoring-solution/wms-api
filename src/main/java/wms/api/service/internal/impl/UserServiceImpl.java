package wms.api.service.internal.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import wms.api.common.request.AdminLoginRequest;
import wms.api.exception.WMSException;
import wms.api.util.Constant;
import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.dao.entity.User;
import wms.api.dao.repo.UserRepository;
import wms.api.service.impl.BaseServiceImpl;
import wms.api.service.internal.UserService;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserRepository, User, Long> implements UserService {


    @Value("${spring.jwt.admin.username}")
    String adminUsername;

    @Value("${spring.jwt.admin.password}")
    String adminPassword;

    @Override
    public String login(UserLoginRequest request) {
        User user = repo.findByEmail(request.getEmail());
        if (ObjectUtils.isEmpty(user)) {
            throw new WMSException.NotFoundEntityException();
        }
        if (!authenticate(user.getPassword(), request.getPassword())) {
            throw new WMSException.AuthenticationErrorException();
        }
        if (!user.isActive()) {
            throw new WMSException.UserNotActiveException();
        }
        String token = generateToken(user);
        repo.save(user);
        return token;
    }

    @Override
    public String loginAdmin(AdminLoginRequest request) {
        adminAuthentication(request);
        return adminGenerateToken();
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest, HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        tokenService.validateAdminToken(token);
        if (repo.existsByEmail(createUserRequest.getEmail())) {
            throw new WMSException.EmailExistException();
        }

        User user = User.builder()
                .email(createUserRequest.getEmail())
                .name(createUserRequest.getName())
                .password(hashPassword(createUserRequest.getPassword()))
                .avatar(createUserRequest.getAvatar())
                .active(true)
                .build();
        generateToken(user);
        repo.save(user);
        return user;
    }

    @Override
    public User changeActiveStatus(String id, HttpServletRequest request) {
        validateLongValue(id);
        String token = getTokenFromHeader(request);
        tokenService.validateAdminToken(token);
        User user = repo.findById(Long.valueOf(id)).get();
        user.setActive(!user.isActive());
        repo.save(user);
        return user;
    }

    private void adminAuthentication(AdminLoginRequest request) {
        if (!request.getUsername().equals(adminUsername) || !request.getPassword().equals(adminPassword)) {
            throw new WMSException.AuthenticationErrorException();
        }
    }

    private String generateToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .signWith(SignatureAlgorithm.HS256, secretkey)
                .compact();
        user.setToken(token);
        return token;
    }

    private String adminGenerateToken() {
        String token = Jwts.builder()
                .setSubject(adminUsername)
                .signWith(SignatureAlgorithm.HS256, secretkey)
                .compact();
        return token;
    }

    private boolean authenticate(String hashed, String password) {
        return BCrypt.checkpw(password, hashed);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(Constant.SALT_ROUND));
    }
}
