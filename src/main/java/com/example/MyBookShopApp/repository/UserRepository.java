package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "FROM UserEntity u WHERE u.id = :id", nativeQuery = false)
    UserEntity findByIdExactly(Long id);

    UserEntity findByEmail(String email);

    UserEntity findByName(String name);
}
