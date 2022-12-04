package ua.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.domain.Group;
import ua.foxminded.university.repository.GroupRepository;
import ua.foxminded.university.service.interfaces.GroupService;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.*;

@ExtendWith(SpringExtension.class)
@Import(GroupServiceImpl.class)
class GroupServiceTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final String MESSAGE = "ERROR";
    private static final Group EXPECTED_GROUP = setExpectedGroups().get(0);

    @Autowired
    private GroupService groupService;

    @MockBean
    private GroupRepository groupRepository;

    @Test
    void givenGroup_whenAddGroup_thenGroup() {
        when(groupRepository.save(any(Group.class))).thenReturn(EXPECTED_GROUP);

        groupService.add(EXPECTED_GROUP);

        verify(groupRepository, times(INVOCATION_NUMBER)).save(any(Group.class));
    }

    @Test
    void givenGroup_whenUpdateGroup_thenUpdatedGroup() {
        when(groupRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_GROUP));
        when(groupRepository.save(any(Group.class))).thenReturn(EXPECTED_GROUP);

        groupService.update(ACTUAL_ID, EXPECTED_GROUP);

        verify(groupRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
        verify(groupRepository, times(INVOCATION_NUMBER)).save(any(Group.class));

    }

    @Test
    void givenGroup_whenDeleteGroup_thenNoGroup() {
        doNothing().when(groupRepository).deleteById(ACTUAL_ID);
        when(groupRepository.existsById(ACTUAL_ID)).thenReturn(true);

        groupService.delete(ACTUAL_ID);

        verify(groupRepository, times(INVOCATION_NUMBER)).deleteById(ACTUAL_ID);
    }

    @Test
    void givenGroupId_whenFindGroup_thenGroup() {
        when(groupRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_GROUP));

        Group actualGroup = groupService.find(ACTUAL_ID);

        assertEquals(EXPECTED_GROUP, actualGroup);
        verify(groupRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
    }

    @Test
    void whenFindGroups_thenListOfGroups() {
        when(groupRepository.findAll()).thenReturn(setExpectedGroups());

        List<Group> actualGroups = groupService.findAll();

        assertEquals(setExpectedGroups(), actualGroups);
        verify(groupRepository, times(INVOCATION_NUMBER)).findAll();
    }

    @Test
    void givenDepartmentId_whenFindGroupsByDepartment_thenListOfGroups() {
        when(groupRepository.findByDepartmentId(ACTUAL_ID)).thenReturn(setExpectedGroups());

        List<Group> actualGroups = groupService.findByDepartment(ACTUAL_ID);

        assertEquals(setExpectedGroups(), actualGroups);
        verify(groupRepository, times(INVOCATION_NUMBER)).findByDepartmentId(ACTUAL_ID);
    }

    @Test
    void givenSubjectId_whenFindGroupsBySubject_thenListOfGroups() {
        when(groupRepository.findBySubjectsId(ACTUAL_ID)).thenReturn(setExpectedGroups());

        List<Group> actualGroups = groupService.findBySubject(ACTUAL_ID);

        assertEquals(setExpectedGroups(), actualGroups);
        verify(groupRepository, times(INVOCATION_NUMBER)).findBySubjectsId(ACTUAL_ID);
    }

    @Test
    void givenLectureId_whenFindGroupsByLecture_thenListOfGroups() {
        when(groupRepository.findByLecturesId(ACTUAL_ID)).thenReturn(setExpectedGroups());

        List<Group> actualGroups = groupService.findByLecture(ACTUAL_ID);

        assertEquals(setExpectedGroups(), actualGroups);
        verify(groupRepository, times(INVOCATION_NUMBER)).findByLecturesId(ACTUAL_ID);
    }

    @Test
    void givenGroup_whenUpdateGroup_thenEntityNotFoundException() {
        when(groupRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE) {
        });

        assertThrows(EntityNotFoundException.class, () -> groupService.update(ACTUAL_ID, EXPECTED_GROUP));
    }

    @Test
    void givenGroup_whenDeleteGroup_thenEntityNotFoundException() {
        when(groupRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> groupService.delete(ACTUAL_ID));
    }

    @Test
    void givenGroupId_whenFindGroup_thenEntityNotFoundException() {
        when(groupRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE) {
        });

        assertThrows(EntityNotFoundException.class, () -> groupService.find(ACTUAL_ID));
    }

    @Test
    void whenFindGroups_thenEntityNotFoundException() {
        when(groupRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> groupService.findAll());
    }

    @Test
    void givenDepartmentId_whenFindGroupsById_thenEntityNotFoundException() {
        when(groupRepository.findByDepartmentId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> groupService.findByDepartment(ACTUAL_ID));
    }

    @Test
    void givenSubjectId_whenFindGroupsById_thenEntityNotFoundException() {
        when(groupRepository.findBySubjectsId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> groupService.findBySubject(ACTUAL_ID));
    }
}
