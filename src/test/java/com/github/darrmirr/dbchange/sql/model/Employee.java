package com.github.darrmirr.dbchange.sql.model;

public class Employee {
    private final Integer id;
    private final Integer departmentId;
    private final Integer occupationId;
    private final String firstName;
    private final String lastName;

    public Employee(Integer id, Integer departmentId, Integer occupationId, String firstName, String lastName) {
        this.id = id;
        this.departmentId = departmentId;
        this.occupationId = occupationId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public Integer getOccupationId() {
        return occupationId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", departmentId=" + departmentId +
                ", occupationId=" + occupationId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
