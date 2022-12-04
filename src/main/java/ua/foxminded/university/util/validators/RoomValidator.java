package ua.foxminded.university.util.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.university.domain.LectureRoom;
import ua.foxminded.university.dto.room.RoomRequestDTO;
import ua.foxminded.university.repository.LectureRoomRepository;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static ua.foxminded.university.util.exceptions.ExceptionConstants.ENTITY_NOT_FOUND;
import static ua.foxminded.university.util.validators.ValidatorMessages.EXISTS_MESSAGE;

@Component
public class RoomValidator implements Validator {

    private static final String MESSAGE = format(ENTITY_NOT_FOUND, LectureRoom.class.getSimpleName());

    private final LectureRoomRepository roomRepository;

    @Autowired
    public RoomValidator(LectureRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return RoomRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        RoomRequestDTO updatedRoom = (RoomRequestDTO) target;
        Optional<LectureRoom> existedRoom = roomRepository.findByNumber(updatedRoom.getNumber());

        if (existedRoom.isPresent()) {

            if (Objects.isNull(updatedRoom.getId())) {
                errors.reject("global", format(EXISTS_MESSAGE, LectureRoom.class.getSimpleName()));

            } else {
                LectureRoom roomToUpdate = roomRepository.findById(updatedRoom.getId())
                    .orElseThrow(() -> new EntityNotFoundException(MESSAGE));
                Integer existedNumber = roomToUpdate.getNumber();

                if (!existedNumber.equals(updatedRoom.getNumber())) {
                    errors.reject("global", format(EXISTS_MESSAGE, LectureRoom.class.getSimpleName()));
                }
            }
        }
    }
}
