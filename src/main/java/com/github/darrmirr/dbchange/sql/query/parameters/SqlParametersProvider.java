package com.github.darrmirr.dbchange.sql.query.parameters;

import java.util.Map;

/**
 * Interface to supply named JDBC parameters to {@link com.github.darrmirr.dbchange.sql.query.TemplateSqlQuery}.
 */
@FunctionalInterface
public interface SqlParametersProvider {

    Map<String, Object> getParameters();
}
