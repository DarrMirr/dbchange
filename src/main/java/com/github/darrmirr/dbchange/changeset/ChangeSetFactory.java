package com.github.darrmirr.dbchange.changeset;

import com.github.darrmirr.dbchange.sql.query.ChainedSqlQuery;
import com.github.darrmirr.dbchange.sql.query.EmptyTemplateSqlQuery;
import com.github.darrmirr.dbchange.sql.query.SqlQuery;
import com.github.darrmirr.dbchange.sql.query.TemplateSqlQuery;

import java.util.Collections;
import java.util.List;

/**
 * Factory class to create instance of {@link ChangeSetProvider}.
 */
public final class ChangeSetFactory {

    private ChangeSetFactory() {
    }

    private static List<ChangeSetItem> createNew(TemplateSqlQuery sqlQuery) {
        ChangeSetItem changeSetItem = new ChangeSetItem(sqlQuery.get(), sqlQuery.getParameters());
        return Collections.singletonList(changeSetItem);
    }

    private static ChangeSetProvider createChangeSetProvider(TemplateSqlQuery sqlProvider) {
        return () -> createNew(sqlProvider);
    }

    /**
     * Create instance of {@link ChangeSetProvider} from {@link SqlQuery}.
     *
     * If {@link SqlQuery} object is instance of {@link ChainedSqlQuery}
     * factory recursively passes through all chained sql query and
     * unites it into one instance of {@link ChangeSetProvider}.
     *
     * @param sqlQuery information about sql query.
     * @return instance of {@link ChangeSetProvider}.
     */
    public static ChangeSetProvider get(SqlQuery sqlQuery) {
        if (sqlQuery instanceof ChainedSqlQuery) {
            ChainedSqlQuery chainedSqlQuery = (ChainedSqlQuery) sqlQuery;
            ChangeSetProvider nextChangeSetProvider = get(chainedSqlQuery.next());
            return toChangeSetProvider(sqlQuery).chain(nextChangeSetProvider);
        }
        return toChangeSetProvider(sqlQuery);
    }

    private static ChangeSetProvider toChangeSetProvider(SqlQuery sqlQuery) {
        if (sqlQuery instanceof TemplateSqlQuery) {
            TemplateSqlQuery templateSqlQuery = (TemplateSqlQuery) sqlQuery;
            return createChangeSetProvider(templateSqlQuery);
        }
        return createChangeSetProvider(EmptyTemplateSqlQuery.of(sqlQuery));
    }
}
