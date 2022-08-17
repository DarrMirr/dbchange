package com.github.darrmirr.dbchange.sql.executor.factory;

import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.meta.DbChangeMeta;
import com.github.darrmirr.dbchange.sql.executor.SqlExecutor;
import com.github.darrmirr.dbchange.util.ObjectFactory;

import java.util.Optional;

public final class SqlExecutorFactory {

    private SqlExecutorFactory() { }

    public static SqlExecutor get(DbChangeMeta dbChange, Object testClassInstance) {
        if (dbChange.sqlExecutorGetter() == null || dbChange.sqlExecutorGetter().isEmpty() || dbChange.sqlExecutorGetter().trim().isEmpty()) {
            return getFromClass(testClassInstance)
                    .orElseThrow(() -> new IllegalStateException("Error to get sql executor from annotation"));
        }
        return ObjectFactory
                .getByMethod(SqlExecutor.class, dbChange.sqlExecutorGetter(), testClassInstance)
                .orElseThrow(() -> new IllegalStateException("Error to get sql executor from method : method_name=" + dbChange.sqlExecutorGetter()));
    }

    private static Optional<SqlExecutor> getFromClass(Object testClassInstance) {
        SqlExecutorGetter sqlExecutorGetter = testClassInstance
                .getClass()
                .getAnnotation(SqlExecutorGetter.class);
        if (sqlExecutorGetter == null) {
            return Optional.empty();
        }
        return ObjectFactory.getByMethod(SqlExecutor.class, sqlExecutorGetter.value(), testClassInstance);
    }
}
