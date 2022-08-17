package com.github.darrmirr.dbchange.sql.query.template.specific;

import com.github.darrmirr.dbchange.sql.Id;
import com.github.darrmirr.dbchange.sql.query.JdbcQueryTemplates;
import com.github.darrmirr.dbchange.sql.query.SqlQuery;

public class DeleteDepartment9 implements SqlQuery {

    @Override
    public String get() {
        return JdbcQueryTemplates.DEPARTMENT_DELETE_ONE.replace(":id", Id.DEP_9.toString());
    }
}
