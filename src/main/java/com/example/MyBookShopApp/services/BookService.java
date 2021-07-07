package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.entity.Book;
import com.example.MyBookShopApp.repository.JDBC.JdbcBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private JdbcBookRepository jdbcBookRepository;

    @Autowired
    public BookService(JdbcBookRepository jdbcBookRepository) {
        this.jdbcBookRepository = jdbcBookRepository;
    }

    public List<Book> getBooksData() {
        return jdbcBookRepository.findAll();
    }

//    public List<Book> getBooksData() {
//        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {
//
//            Author author = authorRepository.findByIdForShure(rs.getLong("id"));
//
//            Book book = new Book();
//            book.setId(rs.getLong("id"));
//            book.setAuthorName(author.getAuthor());
//            book.setTitle(rs.getString("title"));
//            book.setPriceOld(rs.getString("priceOld"));
//            book.setPrice(rs.getString("price"));
//            return book;
//        });
//        return new ArrayList<>(books);
//    }
}
