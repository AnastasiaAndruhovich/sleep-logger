INSERT INTO user_sleep (created_date, fall_asleep_time, wake_up_time, sleeping_time_in_minutes, feeling, user_id)
VALUES
    (PARSEDATETIME('2024-11-01', 'yyyy-MM-dd'), '22:30:00', '06:30:00', 323, 'GOOD', (SELECT id FROM users WHERE email = 'harry.potter@test.com')),
    (PARSEDATETIME('2024-11-03', 'yyyy-MM-dd'), '23:00:00', '03:00:00', 300, 'BAD', (SELECT id FROM users WHERE email = 'hermione.granger@test.com')),
    (PARSEDATETIME('2024-11-05', 'yyyy-MM-dd'), '21:45:00', '05:50:00', 434, 'BAD', (SELECT id FROM users WHERE email = 'harry.potter@test.com')),
    (PARSEDATETIME('2024-11-07', 'yyyy-MM-dd'), '22:15:00', '06:00:00', 400, 'GOOD', (SELECT id FROM users WHERE email = 'harry.potter@test.com')),
    (PARSEDATETIME('2024-11-10', 'yyyy-MM-dd'), '23:10:00', '06:45:00', 465, 'OK', (SELECT id FROM users WHERE email = 'hermione.granger@test.com')),
    (PARSEDATETIME('2024-11-12', 'yyyy-MM-dd'), '22:40:00', '06:20:00', 256, 'GOOD', (SELECT id FROM users WHERE email = 'tom.riddle@test.com')),
    (PARSEDATETIME('2024-11-14', 'yyyy-MM-dd'), '22:00:00', '06:10:00', 490, 'BAD', (SELECT id FROM users WHERE email = 'tom.riddle@test.com')),
    (PARSEDATETIME('2024-11-17', 'yyyy-MM-dd'), '23:30:00', '07:15:00', 367, 'GOOD', (SELECT id FROM users WHERE email = 'hermione.granger@test.com')),
    (PARSEDATETIME('2024-11-20', 'yyyy-MM-dd'), '21:50:00', '03:30:00', 180, 'BAD', (SELECT id FROM users WHERE email = 'hermione.granger@test.com')),
    (PARSEDATETIME('2024-11-25', 'yyyy-MM-dd'), '22:10:00', '06:40:00', 240, 'BAD', (SELECT id FROM users WHERE email = 'harry.potter@test.com'));
