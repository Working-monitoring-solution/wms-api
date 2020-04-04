package wms.api.service.internal.impl;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import wms.api.dao.entity.User;
import wms.api.dao.repo.UserRepository;
import wms.api.exception.WMSException;
import wms.api.service.internal.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${spring.jwt.secretkey}")
    String secretkey;

    @Value("${spring.jwt.admin.username}")
    String adminUsername;

    @Value("${spring.jwt.admin.password}")
    String adminPassword;

    @Autowired
    UserRepository userRepository;

    @Override
    public Long getIdFromToken(String token) {
        try {
            Long userId = Long.valueOf(Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody().getSubject());
            return userId;
        } catch (Exception e) {
            throw new WMSException.AuthenticationErrorException();
        }
    }

    @Override
    public boolean validateAdminToken(String token) {
        if (ObjectUtils.isEmpty(token)) {
            throw new WMSException.AuthenticationErrorException();
        }
        try {
            String user = Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody().getSubject();
            return user.equals(adminUsername);
        } catch (Exception e) {
            throw new WMSException.AuthenticationErrorException();
        }
    }

    @Override
    public User validateUserToken(String token) {
        try {
            Long userId = getIdFromToken(token);
            User user = userRepository.findById(userId).orElse(null);
            if (ObjectUtils.isEmpty(user)) {
                throw new WMSException.NotFoundEntityException();
            }
            if (!user.isActive()) {
                throw new WMSException.UserNotActiveException();
            }
            return user;
        } catch (Exception e) {
            throw new WMSException.AuthenticationErrorException();
        }
    }
}
