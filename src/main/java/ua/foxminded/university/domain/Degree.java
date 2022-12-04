package ua.foxminded.university.domain;

public enum Degree {

    ASSOCIATE("Associate"),
    BACHELOR("Bachelor"),
    MASTER("Master"),
    DOCTOR("Doctor");

    private final String degreeValue;

    Degree(String degreeValue) {
        this.degreeValue = degreeValue;
    }

    public String getDegreeValue() {
        return degreeValue;
    }
}
