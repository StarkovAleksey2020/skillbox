DROP TABLE IF EXISTS document;

CREATE TABLE document
(
    id         BIGSERIAL    NOT NULL PRIMARY KEY,
    sort_index INT          NOT NULL DEFAULT 0,
    slug       VARCHAR(255) NOT NULL,
    title      VARCHAR(255) NOT NULL,
    text       TEXT         NOT NULL
);