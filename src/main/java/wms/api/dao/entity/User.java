package wms.api.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User implements Serializable {


    private static final long serialVersionUID = -6513920956800909606L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition="VARCHAR(255) COLLATE utf8_unicode_ci")
    private String name;

    @Column(columnDefinition = "varchar(20) default ''")
    private String phoneNumber;


}
