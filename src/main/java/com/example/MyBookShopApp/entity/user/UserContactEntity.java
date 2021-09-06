package com.example.MyBookShopApp.entity.user;

import com.example.MyBookShopApp.entity.enumiration.ContactType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_contact")
@ApiModel(description = "user contact table")
public class UserContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @JoinColumn(name = "type")
    @ApiModelProperty(value = "Contact type (phone or e-mail)")
    private ContactType type;

    @Column(name = "approved", columnDefinition = "SMALLINT NOT NULL")
    @ApiModelProperty(value = "Is the contact confirmed (0 or 1)")
    private Short approved;

    @Column(name = "code", columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty(value = "Confirmation code")
    private String code;

    @Column(name = "code_trails", columnDefinition = "INT")
    @ApiModelProperty(value = "The number of attempts to enter the confirmation code")
    private Integer codeTrails;

    @Column(name = "code_time", columnDefinition = "TIMESTAMP")
    @ApiModelProperty(value = "Date and time of generation of the confirmation code")
    private LocalDateTime codeTime;

    @Column(name = "contact", columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty(value = "Contact (e-mail or phone)")
    private String contact;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ApiModelProperty(value = "The identifier of the user this contact belongs to")
    private UserEntity userEntityContact;
}
