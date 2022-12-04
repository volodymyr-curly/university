package ua.foxminded.university.data.restcontrollers;

import ua.foxminded.university.domain.Employment;
import ua.foxminded.university.domain.Gender;
import ua.foxminded.university.dto.address.AddressDTO;
import ua.foxminded.university.dto.student.StudentRequestDTO;
import ua.foxminded.university.dto.student.StudentResponseDTO;

import java.time.LocalDate;

import static ua.foxminded.university.domain.Role.ROLE_STUDENT;

public class StudentTestingData {

    public static final String SHOW_ALL_URL = "/api/students";
    public static final String SHOW_BY_GROUP_URL = "/api/students/group/list";
    public static final String SHOW_URL = "/api/students/{id}";
    public static final String EDIT_URL = "/api/students/{id}/edit";
    public static final String DELETE_URL = "/api/students/{id}/delete";
    public static final String REGISTRATION_URL = "/api/student";

    public static final String NOT_FOUND_MESSAGE = "Person not found";
    public static final String LIST_NOT_FOUND_MESSAGE = "Not found";
    public static final String EXISTS_MESSAGE = "Person already exists";

    public static final int LIST_SIZE = 4;
    public static final int LIST_BY_GROUP_SIZE = 1;
    public static final int ID = 1;
    public static final int PARAM_ID = 1;
    public static final int NOT_FOUND_ID = 10;
    public static final int ID_TO_UPDATE = 2;
    public static final String INVALID_DATA = "a";

    public final static String VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"groupId\"," +
        "\"rejectedValue\":null,\"messageError\":\"Should not be empty\"}]}";

    public final static String EMAIL_VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"email\"," +
        "\"rejectedValue\":\"petrenko@mail.com\",\"messageError\":\"This email is already taken\"}]}";

    public StudentRequestDTO generateStudentForCreate() {
        return StudentRequestDTO.builder()
            .id(0)
            .firstName("Vlad")
            .lastName("Vladlenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1995, 1, 1))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Kyiv")
                .city("Kyiv")
                .street("Kyivska 11")
                .apartment("1")
                .postcode("00000")
                .build())
            .email("studenttt@mail.com")
            .phoneNumber("(066)1231234")
            .password("123456")
            .groupId(1)
            .educationForm(Employment.FULL_TIME)
            .enrollmentDate(LocalDate.of(2022, 9, 1))
            .build();
    }

    public StudentRequestDTO generateStudentWithNotUniqueEmail() {
        return StudentRequestDTO.builder()
            .id(0)
            .firstName("Vlad")
            .lastName("Vladlenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1995, 1, 1))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Kyiv")
                .city("Kyiv")
                .street("Kyivska 11")
                .apartment("1")
                .postcode("00000")
                .build())
            .email("petrenko@mail.com")
            .phoneNumber("(066)1231234")
            .password("123456")
            .groupId(1)
            .educationForm(Employment.FULL_TIME)
            .enrollmentDate(LocalDate.of(2022, 9, 1))
            .build();
    }

    public StudentRequestDTO generateStudentForUpdate() {
        return StudentRequestDTO.builder()
            .id(1)
            .firstName("Ivan")
            .lastName("Ivanenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990, 1, 1))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Kyiv")
                .city("Kyiv")
                .street("Kyivska 1")
                .apartment("1")
                .postcode("00000")
                .build())
            .email("student@mail.com")
            .phoneNumber("(066)1234567")
            .password("123456")
            .groupId(1)
            .groupName("Org_ch-1")
            .educationForm(Employment.FULL_TIME)
            .enrollmentDate(LocalDate.of(2021, 9, 1))
            .build();
    }

    public StudentRequestDTO generateUpdatedStudent() {
        return StudentRequestDTO.builder()
            .id(0)
            .firstName("Ivan")
            .lastName("Ivanenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990, 1, 1))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Kyiv")
                .city("Kyiv")
                .street("Kyivska 1")
                .apartment("1")
                .postcode("00000")
                .build())
            .email("studenttt@mail.com")
            .phoneNumber("(066)1234567")
            .password("123456")
            .role(ROLE_STUDENT)
            .groupId(1)
            .educationForm(Employment.FULL_TIME)
            .enrollmentDate(LocalDate.of(2021, 9, 1))
            .build();
    }

    public StudentRequestDTO generateUpdatedStudentNotWithUniqueEmail() {
        return StudentRequestDTO.builder()
            .id(0)
            .firstName("Ivan")
            .lastName("Ivanenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990, 1, 1))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Kyiv")
                .city("Kyiv")
                .street("Kyivska 1")
                .apartment("1")
                .postcode("00000")
                .build())
            .email("petrenko@mail.com")
            .phoneNumber("(066)1234567")
            .password("123456")
            .groupId(1)
            .educationForm(Employment.FULL_TIME)
            .enrollmentDate(LocalDate.of(2021, 9, 1))
            .build();
    }

    public StudentResponseDTO generateStudentForFindById() {
        return StudentResponseDTO.builder()
            .id(1)
            .firstName("Ivan")
            .lastName("Ivanenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990, 1, 1))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Kyiv")
                .city("Kyiv")
                .street("Kyivska 1")
                .apartment("1")
                .postcode("00000")
                .build())
            .email("student@mail.com")
            .phoneNumber("(066)1234567")
            .password("123456")
            .groupName("Org_ch-1")
            .educationForm(Employment.FULL_TIME)
            .enrollmentDate(LocalDate.of(2021, 9, 1))
            .build();
    }

    public StudentRequestDTO generateInvalidStudent() {
        return StudentRequestDTO.builder()
            .id(0)
            .firstName("Ivan")
            .lastName("Ivanenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990, 1, 1))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Kyiv")
                .city("Kyiv")
                .street("Kyivska 1")
                .apartment("1")
                .postcode("00000")
                .build())
            .email("studenttt@mail.com")
            .phoneNumber("(066)1234567")
            .password("123456")
            .groupId(null)
            .educationForm(Employment.FULL_TIME)
            .enrollmentDate(LocalDate.of(2021, 9, 1))
            .build();
    }

    public StudentRequestDTO generateExistedStudent() {
        return StudentRequestDTO.builder()
            .id(0)
            .firstName("Ivan")
            .lastName("Ivanenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990, 1, 1))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Kyiv")
                .city("Kyiv")
                .street("Kyivska 1")
                .apartment("1")
                .postcode("00000")
                .build())
            .email("studenttt@mail.com")
            .phoneNumber("(066)1234567")
            .password("123456")
            .groupId(1)
            .educationForm(Employment.FULL_TIME)
            .enrollmentDate(LocalDate.of(2021, 9, 1))
            .build();
    }
}
