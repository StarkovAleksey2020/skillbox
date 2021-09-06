package com.example.MyBookShopApp.mapper;

import com.example.MyBookShopApp.data.BookReviewRLDto;
import com.example.MyBookShopApp.entity.book.links.Book2RateEntity;
import com.example.MyBookShopApp.entity.book.review.BookReviewEntity;
import com.example.MyBookShopApp.repository.BookRateRepository;
import com.example.MyBookShopApp.repository.BookReviewLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookReviewRLDtoMapper {

    private BookReviewLikeRepository bookReviewLikeRepository;
    private BookRateRepository bookRateRepository;

    @Autowired
    public BookReviewRLDtoMapper(BookReviewLikeRepository bookReviewLikeRepository, BookRateRepository bookRateRepository) {
        this.bookReviewLikeRepository = bookReviewLikeRepository;
        this.bookRateRepository = bookRateRepository;
    }

    public BookReviewRLDto map(BookReviewEntity bookReviewEntity) {
        BookReviewRLDto bookReviewRLDto = new BookReviewRLDto();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedString = bookReviewEntity.getTime().format(formatter);

        bookReviewRLDto.setReviewId(bookReviewEntity.getId());
        bookReviewRLDto.setReviewTime(formattedString);
        bookReviewRLDto.setTextReview(bookReviewEntity.getText());
        bookReviewRLDto.setUserName(bookReviewEntity.getUserEntity().getName());
        Book2RateEntity book2RateEntity = bookRateRepository.findBook2Rate(bookReviewEntity.getUserEntity().getId(), bookReviewEntity.getBookEntity().getId());
        if (book2RateEntity != null) {
            bookReviewRLDto.setUserBookRate(bookRateRepository.findBook2Rate(bookReviewEntity.getUserEntity().getId(), bookReviewEntity.getBookEntity().getId()).getRate());
        }

        bookReviewRLDto.setReviewLikesCount(bookReviewLikeRepository.getLikesCount(bookReviewEntity.getId()));
        bookReviewRLDto.setReviewDislikesCount(bookReviewLikeRepository.getDislikesCount(bookReviewEntity.getId()));
        return bookReviewRLDto;
    }

    public List<BookReviewRLDto> map(List<BookReviewEntity> bookReviewEntities) {
        List<BookReviewRLDto> bookReviewRLDtos = new ArrayList<>();
        for (BookReviewEntity entity : bookReviewEntities) {
            bookReviewRLDtos.add(map(entity));
        }
        return bookReviewRLDtos;
    }

}
