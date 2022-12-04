INSERT INTO faculties (faculty_name)
VALUES ('Chemistry');
INSERT INTO faculties (faculty_name)
VALUES ('Economics');

INSERT INTO departments (department_name, faculty_id)
VALUES ('Organic Chemistry', 1);
INSERT INTO departments (department_name, faculty_id)
VALUES ('Analytical Chemistry', 1);
INSERT INTO departments (department_name, faculty_id)
VALUES ('Finance', 2);

INSERT INTO groups (group_name, department_id)
VALUES ('Org_ch-1', 1);
INSERT INTO groups (group_name, department_id)
VALUES ('Org_ch-2', 1);
INSERT INTO groups (group_name, department_id)
VALUES ('An_ch-1', 1);
INSERT INTO groups (group_name, department_id)
VALUES ('Fin_f-1', 3);

INSERT INTO people (first_name, last_name, gender, birth_date, email, phone_number, role, password)
VALUES ('Ivan', 'Ivanenko', 'MALE', '1990-01-01', 'student@mail.com', '(066)1234567', 'ROLE_STUDENT',
        '$2a$10$Zmi31iZcIb5wql.KQ5bT5uYheyW9YjgPJrJ0YKqOjXNJVIgNA3/Ua');
INSERT INTO people (first_name, last_name, gender, birth_date, email, phone_number, role, password)
VALUES ('Petro', 'Petrenko', 'MALE', '1990-02-02', 'petrenko@mail.com', '(066)7654321', 'ROLE_STUDENT',
        '$2a$10$KD/2ZNs47DJTRigIRfxHHOiiHfYitnQTHOcVxsAmQAjn3ZKIvZkNi');
INSERT INTO people (first_name, last_name, gender, birth_date, email, phone_number, role, password)
VALUES ('Olena', 'Olenenko', 'FEMALE', '1991-03-03', 'olenenko@mail.com', '(066)1111111', 'ROLE_STUDENT',
        '$2a$10$Z40vaPKd7z5Oq0GRxrA9EuCzaj/TBZF3/y6A6p9wuYNLJgcsnSP0q');
INSERT INTO people (first_name, last_name, gender, birth_date, email, phone_number, role, password)
VALUES ('Serhiy', 'Serhienko', 'MALE', '1989-04-04', 'serhienko@mail.com', '(066)1111111', 'ROLE_STUDENT',
        '$2a$10$6FmAJgkCPScirz/.DFU5ZeLlArFvwd.T5naUp1h8/Pn6x1wHecfLq');
INSERT INTO people (first_name, last_name, gender, birth_date, email, phone_number, role, password)
VALUES ('Ihor', 'Ihorenko', 'MALE', '1990-01-01', 'admin@mail.com', '(066)2222222', 'ROLE_ADMIN',
        '$2a$10$2wAPhd6pIvNgsmixeCZSE.Ble5nFpdBrlJZBKj2E3LKHfctjQqrkm');
INSERT INTO people (first_name, last_name, gender, birth_date, email, phone_number, role, password)
VALUES ('Taras', 'Tarasenko', 'MALE', '1990-02-02', 'teacher@mail.com', '(066)3333333', 'ROLE_TEACHER',
        '$2a$10$rPWHr3EkNWxlHEyMRzmLsux.tzxz8dYhLLBsEVRAeLr/.Wa3jPAha');
INSERT INTO people (first_name, last_name, gender, birth_date, email, phone_number, role, password)
VALUES ('Olga', 'Ivanenko', 'FEMALE', '1991-03-03', 'ivanenkoO@mail.com', '(066)4444444', 'ROLE_TEACHER',
        '$2a$10$qOj8SgRKVeLjDDNIy8MK6ecXg0cfhaxRFxYL58snzp0IPmtJ4YJw.');
INSERT INTO people (first_name, last_name, gender, birth_date, email, phone_number, role, password)
VALUES ('Yurii', 'Yurishyn', 'MALE', '1989-04-04', 'employee@mail.com', '(066)5555555', 'ROLE_EMPLOYEE',
        '$2a$10$ez75iSKC6FMO2H.Aj9QYhe.nn3/hTX4c/QkQ3bA3PzApFD4MBYsuW');

