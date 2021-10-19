package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {
    @Query(value = "FROM SmsCode sc WHERE sc.code = :code")
    public SmsCode findByCode(@RequestParam("code") String code);
}
