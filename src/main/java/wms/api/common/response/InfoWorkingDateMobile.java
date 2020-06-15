package wms.api.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoWorkingDateMobile {

    String timeCheckin;
    String lastTimeOnsite;
}