INSERT INTO addresses (country, region, city, street, apartment, postcode, person_id)
VALUES ('Ukraine', 'Kyiv', 'Kyiv', 'Kyivska 1', '1a', '01000', 1);
INSERT INTO addresses (country, region, city, street, apartment, postcode, person_id)
VALUES ('Ukraine', 'Kyiv', 'Kyiv', 'Kyivska 2', '2a', '01000', 2);
INSERT INTO addresses (country, region, city, street, apartment, postcode, person_id)
VALUES ('Ukraine', 'Dnipro', 'Dnipro', 'Dniprovska 2', '2b', '49000', 3);
INSERT INTO addresses (country, region, city, street, apartment, postcode, person_id)
VALUES ('Ukraine', 'Dnipro', 'Dnipro', 'Dniprovska 3', '3b', '49000', 4);
INSERT INTO addresses (country, region, city, street, apartment, postcode, person_id)
VALUES ('Ukraine', 'Lviv', 'Lviv', 'Lvivska 4', '4c', '79000', 5);
INSERT INTO addresses (country, region, city, street, apartment, postcode, person_id)
VALUES ('Ukraine', 'Lviv', 'Lviv', 'Lvivska 3', '3c', '79000', 6);
INSERT INTO addresses (country, region, city, street, apartment, postcode, person_id)
VALUES ('Ukraine', 'Kharkiv', 'Kharkiv', 'Kharkivska 4', '4d', '61000', 7);
INSERT INTO addresses (country, region, city, street, apartment, postcode, person_id)
VALUES ('Ukraine', 'Kharkiv', 'Kharkiv', 'Kharkivska 5', '5d', '61000', 8);

INSERT INTO students (id, group_id, education_form, enrollment_date)
VALUES (1, 1, 'FULL_TIME', '2021-09-01');
INSERT INTO students (id, group_id, education_form, enrollment_date)
VALUES (2, 2, 'FULL_TIME', '2020-09-01');
INSERT INTO students (id, group_id, education_form, enrollment_date)
VALUES (3, 3, 'PART_TIME', '2019-09-01');
INSERT INTO students (id, group_id, education_form, enrollment_date)
VALUES (4, 4, 'FULL_TIME', '2019-09-01');

INSERT INTO employees (id, department_id, job_title, employment_type, employment_date)
VALUES (5, 1, 'TEACHER', 'FULL_TIME', '2021-09-01');
INSERT INTO employees (id, department_id, job_title, employment_type, employment_date)
VALUES (6, 1, 'TEACHER', 'FULL_TIME', '2020-09-01');
INSERT INTO employees (id, department_id, job_title, employment_type, employment_date)
VALUES (7, 2, 'ASSISTANT', 'PART_TIME', '2019-09-01');
INSERT INTO employees (id, department_id, job_title, employment_type, employment_date)
VALUES (8, 3, 'DEPARTMENT_HEAD', 'FULL_TIME', '2019-09-01');

INSERT INTO teachers (teacher_id, degree, employee_id)
VALUES (1, 'MASTER', 5);
INSERT INTO teachers (teacher_id, degree, employee_id)
VALUES (2, 'DOCTOR', 6);
INSERT INTO teachers (teacher_id, degree, employee_id)
VALUES (3, 'BACHELOR', 7);

INSERT INTO subjects (subject_name, start_date, end_date)
VALUES ('Chemistry', '2022-09-01', '2022-12-31');
INSERT INTO subjects (subject_name, start_date, end_date)
VALUES ('Cosmetic Chemistry', '2022-09-01', '2022-12-31');
INSERT INTO subjects (subject_name, start_date, end_date)
VALUES ('Medical Chemistry', '2022-09-01', '2022-12-31');
INSERT INTO subjects (subject_name, start_date, end_date)
VALUES ('Finance And Credit', '2022-09-01', '2022-12-31');
INSERT INTO subjects (subject_name, start_date, end_date)
VALUES ('Economics And Law', '2022-09-01', '2022-12-31');

INSERT INTO groups_subjects (group_id, subject_id)
VALUES (1, 2);
INSERT INTO groups_subjects (group_id, subject_id)
VALUES (1, 3);
INSERT INTO groups_subjects (group_id, subject_id)
VALUES (2, 1);
INSERT INTO groups_subjects (group_id, subject_id)
VALUES (2, 2);
INSERT INTO groups_subjects (group_id, subject_id)
VALUES (2, 3);
INSERT INTO groups_subjects (group_id, subject_id)
VALUES (3, 1);
INSERT INTO groups_subjects (group_id, subject_id)
VALUES (3, 2);
INSERT INTO groups_subjects (group_id, subject_id)
VALUES (3, 3);
INSERT INTO groups_subjects (group_id, subject_id)
VALUES (4, 4);
INSERT INTO groups_subjects (group_id, subject_id)
VALUES (4, 5);
INSERT INTO groups_subjects (group_id, subject_id)
VALUES (4, 1);

