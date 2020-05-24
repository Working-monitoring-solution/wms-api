package wms.api.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wms.api.util.Utils;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestResponse {
    private Long id;
    @JsonFormat(pattern = Utils.ddMMyyyy)
    private Date date;
    private UserInfoResponse user;
    private ManagerResponse manager;
    private String status;
    private String reason;
}
