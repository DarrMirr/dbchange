package com.github.darrmirr.dbchange.annotation.onclass;

import com.github.darrmirr.dbchange.sql.query.SqlQuery;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta information about RDBMS changes before/after all tests execution in class.
 *
 * Sql query sources:
 *   - changeSet
 *   - sqlQueryGetter
 *   - sqlQueryFiles
 *
 * Sql queries from all sources unites into one list and
 * it passes into {@link com.github.darrmirr.dbchange.sql.executor.SqlExecutor}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(DbChangesOnce.class)
public @interface DbChangeOnce {


    /**
     * RDBMS changes provided by array of {@link SqlQuery} classes.
     *
     * Classes must have default constructor (constructor without parameters).
     * Use {@link DbChangeOnce#sqlQueryGetter()} for classes with constructor with parameters.
     *
     * Notice:
     * Queries are executed in the same order as they present in array.
     * Please, use {@link com.github.darrmirr.dbchange.sql.query.ChainedSqlQuery}
     * if you need to show execution order in your project source code.
     *
     * @return array of {@link SqlQuery} classes.
     */
    Class<? extends SqlQuery>[] changeSet() default {};

    /**
     * RDBMS changes provided by method name defined in test class.
     * Method must return {@link com.github.darrmirr.dbchange.sql.query.getter.SqlQueryGetter} object.
     * Method could have static modifier, but it allows omitting static modifier and make method as instance one.
     *
     * Notice:
     * Method must have public access modifier
     *
     * @return method name defined in test class.
     */
    String sqlQueryGetter() default "";

    /**
     * RDBMS changes provided by sql files.
     *
     * Notice:
     * Project's classpath path is root dir for relative path.
     *
     * @return sql file paths.
     */
    String[] sqlQueryFiles() default { "" };

    /**
     * Method name defined in test class
     * that returns instance of {@link com.github.darrmirr.dbchange.sql.executor.SqlExecutor} class.
     *
     * Notice:
     * Method must have public access modifier
     *
     * @return method name defined in test class.
     */
    String sqlExecutorGetter() default "";

    /**
     * Provide inline sql query statements.
     *
     * @return array of inline sql query statements.
     */
    String[] statements() default "";

    /**
     * Set execution phase when RDBMS changes should be applied.
     *
     * @return instance of {@link ExecutionPhase}
     */
    ExecutionPhase executionPhase() default ExecutionPhase.BEFORE_ALL;

    /**
     * Execution phase enum.
     */
    enum ExecutionPhase {
        BEFORE_ALL, AFTER_ALL
    }
}
