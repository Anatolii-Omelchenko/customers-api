package tech.theraven.customers_api.model.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static tech.theraven.customers_api.constants.ValidationConstants.*;

@Data
public class CustomerUpdateDTO {

    @Size(min = FULL_NAME_MIN_LENGTH, max = FULL_NAME_MAX_LENGTH, message = FULL_NAME_LENGTH_MESSAGE)
    private String fullName;

    @Pattern(regexp = PHONE_REGEX, message = PHONE_FORMAT_MESSAGE)
    private String phone;
}
