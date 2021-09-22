package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.other.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    @Query(value = "FROM TokenEntity te where te.token = :token")
    TokenEntity findByToken(@RequestParam("token") String token);
}
