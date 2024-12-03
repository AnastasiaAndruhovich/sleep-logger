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
    sleeping_time_in_minutes INTEGER NOT NULL
        CONSTRAINT check_positive
            CHECK (sleeping_time_in_minutes >= 0),
    created_date             DATE    NOT NULL,
    CONSTRAINT user_sleep_user_id_created_date_unique_idx
        UNIQUE (user_id, created_date)
);