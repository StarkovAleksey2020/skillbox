DROP TABLE IF EXISTS user_contact;

CREATE TYPE contact_type AS ENUM ('PHONE', 'EMAIL');

CREATE TABLE user_contact
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY,
    user_id     INT          NOT NULL,
    type        contact_type,
    approved    SMALLINT     NOT NULL,
    code        VARCHAR(255),
    code_trials INT,
    code_time   TIMESTAMP,
    contact     VARCHAR(255) NOT NULL
);