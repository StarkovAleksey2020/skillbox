DROP TABLE IF EXISTS book2user_type;

CREATE TABLE book2user_type
(
    id   BIGSERIAL    NOT NULL PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);