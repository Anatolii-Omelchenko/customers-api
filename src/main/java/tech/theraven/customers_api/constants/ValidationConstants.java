package tech.theraven.customers_api.constants;

public class ValidationConstants {

    // Full name constraints
    public static final int FULL_NAME_MIN_LENGTH = 2;
    public static final int FULL_NAME_MAX_LENGTH = 50;

    // Email constraints
    public static final int EMAIL_MIN_LENGTH = 2;
    public static final int EMAIL_MAX_LENGTH = 100;

    // Phone constraints
    public static final String PHONE_REGEX = "^\\+\\d{6,14}$";

    // Validation messages
    public static final String FULL_NAME_LENGTH_MESSAGE = "Full name must be between " + FULL_NAME_MIN_LENGTH + " and " + FULL_NAME_MAX_LENGTH + " characters long";
    public static final String EMAIL_LENGTH_MESSAGE = "Email must be between " + EMAIL_MIN_LENGTH + " and " + EMAIL_MAX_LENGTH + " characters long";
    public static final String EMAIL_FORMAT_MESSAGE = "Email should be valid and contain exactly one '@'";
    public static final String PHONE_FORMAT_MESSAGE = "Phone number must start with '+' and be between 6 and 14 digits long";
}
