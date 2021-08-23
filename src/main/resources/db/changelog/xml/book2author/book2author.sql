DROP TABLE IF EXISTS book2author;

CREATE TABLE book2author
(
    id         BIGSERIAL NOT NULL PRIMARY KEY,
    book_id    INT       NOT NULL,
    author_id  INT       NOT NULL,
    sort_index INT       NOT NULL
);