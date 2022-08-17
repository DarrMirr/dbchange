package com.github.darrmirr.dbchange.sql.query.template.specific;

import com.github.darrmirr.dbchange.sql.Id;
import com.github.darrmirr.dbchange.sql.query.JdbcQueryTemplates;
import com.github.darrmirr.dbchange.sql.query.SqlQuery;

public class DeleteOccupation3 implements SqlQuery {

    @Override
    public String get() {
        return JdbcQueryTemplates.OCCUPATION_DELETE_ONE.replace(":id", Id.OCC_3.toString());
    }
}
