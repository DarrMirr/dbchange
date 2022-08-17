package com.github.darrmirr.dbchange.meta;

import com.github.darrmirr.dbchange.sql.query.SqlQuery;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class DbChangeMetaTest {
    private final DbChangeMeta meta = new DbChangeMeta();

    @Test
    void isChangeSetPresentChangeSet() {
        meta.setChangeSet(TestSqlQuery.class);

        assertThat(meta.isChangeSetPresent(), is(Boolean.TRUE));
    }

    private static class TestSqlQuery implements SqlQuery {
        @Override
        public String get() {
            return "null";
        }
    }

    @Test
    void isChangeSetPresentSqlQueryGetter() {
        meta.setSqlQueryGetter("test");

        assertThat(meta.isChangeSetPresent(), is(Boolean.TRUE));
    }

    @Test
    void isChangeSetPresentSqlQueryFiles() {
        meta.setSqlQueryFiles("test.sql");

        assertThat(meta.isChangeSetPresent(), is(Boolean.TRUE));
    }

    @Test
    void isChangeSetPresentStatements() {
        meta.setStatements("insert into test_table(id) values(1);");

        assertThat(meta.isChangeSetPresent(), is(Boolean.TRUE));
    }

    @Test
    void isChangeSetPresentNegative() {

        assertThat(meta.isChangeSetPresent(), is(Boolean.FALSE));
    }

    @Test
    void isChangeSetPresentSqlQueryGetterNegative() {
        meta.setSqlQueryGetter("");

        assertThat(meta.isChangeSetPresent(), is(Boolean.FALSE));
    }

    @Test
    void isChangeSetPresentChangeSetNegative() {
        meta.setChangeSet(Collections.emptyList());

        assertThat(meta.isChangeSetPresent(), is(Boolean.FALSE));
    }

    @Test
    void isChangeSetPresentSqlQueryFilesNegative() {
        meta.setSqlQueryFiles(Collections.emptyList());

        assertThat(meta.isChangeSetPresent(), is(Boolean.FALSE));
    }

    @Test
    void isChangeSetPresentStatementsNegative() {
        meta.setStatements(Collections.emptyList());

        assertThat(meta.isChangeSetPresent(), is(Boolean.FALSE));
    }
}