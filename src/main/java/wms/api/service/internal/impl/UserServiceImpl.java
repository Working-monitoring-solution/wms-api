package wms.api.service.internal.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import wms.api.common.request.AdminLoginRequest;
import wms.api.common.request.ChangeInformationRequest;
import wms.api.common.response.GetAllUsersInfoResponse;
import wms.api.constant.WMSConstant;
import wms.api.exception.WMSException;
import wms.api.util.Constant;
import wms.api.common.request.CreateUserRequest;
import wms.api.common.request.UserLoginRequest;
import wms.api.dao.entity.User;
import wms.api.dao.repo.UserRepository;
import wms.api.service.impl.BaseServiceImpl;
import wms.api.service.internal.UserService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        User user = User.builder()
                .email(createUserRequest.getEmail())
                .name(createUserRequest.getName())
                .password(hashPassword(createUserRequest.getPassword()))
                .createdDate(format.format(new Date()))
                .active(true)
                .build();
        user = repo.save(user);
        generateToken(user);
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

    @Override
    public Page<User> getAllUsers(HttpServletRequest request, int page) {
        String token = getTokenFromHeader(request);
        tokenService.validateAdminToken(token);
        Pageable pageable = PageRequest.of(page, WMSConstant.PAGE_SIZE_DEFAULT, Sort.by("id").ascending());
        return repo.findAll(pageable);
    }

    @Override
    public User changeUserInformation(ChangeInformationRequest changeInformationRequest, HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        User user = tokenService.validateUserToken(token);
        user.setAvatar(changeInformationRequest.getAvatar());
        user.setPassword(hashPassword(changeInformationRequest.getPassword()));
        user = repo.save(user);
        return user;
    }

    @Override
    public Page<User> findUsersByName(HttpServletRequest request, int page, String name) {
        String token = getTokenFromHeader(request);
        tokenService.validateAdminToken(token);
        Pageable pageable = PageRequest.of(page, WMSConstant.PAGE_SIZE_DEFAULT, Sort.by("id").ascending());
        return repo.findByNameContains(name.trim(), pageable);
    }

    @Override
    public User findByEmail(HttpServletRequest request, String email) {
        String token = getTokenFromHeader(request);
        tokenService.validateAdminToken(token);
        return repo.findByEmail(email);
    }

    @Override
    public Boolean adminValidateToken(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        return tokenService.validateAdminToken(token);

    }

    @Override
    public User getUserInfo(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        return tokenService.validateUserToken(token);
    }

    @Override
    public GetAllUsersInfoResponse getAllUsersInfo(HttpServletRequest request) {
        long usersCount = repo.count();
        int pagesCount = (int) (usersCount / WMSConstant.PAGE_SIZE_DEFAULT);
        return GetAllUsersInfoResponse.builder()
                .usersCount(usersCount)
                .pagesCount(pagesCount++)
                .build();
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
