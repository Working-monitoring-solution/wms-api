package wms.api.service.internal;

import wms.api.dao.entity.User;

public interface TokenService {

    public Long getIdFromToken(String token);

    public boolean validateAdminToken(String token);

    public User validateUserToken(String token);
}
