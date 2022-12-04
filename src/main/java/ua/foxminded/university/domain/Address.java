package ua.foxminded.university.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

import static ua.foxminded.university.util.validators.ValidatorMessages.*;
import static ua.foxminded.university.util.validators.ValidatorPatterns.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Address {

    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne()
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

    @Column(name = "country")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Size(min = 2, max = 30, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String country;

    @Column(name = "region")
    @Size(min = 2, max = 30, message = NOT_EMPTY_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String region;

    @Column(name = "city")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Size(min = 2, max = 30, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String city;

    @Column(name = "street")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Size(min = 2, max = 30, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = STREET_PATTERN, message = STREET_PATTERN_MESSAGE)
    private String street;

    @Column(name = "apartment")
    @Size(min = 1, max = 5, message = APARTMENT_SIZE_MESSAGE)
    @Pattern(regexp = APARTMENT_PATTERN, message = APARTMENT_PATTERN_MESSAGE)
    private String apartment;

    @Column(name = "postcode")
    @NotBlank(message = NOT_EMPTY_MESSAGE)
    @Pattern(regexp = POSTCODE_PATTERN, message = POSTCODE_PATTERN_MESSAGE)
    private String postcode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id == address.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
