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
    public User validateAdminToken(String token) {
        User user = validateToken(token);
        if (!user.isRoleAdmin()) {
            throw new WMSException.AuthorizationErrorException();
        }
        return user;
    }

    @Override
    public User validateUserToken(String token) {
        return validateToken(token);
    }

    private User validateToken(String token) {
        if (ObjectUtils.isEmpty(token)) {
            throw new WMSException.AuthenticationErrorException();
        }
        try {
            Long userId = getIdFromToken(token);
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new WMSException.NotFoundEntityException("user")
            );
            if (!token.equals(user.getToken())) {
                throw new WMSException.AuthenticationErrorException();
            }
            if (!user.isActive()) {
                throw new WMSException.NotActiveEntityException("user");
            }
            return user;
        } catch (Exception e) {
            throw new WMSException.AuthenticationErrorException();
        }
    }
}
