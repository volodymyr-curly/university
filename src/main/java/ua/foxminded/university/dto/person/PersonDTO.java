package ua.foxminded.university.dto.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import ua.foxminded.university.domain.Gender;
import ua.foxminded.university.domain.Role;
import ua.foxminded.university.dto.address.AddressDTO;
import ua.foxminded.university.util.validators.interfaces.Password;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

import static ua.foxminded.university.util.validators.ValidatorMessages.*;
import static ua.foxminded.university.util.validators.ValidatorMessages.NOT_EMPTY_MESSAGE;
import static ua.foxminded.university.util.validators.ValidatorPatterns.NAME_PATTERN;
import static ua.foxminded.university.util.validators.ValidatorPatterns.PHONE_NUMBER_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class PersonDTO {

    @Schema(example = "1")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;

    @Schema(example = "Ivan")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Size(min = 2, max = 30, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String firstName;

    @Schema(example = "Ivanenko")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Size(min = 2, max = 30, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String lastName;

    @Schema(enumAsRef = true)
    @NotNull(message = NOT_EMPTY_MESSAGE)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Schema(example = "2022-01-31")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = BIRTH_DATE_MESSAGE)
    @NotNull(message = NOT_EMPTY_MESSAGE)
    private LocalDate birthDate;

    @Valid
    @ToString.Exclude
    private AddressDTO address;

    @Schema(example = "person@mail.com")
    @Email(message = EMAIL_PATTERN_MESSAGE)
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    private String email;

    @Schema(example = "(012)3456789")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Pattern(regexp = PHONE_NUMBER_PATTERN, message = PHONE_NUMBER_PATTERN_MESSAGE)
    private String phoneNumber;

    @Password
    @Schema(example = "password")
    private String password;

    @Schema(enumAsRef = true)
    @Enumerated(EnumType.STRING)
    private Role role;
}
