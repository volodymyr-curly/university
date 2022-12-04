package ua.foxminded.university.data;

import ua.foxminded.university.domain.Employment;
import ua.foxminded.university.domain.Gender;
import ua.foxminded.university.dto.address.AddressDTO;
import ua.foxminded.university.dto.department.DepartmentRequestDTO;
import ua.foxminded.university.dto.department.DepartmentResponseDTO;
import ua.foxminded.university.dto.duration.DurationRequestDTO;
import ua.foxminded.university.dto.faculty.FacultyRequestDTO;
import ua.foxminded.university.dto.group.GroupRequestDTO;
import ua.foxminded.university.dto.lecture.LectureResponseDTO;
import ua.foxminded.university.dto.person.PersonDTO;
import ua.foxminded.university.dto.room.RoomRequestDTO;
import ua.foxminded.university.dto.student.StudentRequestDTO;

import java.time.LocalDate;
import java.util.Collections;

public final class DTOData {

    public static FacultyRequestDTO setFacultyDTO() {
        return FacultyRequestDTO.builder()
            .id(0)
            .name("Chemistry")
            .build();
    }

    public static FacultyRequestDTO setUpdatedFacultyDTO() {
        return FacultyRequestDTO.builder()
            .id(1)
            .name("Chemistry")
            .build();
    }

    public static DepartmentRequestDTO setDepartmentRequestDTO() {
        return DepartmentRequestDTO.builder()
            .id(0)
            .name("Organic Chemistry")
            .facultyId(1)
            .facultyName("Chemistry")
            .build();
    }

    public static DepartmentResponseDTO setDepartmentResponseDTO() {
        return DepartmentResponseDTO.builder()
            .id(1)
            .name("Organic Chemistry")
            .facultyName("Chemistry")
            .build();
    }

    public static DepartmentRequestDTO setUpdatedDepartmentDTO() {
        return DepartmentRequestDTO.builder()
            .id(1)
            .name("Organic Chemistry")
            .facultyId(1)
            .facultyName("Chemistry")
            .build();
    }

    public static GroupRequestDTO setGroupDTO() {
        return GroupRequestDTO.builder()
            .id(0)
            .name("Org_ch-1")
            .build();
    }

    public static GroupRequestDTO setUpdatedGroupDTO() {
        return GroupRequestDTO.builder()
            .id(1)
            .name("Org_ch-1")
            .build();
    }

    public static GroupRequestDTO setUpdatedGroupDTOWihLectures() {
        return GroupRequestDTO.builder()
            .id(1)
            .name("Org_ch-1")
            .lectures(Collections.singletonList(new LectureResponseDTO()))
            .build();
    }

    public static PersonDTO setPersonDTO() {
        return StudentRequestDTO.builder()
            .id(0)
            .email("person@mail.com")
            .build();
    }

    public static PersonDTO setUpdatedPersonDTO() {
        return StudentRequestDTO.builder()
            .id(1)
            .email("person@mail.com")
            .build();
    }

    public static DurationRequestDTO setDurationDTO() {
        return DurationRequestDTO.builder()
            .id(0)
            .startTime("09:00")
            .endTime("10:20")
            .build();
    }

    public static RoomRequestDTO setRoomDTO() {
        return RoomRequestDTO.builder()
            .id(0)
            .number(101)
            .build();
    }

    public static RoomRequestDTO setUpdatedRoomDTO() {
        return RoomRequestDTO.builder()
            .id(1)
            .number(101)
            .build();
    }

    public static StudentRequestDTO setStudentDTO() {
        return StudentRequestDTO.builder()
            .lastName("Ivan")
            .firstName("Ivanenko")
            .gender(Gender.MALE)
            .birthDate(LocalDate.of(1990,1,1))
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
            .groupId(1)
            .educationForm(Employment.FULL_TIME)
            .enrollmentDate(LocalDate.of(2022,9,1))
            .build();
    }
}
