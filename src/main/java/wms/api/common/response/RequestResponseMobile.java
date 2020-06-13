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
public class RequestResponseMobile {

    @JsonFormat(pattern = Utils.ddMMyyyy)
    private Date date;

    private String status;

    private String reason;
}
