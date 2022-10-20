package com.github.darrmirr.dbchange.util.function;

import com.github.darrmirr.dbchange.changeset.ChangeSetFactory;
import com.github.darrmirr.dbchange.changeset.ChangeSetItem;
import com.github.darrmirr.dbchange.changeset.ChangeSetProvider;
import com.github.darrmirr.dbchange.meta.DbChangeMeta;
import com.github.darrmirr.dbchange.sql.executor.SqlExecutor;
import com.github.darrmirr.dbchange.sql.executor.factory.SqlExecutorFactory;
import com.github.darrmirr.dbchange.sql.query.factory.SqlQueryFactory;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Project function bank.
 */
public final class Functions {
    public static final BiFunction<DbChangeMeta, TestInstanceSupplier, List<ChangeSetItem>> CHANGESET_EXTRACTOR = toChangeSetList();
    public static final BiFunction<DbChangeMeta, TestInstanceSupplier, SqlExecutor> SQL_EXECUTOR = toSqlExecutor();

    private Functions() {}

    /**
     * Extract {@link ChangeSetItem} list according to information in {@link DbChangeMeta}.
     *
     * @return function to extract {@link ChangeSetItem}.
     */
    private static BiFunction<DbChangeMeta, TestInstanceSupplier, List<ChangeSetItem>> toChangeSetList() {
        return (dbChange, testInstanceSupplier) -> SqlQueryFactory
                .getStream(dbChange, testInstanceSupplier.get())
                .map(ChangeSetFactory::get)
                .reduce(ChangeSetProvider::chain)
                .orElseGet(() -> Collections::emptyList)
                .getChangeSet();
    }

    /**
     * Create {@link SqlExecutor} according to information in {@link DbChangeMeta}.
     *
     * @return function to create {@link SqlExecutor}.
     */
    private static BiFunction<DbChangeMeta, TestInstanceSupplier, SqlExecutor> toSqlExecutor() {
        return (dbChange, testInstanceSupplier) -> SqlExecutorFactory.get(dbChange, testInstanceSupplier.get());
    }
}
