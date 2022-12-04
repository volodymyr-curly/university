package ua.foxminded.university.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorModel {

    private String fieldName;
    private Object rejectedValue;
    private String messageError;
}
