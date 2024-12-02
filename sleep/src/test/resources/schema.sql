CREATE TABLE users
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(40) NOT NULL,
    email VARCHAR(40) NOT NULL
);
CREATE UNIQUE INDEX user_email_unique_ids ON users (email);

CREATE TYPE feeling AS ENUM ('GOOD', 'OK', 'BAD');

CREATE TABLE user_sleep
(
    id                       SERIAL PRIMARY KEY,
    fall_asleep_time         TIME    NOT NULL,
    wake_up_time             TIME    NOT NULL,
    feeling                  FEELING NOT NULL,
    user_id                  INTEGER NOT NULL
        REFERENCES users
            ON DELETE CASCADE,
    sleeping_time_in_minutes INTEGER NOT NULL,
    created_date             DATE    NOT NULL
);
CREATE INDEX user_sleep_created_date_idx ON user_sleep (user_id, created_date);