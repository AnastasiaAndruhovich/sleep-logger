DROP INDEX user_sleep_idx;

ALTER TABLE user_sleep
    ALTER COLUMN created_tms TYPE DATE;
ALTER TABLE user_sleep
    RENAME COLUMN created_tms TO created_date;

ALTER TABLE user_sleep
    ALTER COLUMN fall_asleep_tms TYPE TIME;
ALTER TABLE user_sleep
    RENAME COLUMN fall_asleep_tms TO fall_asleep_time;

ALTER TABLE user_sleep
    ALTER COLUMN wake_up_tms TYPE TIME;
ALTER TABLE user_sleep
    RENAME COLUMN wake_up_tms TO wake_up_time;

CREATE INDEX user_sleep_created_date_idx ON user_sleep (user_id, created_date);
CREATE UNIQUE INDEX user_email_unique_ids ON users (email);