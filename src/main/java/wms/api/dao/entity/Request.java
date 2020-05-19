package wms.api.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import wms.api.util.Utils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "request")
public class Request implements Serializable {

    private static final long serialVersionUID = 9116261645971485362L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    @JsonFormat(pattern = Utils.ddMMyyyy)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User manager;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @Column(name = "status")
    private String status;

    @Column(name = "reason")
    private String reason;

    public static final String STATUS_PENDING = "PENDING";

    public static final String STATUS_DENIED = "DENIED";

    public static final String STATUS_APPROVED = "APPROVED";
}
