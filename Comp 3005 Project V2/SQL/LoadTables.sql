--Clear Tables
delete from equipment_maintenance;
delete from equipment;
delete from session_members;
delete from session;
delete from room;
delete from health_metrics;
delete from exercise_routines;
delete from fitness_goals;
delete from users;

-- error with ids so eqipment_maintenace and session members not loaded

--Users
insert into users (user_email, first_name,last_name, account_type, sex, password,signup_date) values ('max@gmail.com','max','Fletcher','Trainer','M','1','2024-03-12');
insert into users (user_email, first_name,last_name, account_type, sex, password,signup_date) values ('1','jackson','scott','Admin','M','1','2024-04-01');
insert into users (user_email, first_name,last_name, account_type, sex, password,signup_date) values ('tom@gmail.com','tom','brick','Member','M','1','2024-04-12');
insert into users (user_email, first_name,last_name, account_type, sex, password,signup_date) values ('tomtom','Tom','Tom','Member','M','1','2023-12-10');
insert into users (user_email, first_name,last_name, account_type, sex, password,signup_date) values ('ashley','ashley','forths','Trainer','F','1','2023-06-23');
insert into users (user_email, first_name,last_name, account_type, sex, password,signup_date) values ('mack@gmail.com','macklinnley','Jolinah','Member','F','1','2024-01-01');

--Fitness Goals
INSERT INTO fitness_goals (user_email, goal, start_date, completion_date) VALUES ('1', 'sleep more than 8 hours', '2024-04-12', '2024-04-12');
INSERT INTO fitness_goals (user_email, goal, start_date, completion_date) VALUES ( '1', 'drink 2 liters of water', '2024-04-12', '2024-04-12');
INSERT INTO fitness_goals (user_email, goal, start_date, completion_date) VALUES ('1', 'touch my toes', '2024-04-12', '2024-04-12');
INSERT INTO fitness_goals (user_email, goal, start_date, completion_date) VALUES ('1', 'do a push up', '2024-04-13', NULL);
INSERT INTO fitness_goals (user_email, goal, start_date, completion_date) VALUES ('1', 'doom', '2024-04-13', NULL);

--Exercise Routines k
INSERT INTO exercise_routines (user_email,routine,start_date) VALUES ('1','50 sit ups','2024-04-13');
INSERT INTO exercise_routines (user_email,routine,start_date) VALUES ('1','run 5km','2024-04-13');
INSERT INTO exercise_routines (user_email,routine,start_date) VALUES ('1','50 push ups','2024-04-13');

--Health Metrics k
delete from health_metrics;
INSERT INTO health_metrics (user_email, weight, height, heart_rate, bmi, input_date) VALUES ('1', 200, 180, 72, 24, '2024-04-08');
INSERT INTO health_metrics (user_email, weight, height, heart_rate, bmi, input_date) VALUES ('1', 205, 180, 74, 25, '2024-04-12');
INSERT INTO health_metrics (user_email, weight, height, heart_rate, bmi, input_date) VALUES ('1', 210, 180, 76, 27, '2024-05-13');
INSERT INTO health_metrics (user_email, weight, height, heart_rate, bmi, input_date) VALUES ('1', 207, 180, 70, 25, '2024-05-21');

--Rooms
INSERT INTO room (room_num, name) VALUES (3, 'Multipurpose Room');
INSERT INTO room (room_num, name) VALUES (1, 'Cardio Room');
INSERT INTO room (room_num, name) VALUES (2, 'Lifting Room');

--Session
INSERT INTO session (user_email, max_size, date, start_time, end_time, room_num, cost) VALUES ('max@gmail.com', 1, '2024-05-02', '09:30:00', '10:30:00', 1, 30);
INSERT INTO session (user_email, max_size, date, start_time, end_time, room_num, cost) VALUES ('max@gmail.com', 1, '2024-05-02', '10:30:00', '11:30:00', 1, 30);
INSERT INTO session (user_email, max_size, date, start_time, end_time, room_num, cost) VALUES ('max@gmail.com', 1, '2024-05-02', '11:30:00', '12:30:00', 1, 30);
INSERT INTO session (user_email, max_size, date, start_time, end_time, room_num, cost) VALUES ('max@gmail.com', 1, '2024-05-02', '12:30:00', '13:30:00', 1, 30);
INSERT INTO session (user_email, max_size, date, start_time, end_time, room_num, cost) VALUES ('max@gmail.com', 1, '2024-05-03', '09:30:00', '10:30:00', 1, 30);
INSERT INTO session (user_email, max_size, date, start_time, end_time, room_num, cost) VALUES ('max@gmail.com', 1, '2024-05-03', '10:30:00', '11:30:00', 1, 30);
INSERT INTO session (user_email, max_size, date, start_time, end_time, room_num, cost) VALUES ('max@gmail.com', 1, '2024-05-03', '11:30:00', '12:30:00', 1, 30);
INSERT INTO session (user_email, max_size, date, start_time, end_time, room_num, cost) VALUES ('max@gmail.com', 1, '2024-05-03', '12:30:00', '13:30:00', 1, 30);
INSERT INTO session (user_email, max_size, date, start_time, end_time, room_num, cost) VALUES ('ashley', 4, '2024-04-15', '13:30:00', '15:00:00', 3, 10);
INSERT INTO session (user_email, max_size, date, start_time, end_time, room_num, cost) VALUES ('ashley', 20, '2024-05-30', '15:00:00', '17:00:00', 2, 40);

--Equipment
INSERT INTO equipment (name, open, room_num) VALUES ('Treadmill', 't', 1);
INSERT INTO equipment (name, open, room_num) VALUES ('Bar Bell', 't', 2);
INSERT INTO equipment (name, open, room_num) VALUES ('pull up bar', 't', 2);
INSERT INTO equipment (name, open, room_num) VALUES ('Escalator', 't', 1);
INSERT INTO equipment (name, open, room_num) VALUES ('Spin Bike', 'f', 1);
INSERT INTO equipment (name, open, room_num) VALUES ('Pool Table', 't', 3);
INSERT INTO equipment (name, open, room_num) VALUES ('Ping Pong Table', 'f', 3);
INSERT INTO equipment (name, open, room_num) VALUES ('Spin Bike', 't', 1);
INSERT INTO equipment (name, open, room_num) VALUES ('Rower', 'f', 2);







