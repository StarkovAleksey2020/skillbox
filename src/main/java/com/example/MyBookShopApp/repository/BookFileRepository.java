package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.book.file.BookFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookFileRepository extends JpaRepository<BookFileEntity, Long> {

    BookFileEntity findBookFileEntityByHash(String hash);
}
