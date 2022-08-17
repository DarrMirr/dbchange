package com.github.darrmirr.dbchange.sql.query;

import com.github.darrmirr.dbchange.sql.query.parameters.SqlParametersProvider;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class that provide information for template sql query with named JDBC parameters.
 *
 * Named JDBC parameter has character ":" before parameter's name.
 * For example:
 *     select * from table_name where column1 = :param_1;
 */
public abstract class TemplateSqlQuery implements SqlParametersProvider, SqlQuery {

    /**
     * Sql query template with named JDBC parameters.
     *
     * @return string of sql query template with named JDBC parameters.
     */
    public abstract String queryTemplate();

    @Override
    public final String get() {
        return queryTemplate();
    }

    public static Builder templateBuilder(String queryTemplate) {
        return new Builder(queryTemplate);
    }

    public static class Builder {
        private final String queryTemplate;
        private final Map<String, Object> parameters = new LinkedHashMap<>();

        private Builder(String queryTemplate) {
            this.queryTemplate = queryTemplate;
        }

        public Builder withParam(String key, Object value) {
            parameters.put(key, value);
            return this;
        }

        public Builder withParams(Map<String, Object> params) {
            parameters.putAll(params);
            return this;
        }

        public TemplateSqlQuery build() {
            return new TemplateSqlQuery() {

                @Override
                public String queryTemplate() {
                    return queryTemplate;
                }

                @Override
                public Map<String, Object> getParameters() {
                    return parameters;
                }
            };
        }
    }
}
