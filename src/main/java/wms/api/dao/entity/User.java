package wms.api.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "user")
public class User implements Serializable {


    private static final long serialVersionUID = -6513920956800909606L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "token")
    private String token;

    @Column(name = "exprired_date")
    private long expiredDate;

    @Column(name = "status")
    private boolean status;

    @Column(name = "avatar")
    private String avatar;
}
