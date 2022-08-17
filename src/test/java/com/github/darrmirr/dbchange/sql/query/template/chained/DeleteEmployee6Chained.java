package com.github.darrmirr.dbchange.sql.query.template.chained;

import com.github.darrmirr.dbchange.sql.Id;
import com.github.darrmirr.dbchange.sql.query.ChainedSqlQuery;
import com.github.darrmirr.dbchange.sql.query.JdbcQueryTemplates;
import com.github.darrmirr.dbchange.sql.query.SqlQuery;

public class DeleteEmployee6Chained implements SqlQuery, ChainedSqlQuery {

    @Override
    public String get() {
        return JdbcQueryTemplates.EMPLOYEE_DELETE_ONE.replace(":id", Id.EMP_6.toString());
    }

    @Override
    public SqlQuery next() {
        return new DeleteOccupation3();
    }

    public static class DeleteOccupation3 implements SqlQuery, ChainedSqlQuery {

        @Override
        public String get() {
            return JdbcQueryTemplates.OCCUPATION_DELETE_ONE.replace(":id", Id.OCC_4.toString());
        }

        @Override
        public SqlQuery next() {
            return () -> JdbcQueryTemplates.DEPARTMENT_DELETE_ONE.replace(":id", Id.DEP_10.toString());
        }
    }
}
