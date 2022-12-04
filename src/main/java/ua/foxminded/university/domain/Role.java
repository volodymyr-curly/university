package ua.foxminded.university.domain;

public enum Role {

    ROLE_ADMIN("Admin"),
    ROLE_EMPLOYEE("Employee"),
    ROLE_TEACHER("Teacher"),
    ROLE_STUDENT("Student");

    private final String roleValue;

    Role(String roleValue) {
        this.roleValue = roleValue;
    }

    public String getRoleValue() {
        return roleValue;
    }
}
