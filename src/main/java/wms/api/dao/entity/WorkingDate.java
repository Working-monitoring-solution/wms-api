package wms.api.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import wms.api.util.Utils;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "working_date")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkingDate implements Serializable {

    private static final long serialVersionUID = -6488011147337624813L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    @JsonFormat(pattern = Utils.ddMMyyyy)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @Column(name = "permission")
    private boolean permission;

    @Column(name = "check_in")
    @JsonFormat(pattern = Utils.ddMMyyyyHHmmSS)
    private Timestamp checkIn;

    @Column(name = "check_out")
    @JsonFormat(pattern = Utils.ddMMyyyyHHmmSS)
    private Timestamp checkOut;

    @Column(name = "is_come_out")
    private boolean comeOut;

    @Column(name = "at_0800")
    private boolean at0800;

    @Column(name = "at_0815")
    private boolean at0815;

    @Column(name = "at_0830")
    private boolean at0830;

    @Column(name = "at_0845")
    private boolean at0845;

    @Column(name = "at_0900")
    private boolean at0900;

    @Column(name = "at_0915")
    private boolean at0915;

    @Column(name = "at_0930")
    private boolean at0930;

    @Column(name = "at_0945")
    private boolean at0945;

    @Column(name = "at_1000")
    private boolean at1000;

    @Column(name = "at_1015")
    private boolean at1015;

    @Column(name = "at_1030")
    private boolean at1030;

    @Column(name = "at_1045")
    private boolean at1045;

    @Column(name = "at_1100")
    private boolean at1100;

    @Column(name = "at_1115")
    private boolean at1115;

    @Column(name = "at_1130")
    private boolean at1130;

    @Column(name = "at_1145")
    private boolean at1145;

    @Column(name = "at_1200")
    private boolean at1200;

    @Column(name = "at_1300")
    private boolean at1300;

    @Column(name = "at_1315")
    private boolean at1315;

    @Column(name = "at_1330")
    private boolean at1330;

    @Column(name = "at_1345")
    private boolean at1345;

    @Column(name = "at_1400")
    private boolean at1400;

    @Column(name = "at_1415")
    private boolean at1415;

    @Column(name = "at_1430")
    private boolean at1430;

    @Column(name = "at_1445")
    private boolean at1445;

    @Column(name = "at_1500")
    private boolean at1500;

    @Column(name = "at_1515")
    private boolean at1515;

    @Column(name = "at_1530")
    private boolean at1530;

    @Column(name = "at_1545")
    private boolean at1545;

    @Column(name = "at_1600")
    private boolean at1600;

    @Column(name = "at_1615")
    private boolean at1615;

    @Column(name = "at_1630")
    private boolean at1630;

    @Column(name = "at_1645")
    private boolean at1645;

    @Column(name = "at_1700")
    private boolean at1700;
}
