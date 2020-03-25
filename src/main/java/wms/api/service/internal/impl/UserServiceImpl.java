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

@Service
public class UserServiceImpl extends BaseServiceImpl<UserRepository, User, Long> implements UserService {

    public UserServiceImpl() throws Exception {
    }

    @Override
    public String login(UserLoginRequest request) {
        User user = repo.findByEmail(request.getEmail());
        if (ObjectUtils.isEmpty(user) || !authenticate(user.getPassword(), request.getPassword())) {
            throw new WMSException.LoginFailException();
        }
        if (!ObjectUtils.isEmpty(user.getToken())) {
            return user.getToken();
        }
        String token = generateToken(user);
        repo.save(user);
        return token;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(hashPassword(request.getPassword()))
                .avatar(request.getAvatar())
                .build();
        generateToken(user);
        repo.save(user);
        return user;
    }

    private String generateToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .signWith(SignatureAlgorithm.ES256, secretkey)
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
