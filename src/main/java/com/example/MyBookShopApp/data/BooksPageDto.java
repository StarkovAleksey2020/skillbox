package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.entity.BookEntity;
import com.example.MyBookShopApp.entity.other.LinkEntity;
import lombok.Data;

import java.util.List;

@Data
public class BooksPageDto {

    private Integer count;

    private List<BookEntity> books;

    public BooksPageDto(List<BookEntity> books) {
        this.books = books;
        this.count = books.size();
    }

    public BooksPageDto() {
    }
}
