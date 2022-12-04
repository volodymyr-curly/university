package ua.foxminded.university.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.university.repository.LectureRoomRepository;
import ua.foxminded.university.domain.LectureRoom;
import ua.foxminded.university.service.interfaces.LectureRoomService;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class LectureRoomServiceImpl implements LectureRoomService {

    private static final String MESSAGE = format(ENTITY_NOT_FOUND, LectureRoom.class.getSimpleName());

    private final LectureRoomRepository roomRepository;

    @Autowired
    public LectureRoomServiceImpl(LectureRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional
    public void add(LectureRoom room) {
        roomRepository.save(room);
    }

    @Override
    @Transactional
    public void update(int id, LectureRoom updatedRoom) {
        ifNotFoundThenException(id);
        roomRepository.save(updatedRoom);
    }

    @Override
    @Transactional
    public void delete(int id) {
        ifNotFoundThenException(id);
        roomRepository.deleteById(id);
    }

    @Override
    public LectureRoom find(int id) {
        return roomRepository.findById(id).orElseThrow(() -> {
            log.error("Failed to find room with id={}", id);
            return new EntityNotFoundException(MESSAGE);
        });
    }

    @Override
    public List<LectureRoom> findAll() {
        List<LectureRoom> rooms = roomRepository.findAll();

        if (rooms.isEmpty()) {
            log.error("Fail when searching for rooms list");
            throw new EntityNotFoundException(NOT_FOUND);
        }

        return rooms;
    }

    private void ifNotFoundThenException(int id) {
        if (!roomRepository.existsById(id)) {
            log.error("LectureRoom with id={} not found", id);
            throw new EntityNotFoundException(MESSAGE);
        }
    }
}
