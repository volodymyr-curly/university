package ua.foxminded.university.dto.lecture;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ua.foxminded.university.dto.duration.DurationNestedDTO;
import ua.foxminded.university.dto.group.GroupNestedDTO;
import ua.foxminded.university.dto.room.RoomNestedDTO;
import ua.foxminded.university.dto.subject.SubjectNestedDTO;
import ua.foxminded.university.dto.teacher.TeacherNestedDTO;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LectureRequestDTO {

    @Schema(example = "1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Schema(example = "2022-01-31")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Valid
    private SubjectNestedDTO subject;

    @Valid
    private DurationNestedDTO duration;

    @Valid
    private RoomNestedDTO room;

    @Valid
    @ToString.Exclude
    private List<GroupNestedDTO> groups;

    @Valid
    private TeacherNestedDTO teacher;
}
