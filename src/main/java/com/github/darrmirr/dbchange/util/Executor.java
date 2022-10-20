package com.github.darrmirr.dbchange.util;

import com.github.darrmirr.dbchange.changeset.ChangeSetItem;
import com.github.darrmirr.dbchange.meta.DbChangeMeta;
import com.github.darrmirr.dbchange.sql.executor.SqlExecutor;
import com.github.darrmirr.dbchange.util.function.BiConsumerSubstitute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Executor for {@link DbChangeMeta}.
 */
public final class Executor {
    private static final Logger log = LoggerFactory.getLogger(Executor.class);

    /**
     * Return function to execute {@link DbChangeMeta}.
     *
     * @param dbChange to execute.
     * @return function.
     */
    public static BiConsumerSubstitute<List<ChangeSetItem>, SqlExecutor> execute(DbChangeMeta dbChange) {
        return (changeSet, sqlExecutor) -> {
            if (changeSet.isEmpty()) {
                log.warn("Change set is empty : [ object={} ]", dbChange);
            } else {
                log.debug("Start execute changeSet : [ object={} ]", changeSet);
                sqlExecutor.execute(changeSet);
                log.debug("Finish execute changeSet : [ object={} ]", changeSet);
            }
        };
    }
}
