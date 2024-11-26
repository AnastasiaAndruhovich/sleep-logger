CREATE TABLE USERS
(
    ID    SERIAL PRIMARY KEY,
    NAME  varchar(40) NOT NULL,
    EMAIL varchar(40) NOT NULL
);

CREATE TYPE FEELING AS ENUM ('GOOD', 'OK', 'BAD');

CREATE TABLE USER_SLEEP
(
    ID              SERIAL PRIMARY KEY,
    CREATED_TMS     timestamp,
    FALL_ASLEEP_TMS timestamp,
    WAKE_UP_TMS     timestamp,
    FEELING         FEELING,
    USER_ID         integer references USERS
);

CREATE INDEX USER_SLEEP_IDX ON USER_SLEEP (USER_ID);