DROP TABLE IF EXISTS book2genre;

CREATE TABLE book2genre
(
    id       BIGSERIAL NOT NULL PRIMARY KEY,
    book_id  INT       NOT NULL,
    genre_id INT       NOT NULL
);