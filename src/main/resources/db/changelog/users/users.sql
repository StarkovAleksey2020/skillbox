DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id       BIGSERIAL    NOT NULL PRIMARY KEY,
    hash     VARCHAR(255) NOT NULL,
    reg_time TIMESTAMP    NOT NULL,
    balance  INT          NOT NULL,
    name     VARCHAR(255)
);