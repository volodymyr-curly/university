package ua.foxminded.university.domain;

public enum Gender {

    MALE("Male"),
    FEMALE("Female");

    private final String genderValue;

    Gender(String genderValue) {
        this.genderValue = genderValue;
    }

    public String getGenderValue() {
        return genderValue;
    }
}
