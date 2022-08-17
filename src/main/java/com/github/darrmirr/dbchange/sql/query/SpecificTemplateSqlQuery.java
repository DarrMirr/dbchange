package com.github.darrmirr.dbchange.sql.query;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Kind of {@link TemplateSqlQuery} provides capability to reuse SQL query template
 * and supplies specific named parameters or overrides ones from common template.
 */
public abstract class SpecificTemplateSqlQuery extends TemplateSqlQuery {
    private final TemplateSqlQuery commonTemplateSqlQuery;

    public SpecificTemplateSqlQuery() {
        this.commonTemplateSqlQuery = this.commonTemplateSqlQuery();
    }

    @Override
    public final String queryTemplate() {
        return Optional
                .ofNullable(commonTemplateSqlQuery)
                .map(TemplateSqlQuery::get)
                .orElseThrow(() -> new IllegalStateException("Error to get sql query template : [ 'reason=template sql query is null' ]"));
    }

    @Override
    public final Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        // order is matter because it allows for specific parameters to override values from common one.
        Optional.ofNullable(commonTemplateSqlQuery)
                .map(TemplateSqlQuery::getParameters)
                .ifPresent(params::putAll);
        Optional.ofNullable(specificParameters())
                .ifPresent(params::putAll);
        return params;
    }

    /**
     * Common {@link TemplateSqlQuery} that is base for current sql query.
     *
     * @return instance of {@link TemplateSqlQuery}.
     */
    public abstract TemplateSqlQuery commonTemplateSqlQuery();

    /**
     * Return specific named parameters for current sql query.
     *
     * Name in common named parameters will be overridden
     * if name of specific named parameters hits with common one.
     *
     * @return map of specific named parameters for current sql query.
     */
    public abstract Map<String, Object> specificParameters();

    public static Builder specificBuilder(TemplateSqlQuery commonTemplateSqlQuery) {
        return new Builder(commonTemplateSqlQuery);
    }

    public static class Builder {
        private final TemplateSqlQuery commonTemplateSqlQuery;
        private final Map<String, Object> parameters = new LinkedHashMap<>();

        private Builder(TemplateSqlQuery commonTemplateSqlQuery) {
            this.commonTemplateSqlQuery = commonTemplateSqlQuery;
        }

        public Builder withParam(String key, Object value) {
            parameters.put(key, value);
            return this;
        }

        public Builder withParams(Map<String, Object> params) {
            parameters.putAll(params);
            return this;
        }

        public SpecificTemplateSqlQuery build() {
            return new SpecificTemplateSqlQuery() {

                @Override
                public TemplateSqlQuery commonTemplateSqlQuery() {
                    return commonTemplateSqlQuery;
                }

                @Override
                public Map<String, Object> specificParameters() {
                    return parameters;
                }
            };
        }
    }
}
