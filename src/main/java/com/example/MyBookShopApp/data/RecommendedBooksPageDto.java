package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.entity.BookEntity;
import lombok.Data;

import java.util.List;

@Data
public class RecommendedBooksPageDto {

    private Integer count;

    private List<BookEntity> books;

    public RecommendedBooksPageDto(List<BookEntity> books) {
        this.books = books;
        this.count = books.size();
    }
}
