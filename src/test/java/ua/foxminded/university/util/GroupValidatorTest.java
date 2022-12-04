package ua.foxminded.university.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.university.domain.Group;
import ua.foxminded.university.dto.group.GroupRequestDTO;
import ua.foxminded.university.dto.group.GroupResponseDTO;
import ua.foxminded.university.repository.GroupRepository;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;
import ua.foxminded.university.util.validators.GroupValidator;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.DTOData.*;
import static ua.foxminded.university.data.EntityData.setExpectedGroups;

@ExtendWith(SpringExtension.class)
@Import(GroupValidator.class)
class GroupValidatorTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final int EXPECTED_ERRORS_COUNT = 1;
    private static final String EXIST_MESSAGE = "Group is already exist";
    private static final String NOT_EMPTY_MESSAGE = "Should not be empty";
    private static final Group EXPECTED_GROUP = setExpectedGroups().get(0);
    private static final Group EXPECTED_UPDATED_GROUP = setExpectedGroups().get(1);
    private static final GroupRequestDTO GROUP_DTO = setGroupDTO();
    private static final GroupRequestDTO UPDATED_GROUP_DTO = setUpdatedGroupDTO();
    private static final GroupRequestDTO UPDATED_GROUP_DTO_WITH_LECTURES = setUpdatedGroupDTOWihLectures();

    @Autowired
    private Validator groupValidator;

    @MockBean
    private GroupRepository groupRepository;

    @Test
    void whenValidate_thenSupportsClass() {
        assertTrue(groupValidator.supports(GroupRequestDTO.class));
        assertFalse(groupValidator.supports(Object.class));
    }

    @Test
    void givenNewGroup_whenValidate_thenNoError() {
        when(groupRepository.findByName(GROUP_DTO.getName())).thenReturn(Optional.empty());
        Errors errors = new BeanPropertyBindingResult(new GroupResponseDTO(), "");

        groupValidator.validate(GROUP_DTO, errors);

        verify(groupRepository, times(INVOCATION_NUMBER)).findByName(GROUP_DTO.getName());
        assertNull(errors.getGlobalError());
    }

    @Test
    void givenNewGroup_whenValidate_thenError() {
        when(groupRepository.findByName(GROUP_DTO.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_GROUP));
        Errors errors = new BeanPropertyBindingResult(new GroupResponseDTO(), "");

        groupValidator.validate(GROUP_DTO, errors);

        verify(groupRepository, times(INVOCATION_NUMBER)).findByName(GROUP_DTO.getName());
        assertEquals(EXPECTED_ERRORS_COUNT, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getGlobalError()).getDefaultMessage());
    }

    @Test
    void givenUpdatedGroup_whenValidate_thenNoError() {
        when(groupRepository.findByName(UPDATED_GROUP_DTO.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_GROUP));
        when(groupRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(EXPECTED_GROUP));
        Errors errors = new BeanPropertyBindingResult(new GroupResponseDTO(), "");

        groupValidator.validate(UPDATED_GROUP_DTO, errors);

        verify(groupRepository, times(INVOCATION_NUMBER)).findByName(UPDATED_GROUP_DTO.getName());
        verify(groupRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
        assertNull(errors.getGlobalError());
    }

    @Test
    void givenUpdatedGroupWithOutLectures_whenValidate_thenError() {
        int expectedErrorsCount = 2;
        when(groupRepository.findByName(UPDATED_GROUP_DTO.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_GROUP));
        when(groupRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(EXPECTED_UPDATED_GROUP));
        Errors errors = new BeanPropertyBindingResult(new GroupResponseDTO(), "");

        groupValidator.validate(UPDATED_GROUP_DTO, errors);

        verify(groupRepository, times(INVOCATION_NUMBER)).findByName(UPDATED_GROUP_DTO.getName());
        verify(groupRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
        assertEquals(expectedErrorsCount, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getGlobalError()).getDefaultMessage());
        assertEquals(NOT_EMPTY_MESSAGE,
            Objects.requireNonNull(errors.getFieldError("lectures")).getDefaultMessage());
    }

    @Test
    void givenUpdatedGroupWithLectures_whenValidate_thenError() {
        when(groupRepository.findByName(UPDATED_GROUP_DTO_WITH_LECTURES.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_GROUP));
        when(groupRepository.findById(ACTUAL_ID))
            .thenReturn(Optional.ofNullable(EXPECTED_UPDATED_GROUP));
        Errors errors = new BeanPropertyBindingResult(new GroupResponseDTO(), "");

        groupValidator.validate(UPDATED_GROUP_DTO_WITH_LECTURES, errors);

        verify(groupRepository, times(INVOCATION_NUMBER)).findByName(UPDATED_GROUP_DTO_WITH_LECTURES.getName());
        verify(groupRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
        assertEquals(EXPECTED_ERRORS_COUNT, errors.getErrorCount());
        assertEquals(EXIST_MESSAGE, Objects.requireNonNull(errors.getGlobalError()).getDefaultMessage());
        assertNull(errors.getFieldError("lectures"));
    }

    @Test
    void givenUpdatedGroup_whenValidate_EntityNotFoundException() {
        when(groupRepository.findByName(UPDATED_GROUP_DTO.getName()))
            .thenReturn(Optional.ofNullable(EXPECTED_GROUP));
        when(groupRepository.findById(ACTUAL_ID))
            .thenThrow(new EntityNotFoundException());
        Errors errors = new BeanPropertyBindingResult(new GroupResponseDTO(), "");

        assertThrows(EntityNotFoundException.class,
            () -> groupValidator.validate(UPDATED_GROUP_DTO, errors));
    }
}
