package com.github.darrmirr.dbchange.sql.query.template.insert;

import com.github.darrmirr.dbchange.sql.Id;
import com.github.darrmirr.dbchange.sql.query.InsertSqlQuery;
import com.github.darrmirr.dbchange.sql.query.template.DepartmentQuery;

import java.util.HashMap;
import java.util.Map;

public class InsertDepartment11 extends InsertSqlQuery {

    @Override
    public String tableName() {
        return "department";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put(DepartmentQuery.PARAM_ID, Id.DEP_11);
        params.put(DepartmentQuery.PARAM_NAME, "department for test insert sql query class");
        return params;
    }
}
