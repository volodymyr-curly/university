package ua.foxminded.university.data;

import ua.foxminded.university.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class EntityData {

    public static List<Faculty> setExpectedFaculties() {
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(Faculty.builder()
            .id(1)
            .name("Chemistry")
            .departments(setDepartments())
            .build());

        faculties.add(Faculty.builder()
            .id(1)
            .name("Economics")
            .departments(setDepartments())
            .build());
        return faculties;
    }

    public static List<Department> setExpectedDepartments() {
        List<Department> departments = new ArrayList<>();
        departments.add(Department.builder()
            .id(1)
            .name("Organic chemistry")
            .faculty(setExpectedFaculties().get(0))
            .groups(setGroups())
            .employees(setEmployees())
            .build());

        departments.add(Department.builder()
            .id(1)
            .name("Chemistry")
            .faculty(setExpectedFaculties().get(0))
            .groups(setGroups())
            .employees(setEmployees())
            .build());
        return departments;
    }

    public static List<Group> setExpectedGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(Group.builder()
            .id(1)
            .name("Org_ch-1")
            .department(Department.builder().id(1).build())
            .students(setStudents())
            .subjects(setSubjects())
            .lectures(setLectures())
            .build());

        groups.add(Group.builder()
            .id(1)
            .name("Fin_ec-1")
            .department(Department.builder().id(1).build())
            .students(setStudents())
            .subjects(setSubjects())
            .lectures(setLectures())
            .build());
        return groups;
    }

    public static List<Student> setExpectedStudents() {
        List<Student> students = new ArrayList<>();
        students.add(Student.builder()
            .id(1)
            .firstName("Ivan")
            .lastName("Ivanenko")
            .email("person@mail.com")
            .address(Address.builder().id(1).country("Ukraine").build())
            .group(Group.builder().id(1).build())
            .marks(setMarks())
            .build());

        students.add(Student.builder()
            .id(1)
            .firstName("Ihor")
            .lastName("Ivanenko")
            .email("student@mail.com")
            .address(Address.builder().id(1).country("Ukraine").build())
            .group(Group.builder().id(1).build())
            .marks(setMarks())
            .build());
        return students;
    }

    public static List<Mark> setExpectedMarks() {
        List<Mark> marks = new ArrayList<>();
        marks.add(Mark.builder()
            .id(1)
            .value(MarkValue.A)
            .subject(Subject.builder().id(1).name("Chemistry").build())
            .student(Student.builder().id(1).firstName("Tom").build())
            .build());

        marks.add(Mark.builder()
            .id(1)
            .value(MarkValue.B)
            .subject(Subject.builder().id(1).name("Chemistry").build())
            .student(Student.builder().id(1).firstName("Tom").build())
            .build());
        return marks;
    }

    public static List<Employee> setExpectedEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(Employee.builder()
            .id(5)
            .firstName("Ihor")
            .lastName("Ihorenko")
            .address(Address.builder().id(1).country("Ukraine").build())
            .department(Department.builder().id(1).name("Chemistry").build())
            .employmentType(Employment.FULL_TIME)
            .build());

        employees.add(Employee.builder()
            .id(5)
            .firstName("Ivan")
            .lastName("Ihorenko")
            .address(Address.builder().id(1).country("Ukraine").build())
            .department(Department.builder().id(1).name("Chemistry").build())
            .employmentType(Employment.FULL_TIME)
            .build());
        return employees;
    }

    public static List<Subject> setExpectedSubjects() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.builder()
            .id(1)
            .name("Chemistry")
            .startDate(LocalDate.of(2021, 9, 1))
            .endDate(LocalDate.of(2021, 12, 31))
            .groups(setGroups())
            .teachers(setTeachers())
            .lectures(setLectures())
            .build());

        subjects.add(Subject.builder()
            .id(1)
            .name("Chemistry")
            .startDate(LocalDate.of(2021, 8, 1))
            .endDate(LocalDate.of(2021, 10, 31))
            .groups(setGroups())
            .teachers(setTeachers())
            .lectures(setLectures())
            .build());
        return subjects;
    }

    public static List<Teacher> setInitialTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(Teacher.builder()
            .id(1)
            .degree(Degree.MASTER)
            .employee(setExpectedEmployees().get(0))
            .subjects(setSubjects())
            .build());
        return teachers;
    }

    public static List<Teacher> setExpectedTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(Teacher.builder()
            .id(1)
            .degree(Degree.MASTER)
            .employee(setExpectedEmployees().get(0))
            .subjects(setSubjects())
            .lectures(setLectures())
            .build());
        return teachers;
    }

    public static List<Lecture> setExpectedLectures() {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(Lecture.builder()
            .id(1)
            .subject(Subject.builder().id(1).name("Chemistry").build())
            .teacher(Teacher.builder().id(1).build())
            .groups(setGroups())
            .room(LectureRoom.builder().number(104).build())
            .duration(Duration.builder().id(1).build())
            .date(LocalDate.of(2021, 9, 1))
            .build());

        lectures.add(Lecture.builder()
            .id(1)
            .subject(Subject.builder().id(1).name("Chemistry").build())
            .teacher(Teacher.builder().id(2).build())
            .groups(setGroups())
            .room(LectureRoom.builder().number(104).build())
            .duration(Duration.builder().id(1).build())
            .date(LocalDate.of(2021, 9, 1))
            .build());
        return lectures;
    }

    public static List<Duration> setExpectedDurations() {
        List<Duration> durations = new ArrayList<>();
        durations.add(Duration.builder()
            .id(1)
            .startTime("09:00")
            .endTime("10:20")
            .build());
        return durations;
    }

    public static List<LectureRoom> setInitialRooms() {
        List<LectureRoom> rooms = new ArrayList<>();
        rooms.add(LectureRoom.builder()
            .id(1)
            .number(101)
            .capacity(10)
            .build());
        return rooms;
    }

    public static List<LectureRoom> setExpectedRooms() {
        List<LectureRoom> rooms = new ArrayList<>();
        rooms.add(LectureRoom.builder()
            .number(101)
            .capacity(10)
            .lectures(setLectures())
            .build());

        rooms.add(LectureRoom.builder()
            .number(102)
            .capacity(10)
            .lectures(setLectures())
            .build());
        return rooms;
    }

    public static List<Address> setExpectedAddresses() {
        List<Address> addresses = new ArrayList<>();
        Address address = Address.builder()
            .id(3)
            .country("Ukraine")
            .region("Dnipro")
            .city("Dnipro")
            .street("Dniprovska 2")
            .apartment("2b")
            .postcode("49000")
            .build();
        addresses.add(address);
        return addresses;
    }

    public static List<Department> setDepartments() {
        List<Department> departments = new ArrayList<>();
        departments.add(Department.builder().id(1).name("Organic chemistry").build());
        departments.add(Department.builder().id(2).name("Analytical chemistry").build());
        return departments;
    }

    public static List<Group> setGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(Group.builder().id(1).build());
        groups.add(Group.builder().id(2).build());
        return groups;
    }

    public static List<Employee> setEmployees() {
        List<Employee> employees = new ArrayList<>();
        employees.add(Employee.builder().id(1).build());
        employees.add(Employee.builder().id(2).build());
        return employees;
    }

    public static List<Student> setStudents() {
        List<Student> students = new ArrayList<>();
        students.add(Student.builder().id(1).build());
        students.add(Student.builder().id(2).build());
        return students;
    }

    public static List<Subject> setSubjects() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.builder()
            .id(1)
            .name("Chemistry")
            .groups(setGroups())
            .teachers(setTeachers())
            .build());
        subjects.add(Subject.builder()
            .id(2)
            .name("Geometry")
            .groups(setGroups())
            .teachers(setTeachers())
            .build());
        return subjects;
    }

    public static List<Lecture> setLectures() {
        List<Lecture> lectures = new ArrayList<>();
        lectures.add(Lecture.builder()
            .id(1)
            .groups(setGroups())
            .build());
        lectures.add(Lecture.builder()
            .id(2)
            .groups(setGroups())
            .build());
        return lectures;
    }

    public static List<Mark> setMarks() {
        List<Mark> marks = new ArrayList<>();
        marks.add(Mark.builder().id(1).build());
        marks.add(Mark.builder().id(2).build());
        return marks;
    }

    public static List<Teacher> setTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(Teacher.builder().id(1).build());
        teachers.add(Teacher.builder().id(2).build());
        return teachers;
    }
}
