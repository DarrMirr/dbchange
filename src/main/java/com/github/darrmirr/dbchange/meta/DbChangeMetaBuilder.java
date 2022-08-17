package com.github.darrmirr.dbchange.meta;

import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import com.github.darrmirr.dbchange.annotation.onmethod.DbChange;

import static com.github.darrmirr.dbchange.meta.DbChangeMeta.Source.ON_CLASS;
import static com.github.darrmirr.dbchange.meta.DbChangeMeta.Source.ON_METHOD;

/**
 * Builder for {@link DbChangeMeta} class.
 */
public final class DbChangeMetaBuilder {

    private DbChangeMetaBuilder() { }

    static DbChangeMeta toDbChangeMeta(DbChange dbChange) {
        return new DbChangeMeta()
                .setChangeSet(dbChange.changeSet())
                .setSqlQueryGetter(dbChange.sqlQueryGetter())
                .setSqlExecutorGetter(dbChange.sqlExecutorGetter())
                .setSqlQueryFiles(dbChange.sqlQueryFiles())
                .setStatements(dbChange.statements())
                .setExecutionPhase(DbChangeMeta.ExecutionPhase.valueOf(dbChange.executionPhase().name()))
                .setSource(ON_METHOD);
    }

    static DbChangeMeta toDbChangeMeta(DbChangeOnce dbChange) {
        return new DbChangeMeta()
                .setChangeSet(dbChange.changeSet())
                .setSqlQueryGetter(dbChange.sqlQueryGetter())
                .setSqlExecutorGetter(dbChange.sqlExecutorGetter())
                .setSqlQueryFiles(dbChange.sqlQueryFiles())
                .setStatements(dbChange.statements())
                .setExecutionPhase(DbChangeMeta.ExecutionPhase.valueOf(dbChange.executionPhase().name()))
                .setSource(ON_CLASS);
    }
}
