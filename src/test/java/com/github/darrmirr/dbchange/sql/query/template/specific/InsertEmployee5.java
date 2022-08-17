package com.github.darrmirr.dbchange.sql.query.template.specific;

import com.github.darrmirr.dbchange.sql.query.SpecificTemplateSqlQuery;
import com.github.darrmirr.dbchange.sql.query.TemplateSqlQuery;
import com.github.darrmirr.dbchange.sql.query.template.common.InsertEmployeeCommon;

import java.util.HashMap;
import java.util.Map;

public class InsertEmployee5 extends SpecificTemplateSqlQuery {

    @Override
    public TemplateSqlQuery commonTemplateSqlQuery() {
        return new InsertEmployeeCommon();
    }

    @Override
    public Map<String, Object> specificParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 5);
        params.put("department_id", 9);
        params.put("occupation_id", 3);
        return params;
    }
}
