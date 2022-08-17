package com.github.darrmirr.dbchange.sql.query.template.common;

import com.github.darrmirr.dbchange.sql.query.JdbcQueryTemplates;
import com.github.darrmirr.dbchange.sql.query.TemplateSqlQuery;
import com.github.darrmirr.dbchange.sql.query.template.DepartmentQuery;

import java.util.HashMap;
import java.util.Map;

public class InsertDepartmentCommon extends TemplateSqlQuery {

    @Override
    public String queryTemplate() {
        return JdbcQueryTemplates.DEPARTMENT_INSERT;
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put(DepartmentQuery.PARAM_ID, 0);
        params.put(DepartmentQuery.PARAM_NAME, "default_department_name");
        return params;
    }
}
