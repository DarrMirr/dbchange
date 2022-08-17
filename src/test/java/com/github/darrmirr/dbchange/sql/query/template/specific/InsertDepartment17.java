package com.github.darrmirr.dbchange.sql.query.template.specific;

import com.github.darrmirr.dbchange.sql.Id;
import com.github.darrmirr.dbchange.sql.query.SpecificTemplateSqlQuery;
import com.github.darrmirr.dbchange.sql.query.TemplateSqlQuery;
import com.github.darrmirr.dbchange.sql.query.template.DepartmentQuery;
import com.github.darrmirr.dbchange.sql.query.template.common.InsertDepartmentCommon;

import java.util.Collections;
import java.util.Map;

public class InsertDepartment17 extends SpecificTemplateSqlQuery {

    @Override
    public TemplateSqlQuery commonTemplateSqlQuery() {
        return new InsertDepartmentCommon();
    }

    @Override
    public Map<String, Object> specificParameters() {
        return Collections.singletonMap(DepartmentQuery.PARAM_ID, Id.DEP_17);
    }
}
