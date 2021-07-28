DROP TABLE IF EXISTS book_file;

CREATE TABLE book_file
(
    id      BIGSERIAL    NOT NULL PRIMARY KEY,
    hash    VARCHAR(255) NOT NULL,
    type_id INT          NOT NULL,
    path    VARCHAR(255) NOT NULL
);