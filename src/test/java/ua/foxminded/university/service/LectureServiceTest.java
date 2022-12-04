package ua.foxminded.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.foxminded.university.domain.Lecture;
import ua.foxminded.university.repository.LectureRepository;
import ua.foxminded.university.service.interfaces.LectureService;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ua.foxminded.university.data.EntityData.setExpectedLectures;

@ExtendWith(SpringExtension.class)
@Import(LectureServiceImpl.class)
class LectureServiceTest {

    private static final int ACTUAL_ID = 1;
    private static final int INVOCATION_NUMBER = 1;
    private static final String MESSAGE = "ERROR";
    private static final Lecture EXPECTED_LECTURE = setExpectedLectures().get(0);

    @Autowired
    private LectureService lectureService;

    @MockBean
    private LectureRepository lectureRepository;

    @Test
    void givenLecture_whenAddLecture_thenLecture() {
        when(lectureRepository.existsSameLecture(any(LocalDate.class), anyInt(), anyInt(), anyInt(), anyInt()))
            .thenReturn(false);
        when(lectureRepository.save(any(Lecture.class))).thenReturn(EXPECTED_LECTURE);
        EXPECTED_LECTURE.setId(0);

        lectureService.add(EXPECTED_LECTURE);

        verify(lectureRepository, times(INVOCATION_NUMBER))
            .existsSameLecture(any(LocalDate.class), anyInt(), anyInt(), anyInt(), anyInt());
        verify(lectureRepository, times(INVOCATION_NUMBER)).save(any(Lecture.class));
    }

    @Test
    void givenLecture_whenUpdateLecture_thenUpdatedLecture() {
        when(lectureRepository.existsById(ACTUAL_ID)).thenReturn(true);
        when(lectureRepository.existsSameLecture(any(LocalDate.class), anyInt(), anyInt(), anyInt(), anyInt()))
            .thenReturn(false);
        when(lectureRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(setExpectedLectures().get(1)));
        when(lectureRepository.save(any(Lecture.class))).thenReturn(EXPECTED_LECTURE);

        lectureService.update(ACTUAL_ID, EXPECTED_LECTURE);

        verify(lectureRepository, times(INVOCATION_NUMBER)).existsById(ACTUAL_ID);
        verify(lectureRepository, times(INVOCATION_NUMBER))
            .existsSameLecture(any(LocalDate.class), anyInt(), anyInt(), anyInt(), anyInt());
        verify(lectureRepository, times(INVOCATION_NUMBER)).save(any(Lecture.class));
    }

    @Test
    void givenLecture_whenDeleteLecture_thenNoLecture() {
        doNothing().when(lectureRepository).deleteById(ACTUAL_ID);
        when(lectureRepository.existsById(ACTUAL_ID)).thenReturn(true);

        lectureService.delete(ACTUAL_ID);

        verify(lectureRepository, times(INVOCATION_NUMBER)).existsById(ACTUAL_ID);
        verify(lectureRepository, times(INVOCATION_NUMBER)).deleteById(ACTUAL_ID);
    }

    @Test
    void givenLectureId_whenFindLecture_thenLecture() {
        when(lectureRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(EXPECTED_LECTURE));

        Lecture actualLecture = lectureService.find(ACTUAL_ID);

        assertEquals(EXPECTED_LECTURE, actualLecture);
        verify(lectureRepository, times(INVOCATION_NUMBER)).findById(ACTUAL_ID);
    }

    @Test
    void whenFindLectures_thenListOfLectures() {
        when(lectureRepository.findAll()).thenReturn(setExpectedLectures());

        List<Lecture> actualLectures = lectureService.findAll();

        assertEquals(setExpectedLectures(), actualLectures);
        verify(lectureRepository, times(INVOCATION_NUMBER)).findAll();
    }

    @Test
    void givenSubjectId_whenFindLecturesBySubject_thenListOfLectures() {
        when(lectureRepository.findBySubjectId(ACTUAL_ID)).thenReturn(setExpectedLectures());

        List<Lecture> actualLectures = lectureService.findBySubject(ACTUAL_ID);

        assertEquals(setExpectedLectures(), actualLectures);
        verify(lectureRepository, times(INVOCATION_NUMBER)).findBySubjectId(ACTUAL_ID);
    }

    @Test
    void givenTeacherId_whenFindLecturesByTeacher_thenListOfLectures() {
        when(lectureRepository.findByTeacherId(ACTUAL_ID)).thenReturn(setExpectedLectures());

        List<Lecture> actualLectures = lectureService.findByTeacher(ACTUAL_ID);

        assertEquals(setExpectedLectures(), actualLectures);
        verify(lectureRepository, times(INVOCATION_NUMBER)).findByTeacherId(ACTUAL_ID);
    }

