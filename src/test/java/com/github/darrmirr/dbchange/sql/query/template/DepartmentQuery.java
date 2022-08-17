package com.github.darrmirr.dbchange.sql.query.template;

import com.github.darrmirr.dbchange.sql.query.JdbcQueryTemplates;
import com.github.darrmirr.dbchange.sql.query.TemplateSqlQuery;

import java.util.Map;

public class DepartmentQuery {
    public static final String PARAM_ID = "id";
    public static final String PARAM_NAME = "name";

    public static class Insert {

        public static TemplateSqlQuery.Builder withParam(String key, Object value) {
            return TemplateSqlQuery
                    .templateBuilder(JdbcQueryTemplates.DEPARTMENT_INSERT)
                    .withParam(key, value);
        }

        public static TemplateSqlQuery.Builder withParams(Map<String, Object> params) {
            return TemplateSqlQuery
                    .templateBuilder(JdbcQueryTemplates.DEPARTMENT_INSERT)
                    .withParams(params);
        }
    }

    public static class Delete {

        public static TemplateSqlQuery.Builder withParam(String key, Object value) {
            return TemplateSqlQuery
                    .templateBuilder(JdbcQueryTemplates.DEPARTMENT_DELETE_ONE)
                    .withParam(key, value);
        }

        public static TemplateSqlQuery.Builder withParams(Map<String, Object> params) {
            return TemplateSqlQuery
                    .templateBuilder(JdbcQueryTemplates.DEPARTMENT_DELETE_ONE)
                    .withParams(params);
        }
    }
}
