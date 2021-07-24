DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS authors;

CREATE TABLE books
(
    id          BIGSERIAL PRIMARY KEY,
    author_id   INT,
    author_name VARCHAR(250) NOT NULL,
    title       VARCHAR(250) NOT NULL,
    price_old   VARCHAR(250) DEFAULT NULL,
    price       VARCHAR(250) DEFAULT NULL
);

CREATE TABLE authors
(
    id     BIGSERIAL PRIMARY KEY,
    author VARCHAR(250) NOT NULL
);

