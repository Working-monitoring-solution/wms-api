package wms.api.common.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String email;
    private String password;
    private String name;
    private String managerId;
    private boolean roleAdmin;
    private String departmentId;
    private String positionId;
}
