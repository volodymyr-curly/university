package ua.foxminded.university.dto.lecture;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ua.foxminded.university.dto.group.GroupNestedDTO;
import ua.foxminded.university.dto.group.GroupRequestDTO;
import ua.foxminded.university.util.converters.CustomDeserializer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureResponseDTO {

    @Schema(example = "1")
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @JsonDeserialize(using = CustomDeserializer.class)
    private Integer id;

    @Schema(example = "Economics And Law")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String subjectName;

    @Schema(example = "2022-01-31")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(example = "09:00")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonDeserialize(using = CustomDeserializer.class)
    private String durationStartTime;

    @Schema(example = "101")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer lectureRoomNumber;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ToString.Exclude
    private List<GroupNestedDTO> groups;

    @Schema(example = "Ivanenko")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String teacherEmployeeLastName;

    @Schema(example = "Ivan")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String teacherEmployeeFirstName;
}
