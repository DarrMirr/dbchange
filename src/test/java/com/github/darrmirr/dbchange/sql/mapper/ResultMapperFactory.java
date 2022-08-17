package com.github.darrmirr.dbchange.sql.mapper;

import com.github.darrmirr.dbchange.sql.model.Department;
import com.github.darrmirr.dbchange.sql.model.Employee;
import com.github.darrmirr.dbchange.sql.model.Occupation;

import java.sql.SQLException;

public final class ResultMapperFactory {

    private ResultMapperFactory() {
    }

    public static ResultMapper<Department> department() {
        return resultSet -> {
            try {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                return new Department(id, name);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        };
    }

    public static ResultMapper<Occupation> occupation() {
        return resultSet -> {
            try {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                return new Occupation(id, name);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        };
    }

    public static ResultMapper<Employee> employee() {
        return resultSet -> {
            try {
                int id = resultSet.getInt("id");
                int depId = resultSet.getInt("department_id");
                int occId = resultSet.getInt("occupation_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                return new Employee(id, depId, occId, firstName, lastName);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        };
    }
}
