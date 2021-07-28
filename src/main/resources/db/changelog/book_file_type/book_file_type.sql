DROP TABLE IF EXISTS book_file_type;

CREATE TABLE book_file_type
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT
);