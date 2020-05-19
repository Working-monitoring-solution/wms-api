package wms.api.service.internal;

import wms.api.dao.entity.User;

public interface TokenService {

    public Long getIdFromToken(String token);

    public User validateAdminToken(String token);

    public User validateUserToken(String token);
}
