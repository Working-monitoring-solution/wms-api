package wms.api.common.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChangeInformationRequest {
    private MultipartFile avatar;
    private String password;
}
