DROP TABLE IF EXISTS book;

CREATE TABLE book
(
    id            BIGSERIAL    NOT NULL PRIMARY KEY,
    pub_date      DATE         NOT NULL,
    is_bestseller SMALLINT     NOT NULL,
    slug          VARCHAR(255) NOT NULL,
    title         VARCHAR(255) NOT NULL,
    image         VARCHAR(255),
    description   TEXT,
    price         INT          NOT NULL,
    discount      SMALLINT     NOT NULL DEFAULT 0
);