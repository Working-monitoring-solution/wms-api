package wms.api.service.internal;

public interface TokenService {

    public Long getIdFromToken(String token);

    public boolean validateAdminToken(String token);

    public boolean validateUserToken(String token);
}
