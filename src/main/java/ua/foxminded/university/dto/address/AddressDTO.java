package ua.foxminded.university.dto.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static ua.foxminded.university.util.validators.ValidatorMessages.*;
import static ua.foxminded.university.util.validators.ValidatorPatterns.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    @Schema(example = "Ukraine")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Size(min = 2, max = 30, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String country;

    @Schema(example = "'Kyiv")
    @Size(min = 2, max = 30, message = NOT_EMPTY_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String region;

    @Schema(example = "'Kyiv")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Size(min = 2, max = 30, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String city;

    @Schema(example = "Kyivska 1")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Size(min = 2, max = 30, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = STREET_PATTERN, message = STREET_PATTERN_MESSAGE)
    private String street;

    @Schema(example = "1a")
    @Size(min = 1, max = 5, message = APARTMENT_SIZE_MESSAGE)
    @Pattern(regexp = APARTMENT_PATTERN, message = APARTMENT_PATTERN_MESSAGE)
    private String apartment;

    @Schema(example = "01000")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Pattern(regexp = POSTCODE_PATTERN, message = POSTCODE_PATTERN_MESSAGE)
    private String postcode;
}
