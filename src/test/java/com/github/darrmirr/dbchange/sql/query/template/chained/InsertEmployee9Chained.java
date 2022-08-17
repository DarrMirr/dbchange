package com.github.darrmirr.dbchange.sql.query.template.chained;

import com.github.darrmirr.dbchange.sql.Id;
import com.github.darrmirr.dbchange.sql.query.ChainedSqlQuery;
import com.github.darrmirr.dbchange.sql.query.SpecificTemplateSqlQuery;
import com.github.darrmirr.dbchange.sql.query.SqlQuery;
import com.github.darrmirr.dbchange.sql.query.TemplateSqlQuery;
import com.github.darrmirr.dbchange.sql.query.template.DepartmentQuery;
import com.github.darrmirr.dbchange.sql.query.template.common.InsertDepartmentCommon;
import com.github.darrmirr.dbchange.sql.query.template.common.InsertEmployeeCommon;
import com.github.darrmirr.dbchange.sql.query.template.common.InsertOccupationCommon;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InsertEmployee9Chained extends SpecificTemplateSqlQuery implements ChainedSqlQuery {

    @Override
    public TemplateSqlQuery commonTemplateSqlQuery() {
        return new InsertDepartmentCommon();
    }

    @Override
    public Map<String, Object> specificParameters() {
        return Collections.singletonMap(DepartmentQuery.PARAM_ID, Id.DEP_13);
    }

    @Override
    public SqlQuery next() {
        return new InsertOccupation7();
    }

    public static class InsertOccupation7 extends SpecificTemplateSqlQuery implements ChainedSqlQuery  {

        @Override
        public TemplateSqlQuery commonTemplateSqlQuery() {
            return new InsertOccupationCommon();
        }

        @Override
        public Map<String, Object> specificParameters() {
            return Collections.singletonMap("id", Id.OCC_7);
        }

        @Override
        public SqlQuery next() {
            return new InsertEmployee9();
        }
    }

    public static class InsertEmployee9 extends SpecificTemplateSqlQuery {
        @Override
        public TemplateSqlQuery commonTemplateSqlQuery() {
            return new InsertEmployeeCommon();
        }

        @Override
        public Map<String, Object> specificParameters() {
            Map<String, Object> params = new HashMap<>();
            params.put("id", Id.EMP_9);
            params.put("department_id", Id.DEP_13);
            params.put("occupation_id", Id.OCC_7);
            return params;
        }
    }
}