INSERT INTO teachers_subjects (teacher_id, subject_id)
VALUES (2, 1);
INSERT INTO teachers_subjects (teacher_id, subject_id)
VALUES (1, 3);
INSERT INTO teachers_subjects (teacher_id, subject_id)
VALUES (3, 4);
INSERT INTO teachers_subjects (teacher_id, subject_id)
VALUES (1, 5);
INSERT INTO teachers_subjects (teacher_id, subject_id)
VALUES (3, 1);


INSERT INTO lecture_rooms (room_number, capacity)
VALUES (101, 10);
INSERT INTO lecture_rooms (room_number, capacity)
VALUES (102, 20);
INSERT INTO lecture_rooms (room_number, capacity)
VALUES (103, 30);
INSERT INTO lecture_rooms (room_number, capacity)
VALUES (104, 40);

INSERT INTO marks (mark_value, student_id, subject_id)
VALUES ('A', 1, 1);
INSERT INTO marks (mark_value, student_id, subject_id)
VALUES ('B', 1, 2);
INSERT INTO marks (mark_value, student_id, subject_id)
VALUES ('C', 1, 3);
INSERT INTO marks (mark_value, student_id, subject_id)
VALUES ('A', 2, 1);
INSERT INTO marks (mark_value, student_id, subject_id)
VALUES ('B', 2, 2);
INSERT INTO marks (mark_value, student_id, subject_id)
VALUES ('C', 2, 3);
INSERT INTO marks (mark_value, student_id, subject_id)
VALUES ('A', 3, 1);
INSERT INTO marks (mark_value, student_id, subject_id)
VALUES ('B', 3, 2);
INSERT INTO marks (mark_value, student_id, subject_id)
VALUES ('C', 3, 3);
INSERT INTO marks (mark_value, student_id, subject_id)
VALUES ('C', 4, 4);

INSERT INTO durations(start_time, end_time)
VALUES ('09:00', '10:20');
INSERT INTO durations(start_time, end_time)
VALUES ('10:30', '11:50');
INSERT INTO durations(start_time, end_time)
VALUES ('12:30', '13:50');
INSERT INTO durations(start_time, end_time)
VALUES ('14:20', '15:40');
INSERT INTO durations(start_time, end_time)
VALUES ('16:10', '17:30');
INSERT INTO durations(start_time, end_time)
VALUES ('17:40', '19:00');

INSERT INTO lectures(subject_id, teacher_id, room_id, duration_id, date)
VALUES (1, 1, 4, 1, '2022-12-01');
INSERT INTO lectures(subject_id, teacher_id, room_id, duration_id, date)
VALUES (2, 2, 1, 1, '2022-12-01');
INSERT INTO lectures(subject_id, teacher_id, room_id, duration_id, date)
VALUES (3, 2, 3, 3, '2022-12-01');
INSERT INTO lectures(subject_id, teacher_id, room_id, duration_id, date)
VALUES (1, 1, 2, 4, '2022-12-01');
INSERT INTO lectures(subject_id, teacher_id, room_id, duration_id, date)
VALUES (4, 3, 1, 5, '2022-12-01');

INSERT INTO groups_lectures(group_id, lecture_id)
VALUES (2, 1);
INSERT INTO groups_lectures(group_id, lecture_id)
VALUES (1, 2);
INSERT INTO groups_lectures(group_id, lecture_id)
VALUES (2, 2);
INSERT INTO groups_lectures(group_id, lecture_id)
VALUES (3, 1);
INSERT INTO groups_lectures(group_id, lecture_id)
VALUES (1, 3);
INSERT INTO groups_lectures(group_id, lecture_id)
VALUES (2, 3);
INSERT INTO groups_lectures(group_id, lecture_id)
VALUES (3, 3);
INSERT INTO groups_lectures(group_id, lecture_id)
VALUES (3, 4);
INSERT INTO groups_lectures(group_id, lecture_id)
VALUES (4, 1);




