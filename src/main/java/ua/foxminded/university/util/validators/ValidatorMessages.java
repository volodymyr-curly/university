package ua.foxminded.university.util.validators;

public final class ValidatorMessages {

    public static final String NOT_EMPTY_MESSAGE = "Should not be empty";
    public static final String NAME_SIZE_MESSAGE = "Should be between 2 and 30 characters";
    public static final String NAME_PATTERN_MESSAGE = "Should be in this format: Abcdefg, Abcdefg Hijklmn, etc.";
    public static final String GROUP_NAME_PATTERN_MESSAGE = "Should be in this format: Aa_aa-11, etc.";
    public static final String STREET_PATTERN_MESSAGE = "Should be in this format: Abcdefg 1, Abcdefg Hijklmn 1, etc.";
    public static final String APARTMENT_SIZE_MESSAGE = "Should be between 1 and 5 characters";
    public static final String APARTMENT_PATTERN_MESSAGE = "Should be in this format: 11, 1A, 1a, etc.";
    public static final String POSTCODE_PATTERN_MESSAGE = "Should be in this format: 55555";
    public static final String BIRTH_DATE_MESSAGE = "Should be in the past";
    public static final String EMAIL_PATTERN_MESSAGE = "Should be in this format: username@domain.com";
    public static final String EMAIL_EXISTS_MESSAGE = "This email is already taken";
    public static final String PHONE_NUMBER_PATTERN_MESSAGE = "Should be in this format: (050)5555555";
    public static final String NO_TEACHERS_MESSAGE = "Should first add an employee who isn't a teacher";
    public static final String EXISTS_MESSAGE = "%s is already exist";
}
