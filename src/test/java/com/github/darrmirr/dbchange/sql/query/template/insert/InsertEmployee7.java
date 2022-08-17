package com.github.darrmirr.dbchange.sql.query.template.insert;

import com.github.darrmirr.dbchange.sql.Id;
import com.github.darrmirr.dbchange.sql.query.InsertSqlQuery;

import java.util.HashMap;
import java.util.Map;

public class InsertEmployee7 extends InsertSqlQuery {

    @Override
    public String tableName() {
        return "employee";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", Id.EMP_7);
        params.put("department_id", Id.DEP_11);
        params.put("occupation_id", Id.OCC_5);
        return params;
    }
}
