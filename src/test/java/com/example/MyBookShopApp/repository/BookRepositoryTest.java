package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookRepositoryTest {

    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryTest(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    void findBookEntitiesByTitleContaining() {
        String token = "Batman";
        List<BookEntity> bookEntities = bookRepository.findBookEntitiesByTitleContaining(token);

        assertNotNull(bookEntities);
        assertFalse(bookEntities.isEmpty());

        for (BookEntity bookEntity : bookEntities) {
            Logger.getLogger(this.getClass().getSimpleName()).info(bookEntity.getTitle());
            assertThat(bookEntity.getTitle().contains(token));
        }
    }

    @Test
    void getBestsellers() {
        List<BookEntity> bookEntities = bookRepository.getBestsellers();
        assertNotNull(bookEntities);
        assertFalse(bookEntities.isEmpty());
        assertThat(bookEntities.size()).isGreaterThan(1);
    }
}