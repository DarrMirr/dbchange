package com.github.darrmirr.dbchange.sql.query.template.chained;

import com.github.darrmirr.dbchange.sql.Id;
import com.github.darrmirr.dbchange.sql.query.ChainedSqlQuery;
import com.github.darrmirr.dbchange.sql.query.JdbcQueryTemplates;
import com.github.darrmirr.dbchange.sql.query.SqlQuery;

public class DeleteEmployee9Chained implements SqlQuery, ChainedSqlQuery {

    @Override
    public String get() {
        return JdbcQueryTemplates.EMPLOYEE_DELETE_ONE.replace(":id", Id.EMP_9.toString());
    }

    @Override
    public SqlQuery next() {
        return new DeleteOccupation7();
    }

    public static class DeleteOccupation7 implements SqlQuery, ChainedSqlQuery {

        @Override
        public String get() {
            return JdbcQueryTemplates.OCCUPATION_DELETE_ONE.replace(":id", Id.OCC_7.toString());
        }

        @Override
        public SqlQuery next() {
            return () -> JdbcQueryTemplates.DEPARTMENT_DELETE_ONE.replace(":id", Id.DEP_13.toString());
        }
    }
}
