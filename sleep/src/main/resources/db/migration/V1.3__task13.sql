TRUNCATE user_sleep;

DROP INDEX user_sleep_created_date_idx;

ALTER TABLE user_sleep
    ADD CONSTRAINT user_sleep_user_id_created_date_unique_idx
        UNIQUE (user_id, created_date);

-- Generate 100 random sleep logs
DO
$$
    DECLARE
        gen_user_id                  INTEGER;
        gen_fall_asleep_time         TIME;
        gen_wake_up_time             TIME;
        gen_total_sleep_time         INTERVAL;
        gen_sleeping_time_in_minutes INTEGER;
        gen_feeling                  feeling;
        gen_created_date             DATE;
    BEGIN
        FOR i IN 1..100
            LOOP
                -- Random user_id from users table
                SELECT id
                INTO gen_user_id
                FROM users
                OFFSET FLOOR(RANDOM() * (SELECT COUNT(*) FROM users)) LIMIT 1;

                -- Random created_date within the last 2 months (including today)
                gen_created_date := CURRENT_DATE - FLOOR(RANDOM() * 60)::INTEGER;

                -- Ensure the created_date is within the last 2 months
                IF gen_created_date < CURRENT_DATE - INTERVAL '2 months' THEN
                    gen_created_date := CURRENT_DATE - FLOOR(RANDOM() * 60)::INTEGER;
                END IF;

                -- Random fall_asleep_time within any time of the day (24 hours)
                gen_fall_asleep_time := TIME '00:00:00' + (RANDOM() * INTERVAL '24 hours');

                -- Random wake_up_time based on fall_asleep_time, ensuring a realistic sleep duration
                gen_wake_up_time := gen_fall_asleep_time + (RANDOM() * INTERVAL '7 hours') + INTERVAL '2 hours';

                -- Calculate the total sleep time as an interval
                gen_total_sleep_time := gen_wake_up_time - gen_fall_asleep_time;

                -- If wake_up_time is earlier in the day than fall_asleep_time (i.e., crossing midnight), add 1 day to the total sleep time
                IF gen_total_sleep_time < INTERVAL '0 hours' THEN
                    gen_total_sleep_time := gen_total_sleep_time + INTERVAL '1 day';
                END IF;

                -- Convert total sleep time to minutes
                gen_sleeping_time_in_minutes := EXTRACT(EPOCH FROM gen_total_sleep_time) / 60;

                -- Simulate interrupted sleep by randomly reducing total sleep time
                gen_sleeping_time_in_minutes := gen_sleeping_time_in_minutes * (50 + FLOOR(RANDOM() * 51)) / 100;

                -- Ensure sleeping_time_in_minutes is positive by taking the absolute value
                gen_sleeping_time_in_minutes := ABS(gen_sleeping_time_in_minutes);

                -- Random feeling
                SELECT UNNEST(ARRAY ['GOOD', 'OK', 'BAD'])
                INTO gen_feeling
                OFFSET FLOOR(RANDOM() * 3);

                -- Insert into the table
                INSERT INTO user_sleep (created_date, fall_asleep_time, wake_up_time, sleeping_time_in_minutes, feeling, user_id)
                VALUES (gen_created_date, gen_fall_asleep_time, gen_wake_up_time, gen_sleeping_time_in_minutes, gen_feeling, gen_user_id)
                ON CONFLICT (user_id, created_date) DO NOTHING;  -- Ensures no duplicate combination
            END LOOP;
    END
$$;