DROP TABLE IF EXISTS message;

CREATE TABLE message
(
    id      BIGSERIAL    NOT NULL PRIMARY KEY,
    time    TIMESTAMP    NOT NULL,
    user_id INT,
    email   VARCHAR(255),
    name    VARCHAR(255),
    subject VARCHAR(255) NOT NULL,
    text    TEXT         NOT NULL
);