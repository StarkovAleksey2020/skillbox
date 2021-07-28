DROP TABLE IF EXISTS balance_transaction;

CREATE TABLE balance_transaction
(
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    user_id     INT       NOT NULL,
    time        TIMESTAMP NOT NULL,
    value       INT       NOT NULL DEFAULT 0,
    book_id     INT       NOT NULL,
    description TEXT      NOT NULL
);