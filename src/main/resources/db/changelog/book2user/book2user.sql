DROP TABLE IF EXISTS book2user;

CREATE TABLE book2user
(
    id      BIGSERIAL NOT NULL PRIMARY KEY,
    time    TIMESTAMP,
    type_id INT       NOT NULL,
    book_id INT       NOT NULL,
    user_id INT       NOT NULL
);