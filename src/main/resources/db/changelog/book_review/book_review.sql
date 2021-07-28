DROP TABLE IF EXISTS book_review;

CREATE TABLE book_review
(
    id      BIGSERIAL NOT NULL PRIMARY KEY,
    book_id INT       NOT NULL,
    user_id INT       NOT NULL,
    time    TIMESTAMP NOT NULL,
    text    TEXT      NOT NULL
);