package wms.api.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "report_month")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportMonth implements Serializable {

    private static final long serialVersionUID = -2420743188803222830L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @Column(name = "month")
    private int month;

    @Column(name = "year")
    private int year;

    @Column(name = "unauthorized_absence")
    private int unauthorizedAbsence;

    @Column(name = "day_off")
    private int dayOff;

    @Column(name = "working_date")
    private int workingDate;

    @Column(name = "work_late")
    private int workLate;

    @Column(name = "home_soon")
    private int homeSoon;

    @Column(name = "offsite_time")
    private int offSiteTime;
}
