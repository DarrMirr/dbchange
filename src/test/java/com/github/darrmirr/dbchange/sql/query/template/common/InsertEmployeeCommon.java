package com.github.darrmirr.dbchange.sql.query.template.common;

import com.github.darrmirr.dbchange.sql.query.JdbcQueryTemplates;
import com.github.darrmirr.dbchange.sql.query.TemplateSqlQuery;

import java.util.HashMap;
import java.util.Map;

public class InsertEmployeeCommon extends TemplateSqlQuery {

    @Override
    public String queryTemplate() {
        return JdbcQueryTemplates.EMPLOYEE_INSERT;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", null);
        params.put("department_id", null);
        params.put("occupation_id", null);
        params.put("first_name", "default_employee_first_name");
        params.put("last_name", "default_employee_last_name");
        return params;
    }
}
