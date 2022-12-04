package ua.foxminded.university.domain;

public enum Employment {

    FULL_TIME("Full time"),
    PART_TIME("Part time"),
    DISTANCE("Distance");

    private final String educationFormValue;

    Employment(String educationFormValue) {
        this.educationFormValue = educationFormValue;
    }

    public String getEducationFormValue() {
        return educationFormValue;
    }
}
