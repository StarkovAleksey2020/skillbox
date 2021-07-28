DROP TABLE IF EXISTS author;

CREATE TABLE author
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY,
    photo       VARCHAR(255),
    slug        VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description TEXT
);