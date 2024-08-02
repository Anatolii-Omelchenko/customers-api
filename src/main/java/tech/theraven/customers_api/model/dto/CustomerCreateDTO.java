package tech.theraven.customers_api.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static tech.theraven.customers_api.constants.ValidationConstants.*;

@Data
public class CustomerCreateDTO {

    @Size(min = FULL_NAME_MIN_LENGTH, max = FULL_NAME_MAX_LENGTH, message = FULL_NAME_LENGTH_MESSAGE)
    private String fullName;

    @Size(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH, message = EMAIL_LENGTH_MESSAGE)
    @Email(message = EMAIL_FORMAT_MESSAGE)
    private String email;

    @Pattern(regexp = PHONE_REGEX, message = PHONE_FORMAT_MESSAGE)
    private String phone;
}
