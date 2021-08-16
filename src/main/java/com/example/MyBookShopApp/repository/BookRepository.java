package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.AuthorEntity;
import com.example.MyBookShopApp.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    List<BookEntity> findBookEntitiesByAuthorSetContaining(AuthorEntity authorEntity);

    List<BookEntity> findBookEntitiesByTitleContaining(String bookTitle);

    List<BookEntity> findBookEntitiesByPriceBetween(Integer min, Integer max);

    List<BookEntity> findBookEntitiesByPriceIs(Integer price);

    @Query("from BookEntity where isBestseller=1")
    List<BookEntity> getBestsellers();

    @Query(value = "SELECT * FROM book WHERE discount = (SELECT MAX(discount) FROM book)", nativeQuery = true)
    List<BookEntity> getBooksWithMaxDiscount();

    @Query(value = "from BookEntity where id = :id")
    BookEntity findByIdExactly(@RequestParam("id") Long id);

    Page<BookEntity> findBookEntitiesByTitleContaining(String bookTitle, Pageable nextPage);

    @Query(value = "FROM BookEntity u WHERE u.pubDate >= :dateFrom AND u.pubDate <= :dateTo")
    Page<BookEntity> getBookEntityByPubDateInterval(@RequestParam("dateFrom") OffsetDateTime dateFrom,
                                                    @RequestParam("dateTo") OffsetDateTime dateTo,
                                                    Pageable nextPage);

    Page<BookEntity> getBookEntityByPubDateAfter(OffsetDateTime dateFrom, Pageable pageable);

    @Query(value = "from BookEntity u where u.pubDate >= :dateFrom AND u.pubDate <= :dateTo ORDER BY u.pubDate DESC")
    Page<BookEntity> findBookEntitiesByPubDateAfterAndPubDateBefore(OffsetDateTime dateFrom, OffsetDateTime dateTo, Pageable pageable);

    Page<BookEntity> getBookEntityByPubDateBetween(OffsetDateTime dateFrom, OffsetDateTime dateTo, Pageable pageable);

    @Query(value = "from BookEntity u ORDER BY u.pubDate DESC")
    Page<BookEntity> findAllDesc(Pageable nextPage);

    @Query(value = "select b.* from book b join book2user bu on bu.book_id = b.id group by b.id order by (count(case when bu.type_id = 3 then bu.id end) + count(case when bu.type_id = 2 then bu.id end) * 0.7 + count(case when bu.type_id = 1 then bu.id end) * 0.4) desc", nativeQuery = true)
    Page<BookEntity> findBooksByPopularRate(Pageable nextPage);
}
