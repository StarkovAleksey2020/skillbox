package com.example.MyBookShopApp.entity.book.review;

import com.example.MyBookShopApp.entity.user.UserEntity;
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
@Table(name = "message")
@ApiModel(description = "messages in the feedback form")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "automatically generated field", position = 1)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    @ApiModelProperty(value = "Date and time when the message was sent", position = 2)
    private LocalDateTime time;

    @Column(columnDefinition = "VARCHAR(255)")
    @ApiModelProperty(value = "User's email, if he was not logged in", position = 3)
    private String email;

    @Column(columnDefinition = "VARCHAR(255)")
    @ApiModelProperty(value = "Username if not logged in", position = 4)
    private String name;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    @ApiModelProperty(value = "Message subject", position = 5)
    private String subject;

    @Column(columnDefinition = "TEXT NOT NULL")
    @ApiModelProperty(value = "Message text", position = 6)
    private String text;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ApiModelProperty(value = "If the user was logged in", position = 7)
    private UserEntity user;

}
