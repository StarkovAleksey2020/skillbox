package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.entity.AuthorEntity;
import com.example.MyBookShopApp.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    List<BookEntity> findBookEntitiesByAuthorSetContaining(AuthorEntity authorEntity);

    List<BookEntity> findBookEntitiesByTitleContaining(String bookTitle);

    List<BookEntity> findBookEntitiesByPriceBetween(Integer min, Integer max);

    List<BookEntity> findBookEntitiesByPriceIs(Integer price);

    @Query("from BookEntity where isBestseller=1")
    List<BookEntity> getBestsellers();

    @Query(value = "SELECT * FROM book WHERE discount = (SELECT MAX(discount) FROM book)", nativeQuery = true)
    List<BookEntity> getBooksWithMaxDiscount();

    Page<BookEntity> findBookEntitiesByTitleContaining(String bookTitle, Pageable nextPage);

    @Query(value = "FROM BookEntity u WHERE u.pubDate >= :dateFrom AND u.pubDate <= :dateTo")
    Page<BookEntity> getBookEntityByPubDateInterval(@RequestParam("dateFrom") OffsetDateTime dateFrom,
                                                    @RequestParam("dateTo") OffsetDateTime dateTo,
                                                    Pageable nextPage);

    @Query(value = "from BookEntity u where u.pubDate >= :dateFrom AND u.pubDate <= :dateTo ORDER BY u.pubDate DESC")
    Page<BookEntity> findBookEntitiesByPubDateAfterAndPubDateBefore(OffsetDateTime dateFrom, OffsetDateTime dateTo, Pageable pageable);

    @Query(value = "from BookEntity u ORDER BY u.pubDate DESC")
    Page<BookEntity> findAllDesc(Pageable nextPage);

    @Query(value = "select b.* from book b join book2user bu on bu.book_id = b.id group by b.id order by (count(case when bu.type_id = 3 then bu.id end) + count(case when bu.type_id = 2 then bu.id end) * 0.7 + count(case when bu.type_id = 1 then bu.id end) * 0.4) desc", nativeQuery = true)
    Page<BookEntity> findBooksByPopularRate(Pageable nextPage);

    @Query(value = "select b from BookEntity b join Book2TagEntity bt on bt.bookId = b.id join TagEntity t on t.id = bt.tagId where t.name = :tagName")
    Page<BookEntity> findBooksByTagName(String tagName, Pageable nextPage);

    @Query(value = "select b from BookEntity b join Book2GenreEntity bg on bg.bookId = b.id join GenreEntity ge on ge.id = bg.genreId where ge.name = :genreName")
    Page<BookEntity> findBooksByGenreName(String genreName, Pageable nextPage);

    @Query(value = "select b from BookEntity b join Book2AuthorEntity ba on ba.bookId = b.id join AuthorEntity a on a.id = ba.authorId where a.name = :authorName")
    Page<BookEntity> findBooksByAuthor(String authorName, Pageable nextPage);

    @Query(value = "select count(b) from BookEntity b join Book2AuthorEntity ba on ba.bookId = b.id join AuthorEntity a on a.id = ba.authorId where a.name = :authorName")
    Integer findBooksCountByAuthor(String authorName);

    @Query(value = "select b from BookEntity b join Book2GenreEntity bg on b.id = bg.bookId join GenreEntity g on bg.genreId = g.id where g.parentId = :parentId")
    Page<BookEntity> findBooksByFolder(@RequestParam("parentId") Long parentId, Pageable nextPage);

    @Query(value = "FROM BookEntity b WHERE b.slug = :slug")
    BookEntity getBookBySlug(@RequestParam("slug") String slug);
}
