package com.github.darrmirr.dbchange.sql.query.template.specific;

import com.github.darrmirr.dbchange.sql.Id;
import com.github.darrmirr.dbchange.sql.query.JdbcQueryTemplates;
import com.github.darrmirr.dbchange.sql.query.SqlQuery;

public class DeleteEmployee7 implements SqlQuery {
    @Override
    public String get() {
        return JdbcQueryTemplates.EMPLOYEE_DELETE_ONE.replace(":id", Id.EMP_7.toString());
    }
}
