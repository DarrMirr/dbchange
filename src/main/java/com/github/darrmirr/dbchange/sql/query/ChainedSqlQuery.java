package com.github.darrmirr.dbchange.sql.query;

/**
 * Interface for {@link SqlQuery} that chained to each other.
 *
 * "Chained" meaning:
 *     - two or more sql queries that related to each other with some business relationship.
 *
 *  This interface should be used to show relationship between two or more sql queries.
 *  For example:
 *      There are two queries:
 *          - insert into department(id, name) values('1', 'it');
 *          - insert into employee(first_name, last_name, department_id) values('Ivan', 'Ivanov', 1);
 *
 *      Sql queries in java code:
 *          public class InsertIvanIvanov implements SqlQuery, ChainedSqlQuery {
 *
 *              public String get() {
 *                  return () -&gt; "insert into department(id, name) values('1', 'it');"
 *              }
 *
 *              public SqlQuery next() {
 *                  return () -&gt; "insert into employee(first_name, last_name, department_id) values('Ivan', 'Ivanov', 1);";
 *              }
 *          }
 */
@FunctionalInterface
public interface ChainedSqlQuery {

    /**
     * Get next instance of {@link SqlQuery} that relates to current one.
     *
     * @return instance of {@link SqlQuery}.
     */
    SqlQuery next();
}
