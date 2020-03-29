package wms.api.common.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String email;
    private String password;
    private String name;
    private String avatar;
}
