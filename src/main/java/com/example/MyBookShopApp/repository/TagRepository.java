package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.tag.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {
    TagEntity findByName(String tagName);

    @Query(value = "FROM TagEntity te where te.id = :id")
    TagEntity findByIdExactly(@Param("id") Long id);
}
