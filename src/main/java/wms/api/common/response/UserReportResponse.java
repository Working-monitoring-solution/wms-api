package wms.api.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReportResponse {
    private List<ReportResponse> reportYear;
    private ReportResponse reportMonth;
}
