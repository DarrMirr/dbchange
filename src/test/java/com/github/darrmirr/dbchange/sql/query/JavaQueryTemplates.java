package com.github.darrmirr.dbchange.sql.query;

public final class JavaQueryTemplates {
    public static final String DEPARTMENT_INSERT = "insert into department(id, name) values (%s, 'dep%s');";
    public static final String DEPARTMENT_DELETE_TWO = "delete from department where id in (%s, %s);";
}
