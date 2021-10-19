package com.example.MyBookShopApp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "sms_keys")
public class SmsCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "id")
    private Long id;

    @JoinColumn(name = "code")
    private String code;

    @JoinColumn(name = "expire_time")
    private LocalDateTime expireTime;

    public SmsCode(String code, Integer expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public SmsCode() {}

    public Boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
