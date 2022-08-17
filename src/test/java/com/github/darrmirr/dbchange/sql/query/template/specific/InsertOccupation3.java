package com.github.darrmirr.dbchange.sql.query.template.specific;

import com.github.darrmirr.dbchange.sql.Id;
import com.github.darrmirr.dbchange.sql.query.SpecificTemplateSqlQuery;
import com.github.darrmirr.dbchange.sql.query.TemplateSqlQuery;
import com.github.darrmirr.dbchange.sql.query.template.common.InsertOccupationCommon;

import java.util.Collections;
import java.util.Map;

public class InsertOccupation3 extends SpecificTemplateSqlQuery {

    @Override
    public TemplateSqlQuery commonTemplateSqlQuery() {
        return new InsertOccupationCommon();
    }

    @Override
    public Map<String, Object> specificParameters() {
        return Collections.singletonMap("id", Id.OCC_3);
    }
}
