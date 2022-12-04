package ua.foxminded.university.domain;

public enum JobTitle {

    LABORATORY_ASSISTANT("Laboratory assistant"),
    SENIOR_LABORATORY_ASSISTANT("Senior laboratory assistant"),
    ASSISTANT("Assistant"),
    TEACHER("Teacher"),
    SENIOR_TEACHER("Senior teacher"),
    DOCENT("Docent"),
    PROFESSOR("Professor"),
    DEPARTMENT_HEAD("Department head"),
    DEAN("Dean"),
    VICE_RECTOR("Vice-rector"),
    RECTOR("Rector"),
    OTHER("Other");

    private final String jobTitleValue;

    JobTitle(String jobTitleValue) {
        this.jobTitleValue = jobTitleValue;
    }

    public String getJobTitleValue() {
        return jobTitleValue;
    }
}

