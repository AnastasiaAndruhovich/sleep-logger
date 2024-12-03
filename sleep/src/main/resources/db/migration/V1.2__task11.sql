ALTER TABLE user_sleep
    ADD CONSTRAINT check_positive CHECK (sleeping_time_in_minutes >= 0);

INSERT INTO users (name, email)
VALUES ('Harry Potter', 'harry.potter@test.com'),
       ('Hermione Granger', 'hermione.granger@test.com'),
       ('Tom Riddle', 'tom.riddle@test.com');

-- Generate 100 random sleep logs
DO
$$
    DECLARE
        user_id                  INTEGER;
        fall_asleep_time         TIME;
        wake_up_time             TIME;
        total_sleep_time         INTERVAL;
        sleeping_time_in_minutes INTEGER;
        feeling                  feeling;
        created_date             DATE;
    BEGIN
        FOR i IN 1..100
            LOOP
                -- Random user_id from users table
                SELECT id
                INTO user_id
                FROM users
                OFFSET FLOOR(RANDOM() * (SELECT COUNT(*) FROM users)) LIMIT 1;

                -- Random created_date within the last 2 months (including today)
                created_date := CURRENT_DATE - FLOOR(RANDOM() * 60)::INTEGER;

                -- Random fall_asleep_time within any time of the day (24 hours)
                fall_asleep_time := TIME '00:00:00' + (RANDOM() * INTERVAL '24 hours');

                -- Random wake_up_time based on fall_asleep_time, ensuring a realistic sleep duration
                wake_up_time := fall_asleep_time + (RANDOM() * INTERVAL '7 hours') + INTERVAL '2 hours';

                -- Calculate the total sleep time as an interval
                total_sleep_time := wake_up_time - fall_asleep_time;

                -- If wake_up_time is earlier in the day than fall_asleep_time (i.e., crossing midnight), add 1 day to the total sleep time
                IF total_sleep_time < INTERVAL '0 hours' THEN
                    total_sleep_time := total_sleep_time + INTERVAL '1 day';
                END IF;

                -- Convert total sleep time to minutes
                sleeping_time_in_minutes := EXTRACT(EPOCH FROM total_sleep_time) / 60;

                -- Simulate interrupted sleep by randomly reducing total sleep time
                sleeping_time_in_minutes := sleeping_time_in_minutes * (50 + FLOOR(RANDOM() * 51)) / 100;

                -- Ensure sleeping_time_in_minutes is positive by taking the absolute value
                sleeping_time_in_minutes := ABS(sleeping_time_in_minutes);

                -- Random feeling
                SELECT UNNEST(ARRAY ['GOOD', 'OK', 'BAD'])
                INTO feeling
                OFFSET FLOOR(RANDOM() * 3);

                -- Insert into the table
                INSERT INTO user_sleep (created_date, fall_asleep_time, wake_up_time, sleeping_time_in_minutes, feeling, user_id)
                VALUES (created_date, fall_asleep_time, wake_up_time, sleeping_time_in_minutes, feeling, user_id);
            END LOOP;
    END
$$;



