DROP TABLE IF EXISTS file_download;

CREATE TABLE file_download
(
    id      BIGSERIAL NOT NULL PRIMARY KEY,
    user_id INT       NOT NULL,
    book_id INT       NOT NULL,
    count   INT       NOT NULL DEFAULT 1
);