package wms.api.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportYear implements Serializable {

    private static final long serialVersionUID = 7267135472368494393L;

    private int year;

    private int dayOffNoPermission;

    private int dayOff;

    private int workingDate;

    private int workLate;

    private int homeSoon;

    private int offSiteTime;
}
