INSERT INTO users (name, email)
VALUES ('Harry Potter', 'harry.potter@test.com'),
       ('Hermione Granger', 'hermione.granger@test.com'),
       ('Tom Riddle', 'tom.riddle@test.com');

INSERT INTO user_sleep (created_date, fall_asleep_time, wake_up_time, sleeping_time_in_minutes, feeling, user_id)
VALUES ('2024-11-01', '22:30:00', '06:30:00', 323, 'GOOD', 1),
       ('2024-11-03', '23:00:00', '03:00:00', 300, 'BAD', 2),
       ('2024-11-05', '21:45:00', '05:50:00', 434, 'BAD', 1),
       ('2024-11-07', '22:15:00', '06:00:00', 400, 'GOOD', 1),
       ('2024-11-10', '23:10:00', '06:45:00', 465, 'OK', 2),
       ('2024-11-12', '22:40:00', '06:20:00', 256, 'GOOD', 3),
       ('2024-11-14', '22:00:00', '06:10:00', 490, 'BAD', 3),
       ('2024-11-17', '23:30:00', '07:15:00', 367, 'GOOD', 2),
       ('2024-11-20', '21:50:00', '03:30:00', 180, 'BAD', 2),
       ('2024-11-25', '22:10:00', '06:40:00', 240, 'BAD', 1);