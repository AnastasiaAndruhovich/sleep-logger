ALTER TABLE user_sleep
    DROP COLUMN created_tms;

DROP INDEX user_sleep_idx;
CREATE INDEX user_fal_asleep_idx ON user_sleep (user_id, fall_asleep_tms);