package com.github.darrmirr.dbchange.changeset;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Interface to get list of changes in RDBMS.
 *
 * Interface hide implementation of retrieving {@link ChangeSetItem} that depends on changes source.
 *
 * ChangeSetItem is generated according to information provided by following sources:
 *   - {@link com.github.darrmirr.dbchange.annotation.onmethod.DbChange} annotation.
 *   - {@link com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce} annotation.
 *   - {@link com.github.darrmirr.dbchange.sql.query.getter.SqlQueryGetter} objects.
 *   - {@link com.github.darrmirr.dbchange.meta.DbChangeMeta} object supplied as argument in Parameterized JUnit test.
 *   - sql files.
 *
 * Instance of {@link ChangeSetProvider} should be immutable object.
 * Use {@link ChangeSetProvider#chain(ChangeSetProvider)} method
 * to combine two instances of {@link ChangeSetProvider} into new one.
 */
public interface ChangeSetProvider {

    /**
     * Get list of changes in RDBMS.
     *
     * @return list of changes in RDBMS.
     */
    List<ChangeSetItem> getChangeSet();


    /**
     * Chain two {@link ChangeSetProvider} instances into new one.
     *
     * @param changeSetProvider to chain.
     * @return chained {@link ChangeSetProvider}.
     */
    default ChangeSetProvider chain(ChangeSetProvider changeSetProvider) {
        return () -> {
            List<ChangeSetItem> changes = new ArrayList<>();
            Optional.ofNullable(this.getChangeSet())
                    .ifPresent(changes::addAll);
            Optional
                    .ofNullable(changeSetProvider)
                    .map(ChangeSetProvider::getChangeSet)
                    .ifPresent(changes::addAll);
            return changes;
        };
    }
}
