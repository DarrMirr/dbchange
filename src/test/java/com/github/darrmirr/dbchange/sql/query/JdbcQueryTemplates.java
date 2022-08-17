package com.github.darrmirr.dbchange.sql.query;

public final class JdbcQueryTemplates {
    public static final String DEPARTMENT_INSERT = "insert into department(id, name) values (:id, :name)";
    public static final String DEPARTMENT_DELETE_ONE = "delete from department where id = :id";

    public static final String OCCUPATION_INSERT = "insert into occupation(id, name) values (:id, :name)";
    public static final String OCCUPATION_DELETE_ONE = "delete from occupation where id = :id";

    public static final String EMPLOYEE_INSERT = "insert into employee(id, department_id, occupation_id, first_name, last_name) values (:id, :department_id, :occupation_id, :first_name, :last_name)";
    public static final String EMPLOYEE_DELETE_ONE = "delete from employee where id = :id";
}
