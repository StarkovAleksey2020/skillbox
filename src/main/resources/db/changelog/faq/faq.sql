DROP TABLE IF EXISTS faq;

CREATE TABLE faq
(
    id         BIGSERIAL    NOT NULL PRIMARY KEY,
    sort_index INT          NOT NULL DEFAULT 0,
    question   VARCHAR(255) NOT NULL,
    answer     TEXT         NOT NULL
);