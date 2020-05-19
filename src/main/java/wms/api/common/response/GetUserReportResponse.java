package wms.api.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import wms.api.dao.entity.ReportMonth;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserReportResponse {
    private List<ReportMonth> reportYear;
    private ReportMonth reportMonth;
}
