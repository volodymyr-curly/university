package ua.foxminded.university.data.restcontrollers;

import ua.foxminded.university.domain.Employment;
import ua.foxminded.university.domain.Gender;
import ua.foxminded.university.domain.JobTitle;
import ua.foxminded.university.domain.Role;
import ua.foxminded.university.dto.address.AddressDTO;
import ua.foxminded.university.dto.employee.EmployeeDTO;

import java.time.LocalDate;

public class EmployeeTestingData {

    public static final String SHOW_ALL_URL = "/api/employees";
    public static final String SHOW_NOT_TEACHERS_URL = "/api/employees/not-teachers/list";
    public static final String SHOW_BY_DEPARTMENT_URL = "/api/employees/department/list";
    public static final String SHOW_URL = "/api/employees/{id}";
    public static final String EDIT_URL = "/api/employees/{id}/edit";
    public static final String DELETE_URL = "/api/employees/{id}/delete";
    public static final String REGISTRATION_URL = "/api/employee";

    public static final String NOT_FOUND_MESSAGE = "Person not found";
    public static final String LIST_NOT_FOUND_MESSAGE = "Not found";
    public static final String EXISTS_MESSAGE = "Person already exists";

    public static final int LIST_SIZE = 4;
    public static final int LIST_BY_DEPARTMENT_SIZE = 2;
    public static final int LIST_NOT_TEACHERS_SIZE = 1;
    public static final int ID = 6;
    public static final int PARAM_ID = 1;
    public static final int NOT_FOUND_ID = 10;
    public static final int ID_TO_UPDATE = 7;
    public static final String INVALID_DATA = "a";

    public final static String VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"departmentId\"," +
        "\"rejectedValue\":null,\"messageError\":\"Should not be empty\"}]}";

    public final static String EMAIL_VALIDATION_ERROR_RESPONSE = "{\"errorMessage\":[{\"fieldName\":\"email\"," +
        "\"rejectedValue\":\"petrenko@mail.com\",\"messageError\":\"This email is already taken\"}]}";

    public EmployeeDTO generateEmployeeForCreate() {
        return EmployeeDTO.builder()
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
            .email("employeee@mail.com")
            .phoneNumber("(066)1231234")
            .password("123456")
            .departmentId(1)
            .jobTitle(JobTitle.ASSISTANT)
            .employmentType(Employment.FULL_TIME)
            .employmentDate(LocalDate.of(2022, 9, 1))
            .build();
    }

    public EmployeeDTO generateEmployeeWithNotUniqueEmail() {
        return EmployeeDTO.builder()
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
            .departmentId(1)
            .jobTitle(JobTitle.ASSISTANT)
            .employmentType(Employment.FULL_TIME)
            .employmentDate(LocalDate.of(2022, 9, 1))
            .build();
    }

    public EmployeeDTO generateEmployeeForUpdate() {
        return EmployeeDTO.builder()
            .id(6)
            .firstName("Taras")
            .lastName("Tarasenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990, 2, 2))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Lviv")
                .city("Lviv")
                .street("Lvivska 3")
                .apartment("3c")
                .postcode("79000")
                .build())
            .email("teacher@mail.com")
            .phoneNumber("(066)3333333")
            .password("123456")
            .departmentId(1)
            .departmentName("Organic Chemistry")
            .jobTitle(JobTitle.TEACHER)
            .employmentType(Employment.FULL_TIME)
            .employmentDate(LocalDate.of(2020, 9, 1))
            .build();
    }

    public EmployeeDTO generateUpdatedEmployee() {
        return EmployeeDTO.builder()
            .id(0)
            .firstName("Taras")
            .lastName("Tarasenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990, 2, 2))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Lviv")
                .city("Lviv")
                .street("Lvivska 3")
                .apartment("3c")
                .postcode("79000")
                .build())
            .email("teacherr@mail.com")
            .phoneNumber("(066)3333333")
            .password("123456")
            .role(Role.ROLE_TEACHER)
            .departmentId(1)
            .departmentName("Organic Chemistry")
            .jobTitle(JobTitle.TEACHER)
            .employmentType(Employment.FULL_TIME)
            .employmentDate(LocalDate.of(2020, 9, 1))
            .build();
    }

    public EmployeeDTO generateUpdatedEmployeeWithNotUniqueEmail() {
        return EmployeeDTO.builder()
            .id(0)
            .firstName("Taras")
            .lastName("Tarasenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990, 2, 2))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Lviv")
                .city("Lviv")
                .street("Lvivska 3")
                .apartment("3c")
                .postcode("79000")
                .build())
            .email("petrenko@mail.com")
            .phoneNumber("(066)3333333")
            .password("123456")
            .departmentId(1)
            .departmentName("Organic Chemistry")
            .jobTitle(JobTitle.TEACHER)
            .employmentType(Employment.FULL_TIME)
            .employmentDate(LocalDate.of(2020, 9, 1))
            .build();
    }

    public EmployeeDTO generateEmployeeForFindById() {
        return EmployeeDTO.builder()
            .id(6)
            .firstName("Taras")
            .lastName("Tarasenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990, 2, 2))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Lviv")
                .city("Lviv")
                .street("Lvivska 3")
                .apartment("3c")
                .postcode("79000")
                .build())
            .email("teacher@mail.com")
            .phoneNumber("(066)3333333")
            .password("123456")
            .departmentId(1)
            .departmentName("Organic Chemistry")
            .jobTitle(JobTitle.TEACHER)
            .employmentType(Employment.FULL_TIME)
            .employmentDate(LocalDate.of(2020, 9, 1))
            .build();
    }

    public EmployeeDTO generateInvalidEmployee() {
        return EmployeeDTO.builder()
            .id(0)
            .firstName("Taras")
            .lastName("Tarasenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990, 2, 2))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Lviv")
                .city("Lviv")
                .street("Lvivska 3")
                .apartment("3c")
                .postcode("79000")
                .build())
            .email("teacherr@mail.com")
            .phoneNumber("(066)3333333")
            .password("123456")
            .departmentId(null)
            .departmentName("Organic Chemistry")
            .jobTitle(JobTitle.TEACHER)
            .employmentType(Employment.FULL_TIME)
            .employmentDate(LocalDate.of(2020, 9, 1))
            .build();
    }

    public EmployeeDTO generateExistedEmployee() {
        return EmployeeDTO.builder()
            .id(0)
            .firstName("Taras")
            .lastName("Tarasenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990, 2, 2))
            .address(AddressDTO.builder()
                .country("Ukraine")
                .region("Lviv")
                .city("Lviv")
                .street("Lvivska 3")
                .apartment("3c")
                .postcode("79000")
                .build())
            .email("teacherr@mail.com")
            .phoneNumber("(066)3333333")
            .password("123456")
            .role(Role.ROLE_TEACHER)
            .departmentId(1)
            .departmentName("Organic Chemistry")
            .jobTitle(JobTitle.TEACHER)
            .employmentType(Employment.FULL_TIME)
            .employmentDate(LocalDate.of(2020, 9, 1))
            .build();
    }
}
