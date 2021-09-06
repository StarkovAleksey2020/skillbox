package com.example.MyBookShopApp.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NamedNativeQuery(
        name = "find_book_review_info_dto",
        query =
                "SELECT " +
                        "  br.id as id, " +
                        "  br.time as reviewTime, " +
                        "  br.text as textReview, " +
                        "  ue.name as userName, " +
                        "  br2.rate as userBookRate, " +
                        "  count(*) filter (where brl.value = 1) as reviewLikesCount, " +
                        "  count(*) filter (where brl.value = 0) as reviewDislikesCount " +
                        "from book_review br " +
                        "join user_entity ue on br.user_id = ue.id " +
                        "join book_review_like brl on brl.review_id = br.id " +
                        "join book2rate br2 on br2.book_id = br.book_id and br2.user_id = br.user_id " +
                        "  where br.book_id = :bookId " +
                        "group by br.id, br.time, br.text, ue.name, br2.rate",
        resultSetMapping = "stock_book_review_info_dto"
)
@SqlResultSetMapping(
        name = "stock_book_review_info_dto",
        classes = @ConstructorResult(
                targetClass = BookReviewRLDto.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "reviewTime", type = LocalDateTime.class),
                        @ColumnResult(name = "textReview", type = String.class),
                        @ColumnResult(name = "userName", type = String.class),
                        @ColumnResult(name = "userBookRate", type = Integer.class),
                        @ColumnResult(name = "reviewLikesCount", type = Integer.class),
                        @ColumnResult(name = "reviewDislikesCount", type = Integer.class)
                }
        )
)
public class BufferEntity {
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
