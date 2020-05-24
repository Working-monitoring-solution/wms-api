package wms.api.common.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportResponse {
    private Long id;
    private UserInfoResponse user;
    private int month;
    private int year;
    private int unauthorizedAbsence;
    private int dayOff;
    private int workingDate;
    private int workLate;
    private int homeSoon;
    private int offSiteTime;
}
