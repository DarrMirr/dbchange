package com.github.darrmirr.dbchange.meta;

import com.github.darrmirr.dbchange.sql.query.SqlQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class describe data structure that common for all sources.
 *
 * For more details regard to class fields
 * see description in {@link com.github.darrmirr.dbchange.annotation.onmethod.DbChange}.
 */
public final class DbChangeMeta {
    private List<Class<? extends SqlQuery>> changeSet;
    private String sqlQueryGetter;
    private String sqlExecutorGetter;
    private List<String> sqlQueryFiles;
    private List<String> statements;
    private ExecutionPhase executionPhase;
    private Source source;

    /**
     * Notice:
     * {@link ExecutionPhase#BEFORE_ALL} and {@link ExecutionPhase#AFTER_ALL} do not work
     * for parameterized tests due to JUnit design.
     *
     * Please, use {@link com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce}
     * for {@link ExecutionPhase#BEFORE_ALL} and {@link ExecutionPhase#AFTER_ALL}.
     */
    public enum ExecutionPhase {
        BEFORE_TEST, AFTER_TEST, BEFORE_ALL, AFTER_ALL
    }

    public enum Source {
        ON_CLASS, ON_METHOD, FROM_PARAMETER
    }

    // for internal usage only
    DbChangeMeta setChangeSet(Class<? extends SqlQuery>[] changeSet) {
        return setChangeSet(Arrays.asList(changeSet));
    }

    public DbChangeMeta setChangeSet(List<Class<? extends SqlQuery>> changeSet) {
        this.changeSet = changeSet;
        return this;
    }

    public DbChangeMeta setChangeSet(Class<? extends SqlQuery> changeSet) {
        return setChangeSet(Collections.singletonList(changeSet));
    }

    public DbChangeMeta setSqlQueryGetter(String sqlQueryGetter) {
        this.sqlQueryGetter = sqlQueryGetter;
        return this;
    }

    public DbChangeMeta setSqlExecutorGetter(String sqlExecutorGetter) {
        this.sqlExecutorGetter = sqlExecutorGetter;
        return this;
    }

    public DbChangeMeta setExecutionPhase(ExecutionPhase executionPhase) {
        this.executionPhase = executionPhase;
        return this;
    }

    // for internal usage only
    DbChangeMeta setSource(Source source) {
        this.source = source;
        return this;
    }

    Source source() {
        return source;
    }

    public List<Class<? extends SqlQuery>> changeSet() {
        return changeSet;
    }

    public String sqlQueryGetter() {
        return sqlQueryGetter;
    }

    public String sqlExecutorGetter() {
        return sqlExecutorGetter;
    }

    public ExecutionPhase executionPhase() {
        return executionPhase;
    }

    public boolean isChangeSetPresent() {
        return (changeSet != null && changeSet.size() > 0)
                || (sqlQueryGetter != null && sqlQueryGetter.length() > 0 && sqlQueryGetter.trim().length() > 0)
                || (sqlQueryFiles != null && sqlQueryFiles.size() > 0)
                || (statements != null && statements.size() > 0);
    }

    public List<String> sqlQueryFiles() {
        return sqlQueryFiles;
    }

    // for internal usage only
    DbChangeMeta setSqlQueryFiles(String[] sqlQueryFiles) {
        return setSqlQueryFiles(Arrays.asList(sqlQueryFiles));
    }

    public DbChangeMeta setSqlQueryFiles(List<String> sqlQueryFiles) {
        this.sqlQueryFiles = sqlQueryFiles;
        return this;
    }

    public DbChangeMeta setSqlQueryFiles(String sqlQueryFiles) {
        return setSqlQueryFiles(Collections.singletonList(sqlQueryFiles));
    }

    // for internal usage only
    DbChangeMeta setStatements(String[] statements) {
        return setStatements(Arrays.asList(statements));
    }

    public DbChangeMeta setStatements(List<String> statements) {
        this.statements = statements;
        return this;
    }

    public DbChangeMeta setStatements(String statements) {
        return setStatements(Collections.singletonList(statements));
    }

    public List<String> statements() {
        return statements;
    }

    @Override
    public String toString() {
        return "DbChangeMeta{" +
                "changeSet=" + changeSet +
                ", sqlQueryGetter='" + sqlQueryGetter + '\'' +
                ", sqlExecutorGetter='" + sqlExecutorGetter + '\'' +
                ", sqlQueryFiles=" + sqlQueryFiles +
                ", statements=" + statements +
                ", executionPhase=" + executionPhase +
                ", source=" + source +
                '}';
    }
}