    @Test
    void givenGroupId_whenFindLecturesByGroup_thenListOfLectures() {
        when(lectureRepository.findByGroupsId(ACTUAL_ID)).thenReturn(setExpectedLectures());

        List<Lecture> actualLectures = lectureService.findByGroup(ACTUAL_ID);

        assertEquals(setExpectedLectures(), actualLectures);
        verify(lectureRepository, times(INVOCATION_NUMBER)).findByGroupsId(ACTUAL_ID);
    }

    @Test
    void givenRoomNumber_whenFindLecturesByRoom_thenListOfLectures() {
        when(lectureRepository.findByRoomId(ACTUAL_ID)).thenReturn(setExpectedLectures());

        List<Lecture> actualLectures = lectureService.findByRoom(ACTUAL_ID);

        assertEquals(setExpectedLectures(), actualLectures);
        verify(lectureRepository, times(INVOCATION_NUMBER)).findByRoomId(ACTUAL_ID);
    }

    @Test
    void givenDurationId_whenFindLecturesByDuration_thenListOfLectures() {
        when(lectureRepository.findByDurationId(ACTUAL_ID)).thenReturn(setExpectedLectures());

        List<Lecture> actualLectures = lectureService.findByDuration(ACTUAL_ID);

        assertEquals(setExpectedLectures(), actualLectures);
        verify(lectureRepository, times(INVOCATION_NUMBER)).findByDurationId(ACTUAL_ID);
    }

    @Test
    void givenLecture_whenAddLecture_thenEntityExistsException() {
        EXPECTED_LECTURE.setId(0);
        when(lectureRepository.existsSameLecture(any(LocalDate.class), anyInt(), anyInt(), anyInt(), anyInt()))
            .thenReturn(true);

        assertThrows(EntityExistsException.class, () -> lectureService.add(EXPECTED_LECTURE));
    }

    @Test
    void givenLecture_whenUpdateLecture_thenEntityNotFoundException() {
        when(lectureRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> lectureService.update(ACTUAL_ID, EXPECTED_LECTURE));
    }

    @Test
    void givenLecture_whenUpdateLecture_thenEntityExistsException() {
        when(lectureRepository.existsById(ACTUAL_ID)).thenReturn(true);
        when(lectureRepository.existsSameLecture(any(LocalDate.class), anyInt(), anyInt(), anyInt(), anyInt()))
            .thenReturn(true);
        when(lectureRepository.findById(ACTUAL_ID)).thenReturn(Optional.ofNullable(setExpectedLectures().get(1)));

        assertThrows(EntityExistsException.class, () -> lectureService.update(ACTUAL_ID, EXPECTED_LECTURE));
    }

    @Test
    void givenLecture_whenDeleteLecture_thenEntityNotFoundException() {
        when(lectureRepository.existsById(ACTUAL_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> lectureService.delete(ACTUAL_ID));
    }

    @Test
    void givenLectureId_whenFindLecture_thenEntityNotFoundException() {
        when(lectureRepository.findById(ACTUAL_ID)).thenThrow(new EntityNotFoundException(MESSAGE) {
        });

        assertThrows(EntityNotFoundException.class, () -> lectureService.find(ACTUAL_ID));
    }

    @Test
    void whenFindLectures_thenEntityNotFoundException() {
        when(lectureRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> lectureService.findAll());
    }

    @Test
    void givenSubjectId_whenFindLecturesById_thenEntityNotFoundException() {
        when(lectureRepository.findBySubjectId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> lectureService.findBySubject(ACTUAL_ID));
    }

    @Test
    void givenTeacherId_whenFindLecturesById_thenEntityNotFoundException() {
        when(lectureRepository.findByTeacherId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> lectureService.findByTeacher(ACTUAL_ID));
    }

    @Test
    void givenGroupId_whenFindLecturesById_thenEntityNotFoundException() {
        when(lectureRepository.findByGroupsId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> lectureService.findByGroup(ACTUAL_ID));
    }

    @Test
    void givenRoomId_whenFindLecturesByNumber_thenEntityNotFoundException() {
        when(lectureRepository.findByRoomId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> lectureService.findByRoom(ACTUAL_ID));
    }

    @Test
    void givenDurationId_whenFindLecturesById_thenEntityNotFoundException() {
        when(lectureRepository.findByDurationId(ACTUAL_ID)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> lectureService.findByDuration(ACTUAL_ID));
    }
}
