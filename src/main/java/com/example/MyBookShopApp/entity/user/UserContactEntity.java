package com.example.MyBookShopApp.entity.user;

import com.example.MyBookShopApp.entity.enums.ContactType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_contact")
public class UserContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @JoinColumn(name = "user_id")
//    @Column(columnDefinition = "INT NOT NULL")
//    private int userId;

    @JoinColumn(name = "type")
    private ContactType type;

    @Column(name = "approved", columnDefinition = "SMALLINT NOT NULL")
    private short approved;

    @Column(name = "code", columnDefinition = "VARCHAR(255) NOT NULL")
    private String code;

    @Column(name = "code_trails", columnDefinition = "INT")
    private int codeTrails;

    @Column(name = "code_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime codeTime;

    @Column(name = "contact", columnDefinition = "VARCHAR(255) NOT NULL")
    private String contact;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}
