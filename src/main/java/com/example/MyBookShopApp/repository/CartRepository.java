package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.cart.CartEntity;
import com.example.MyBookShopApp.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    CartEntity findByUserEntity(UserEntity userEntity);
}
