package wms.api.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WMSResponse<T> implements Serializable {


    private static final long serialVersionUID = 1242527930034345824L;

    @ApiModelProperty(notes = "Mã code", example = "00")
    protected String code;

    @ApiModelProperty(notes = "Message trả về", example = "Successfully")
    protected String message;

    @ApiModelProperty(notes = "Dữ liệu trả về")
    protected T data;
}
