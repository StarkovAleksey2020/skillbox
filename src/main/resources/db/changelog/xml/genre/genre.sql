DROP TABLE IF EXISTS genre;

CREATE TABLE genre
(
    id        BIGSERIAL NOT NULL PRIMARY KEY,
    parent_id INT,
    slug      VARCHAR(255),
    name      VARCHAR(255)
);