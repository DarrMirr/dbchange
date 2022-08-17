package com.github.darrmirr.dbchange.sql.query;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Class created for internal usage in order to convert plain {@link SqlQuery} to {@link TemplateSqlQuery}.
 */
public final class EmptyTemplateSqlQuery extends TemplateSqlQuery {
    private final String queryTemplate;

    public static EmptyTemplateSqlQuery of(SqlQuery sqlQuery) {
        Objects.requireNonNull(sqlQuery);
        return new EmptyTemplateSqlQuery(sqlQuery.get());
    }

    public EmptyTemplateSqlQuery(String queryTemplate) {
        this.queryTemplate = queryTemplate;
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.emptyMap();
    }

    @Override
    public String queryTemplate() {
        return queryTemplate;
    }
}
