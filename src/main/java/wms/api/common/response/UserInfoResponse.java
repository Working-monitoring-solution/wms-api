package wms.api.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wms.api.dao.entity.Department;
import wms.api.dao.entity.Position;
import wms.api.util.Utils;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {
    private Long id;
    private String name;
    private String email;
    private boolean active;
    private String avatar;
    @JsonFormat(pattern = Utils.ddMMyyyy)
    private Date createdDate;
    private boolean roleAdmin;
    private ManagerResponse manager;
    private Department department;
    private Position position;
}
