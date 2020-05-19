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
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = -6513920956800909606L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

    @Column(name = "status")
    private boolean active;

    @Column(name = "createdDate")
    @JsonFormat(pattern = Utils.ddMMyyyy)
    private Date createdDate;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "role_admin")
    private boolean roleAdmin;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User manager;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Department department;

    @ManyToOne
    @JoinColumn(name = "position_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Position position;
}
