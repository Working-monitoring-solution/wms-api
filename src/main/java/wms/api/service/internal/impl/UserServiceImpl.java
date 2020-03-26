package wms.api.service.internal.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
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
    public User createUser(CreateUserRequest createUserRequest, HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        if (ObjectUtils.isEmpty(token) || )

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

    private String generateToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .signWith(SignatureAlgorithm.HS256, secretkey)
                .compact();
        user.setToken(token);
        return token;
    }

    private boolean authenticate(String hashed, String password) {
        return BCrypt.checkpw(password, hashed);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(Constant.SALT_ROUND));
    }
}
