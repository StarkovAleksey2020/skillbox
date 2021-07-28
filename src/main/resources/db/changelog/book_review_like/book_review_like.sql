DROP TABLE IF EXISTS book_review_like;

CREATE TABLE book_review_like
(
    id        BIGSERIAL NOT NULL PRIMARY KEY,
    review_id INT       NOT NULL,
    user_id   INT       NOT NULL,
    time      TIMESTAMP NOT NULL,
    value     SMALLINT  NOT NULL
);