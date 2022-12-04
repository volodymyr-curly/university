package ua.foxminded.university.util.validators;

public final class ValidatorPatterns {

    public static final String NAME_PATTERN = "([A-Z][a-z]+\\s*)+";
    public static final String GROUP_NAME_PATTERN = "[A-Z][a-z]*\\_[a-z]+\\-[0-9]+";
    public static final String STREET_PATTERN = "[A-Z][a-z]+\\s+[0-9]*$";
    public static final String APARTMENT_PATTERN = "\\d+[A-z]?$";
    public static final String POSTCODE_PATTERN = "^\\d{5}$";
    public static final String PHONE_NUMBER_PATTERN = "^(\\(\\d{3}\\))\\d{7}$";

}
