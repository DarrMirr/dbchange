package com.github.darrmirr.dbchange.sql.query;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Kind of {@link TemplateSqlQuery} provides capability to create SQL query template
 * depends on parameter names and table name.
 */
public abstract class InsertSqlQuery extends TemplateSqlQuery {

    /**
     * Table name to insert data.
     *
     * @return table name to insert data.
     */
    public abstract String tableName();

    @Override
    public final String queryTemplate() {
        Set<String> columns = this.getParameters().keySet();
        Objects.requireNonNull(columns, "Null value for sql parameters does not make sense for class=" + InsertSqlQuery.class);
        return "insert into "
                + tableName()
                + "(" + String.join(",", columns) + ")"
                + " values "
                + "(" + columns.stream().map(column -> ":" + column).collect(Collectors.joining(",")) + ");";
    }

    public static InsertSqlQuery.Builder insertBuilder(String tableName) {
        return new InsertSqlQuery.Builder(tableName);
    }

    public static class Builder {
        private final String tableName;
        private final Map<String, Object> parameters = new LinkedHashMap<>();

        private Builder(String tableName) {
            this.tableName = tableName;
        }

        public InsertSqlQuery.Builder withParam(String key, Object value) {
            parameters.put(key, value);
            return this;
        }

        public InsertSqlQuery.Builder withParams(Map<String, Object> params) {
            parameters.putAll(params);
            return this;
        }

        public InsertSqlQuery build() {
            return new InsertSqlQuery() {

                @Override
                public String tableName() {
                    return tableName;
                }

                @Override
                public Map<String, Object> getParameters() {
                    return parameters;
                }
            };
        }
    }
}
