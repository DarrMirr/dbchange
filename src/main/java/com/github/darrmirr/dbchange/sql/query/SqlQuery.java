package com.github.darrmirr.dbchange.sql.query;

import java.util.function.Supplier;

/**
 * Common interface for all sql query.
 */
@FunctionalInterface
public interface SqlQuery extends Supplier<String> {
}
