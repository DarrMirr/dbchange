package com.github.darrmirr.dbchange.sql.query.getter;

import com.github.darrmirr.dbchange.sql.query.SqlQuery;

import java.util.List;
import java.util.function.Supplier;

/**
 * Interface to supply @{@link List} of {@link SqlQuery} from method defined in test class.
 */
@FunctionalInterface
public interface SqlQueryGetter extends Supplier<List<SqlQuery>> {
}
