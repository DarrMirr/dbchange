package com.github.darrmirr.dbchange.changeset;

import com.github.darrmirr.dbchange.sql.query.*;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class ChangeSetFactoryTest {

    @Test
    void getFromSqlQuery() {
        SqlQuery query = () -> "insert into test_table(id) values(1);";
        ChangeSetProvider changeSetProvider = ChangeSetFactory.get(query);

        assertThat(changeSetProvider, notNullValue());
        assertThat(changeSetProvider.getChangeSet(), hasSize(1));
        assertThat(changeSetProvider.getChangeSet().get(0).getQuery(), equalTo("insert into test_table(id) values(1);"));
        assertThat(changeSetProvider.getChangeSet().get(0).getParams().isEmpty(), equalTo(Boolean.TRUE));
    }

    @Test
    void getFromTemplateSqlQuery() {
        SqlQuery query = TemplateSqlQuery
                .templateBuilder("insert into test_table(id) values(:id);")
                .withParam("id", 1)
                .build();
        ChangeSetProvider changeSetProvider = ChangeSetFactory.get(query);

        assertThat(changeSetProvider, notNullValue());
        assertThat(changeSetProvider.getChangeSet(), hasSize(1));
        assertThat(changeSetProvider.getChangeSet().get(0).getQuery(), equalTo("insert into test_table(id) values(:id);"));
        assertThat(changeSetProvider.getChangeSet().get(0).getParams().size(), equalTo(1));
        assertThat(changeSetProvider.getChangeSet().get(0).getParams(), hasEntry("id", 1));
    }

    @Test
    void getFromInsertSqlQuery() {
        SqlQuery query = InsertSqlQuery
                .insertBuilder("test_table")
                .withParam("id", 1)
                .build();
        ChangeSetProvider changeSetProvider = ChangeSetFactory.get(query);

        assertThat(changeSetProvider, notNullValue());
        assertThat(changeSetProvider.getChangeSet(), hasSize(1));
        assertThat(changeSetProvider.getChangeSet().get(0).getQuery(), equalTo("insert into test_table(id) values (:id);"));
        assertThat(changeSetProvider.getChangeSet().get(0).getParams().size(), equalTo(1));
        assertThat(changeSetProvider.getChangeSet().get(0).getParams(), hasEntry("id", 1));
    }

    @Test
    void getFromSpecificTemplateSqlQuery() {
        TemplateSqlQuery commonQuery = TemplateSqlQuery
                .templateBuilder("insert into test_table(id) values(:id);")
                .withParam("id", 1)
                .build();
        SqlQuery query = SpecificTemplateSqlQuery
                .specificBuilder(commonQuery)
                .withParam("id", 2)
                .withParam("name", "test_name")
                .build();
        ChangeSetProvider changeSetProvider = ChangeSetFactory.get(query);

        assertThat(changeSetProvider, notNullValue());
        assertThat(changeSetProvider.getChangeSet(), hasSize(1));
        assertThat(changeSetProvider.getChangeSet().get(0).getQuery(), equalTo("insert into test_table(id) values(:id);"));
        assertThat(changeSetProvider.getChangeSet().get(0).getParams().size(), equalTo(2));
        assertThat(changeSetProvider.getChangeSet().get(0).getParams(), hasEntry("id", 2));
        assertThat(changeSetProvider.getChangeSet().get(0).getParams(), hasEntry("name", "test_name"));
    }

    @Test
    void getFromChainedSqlQuery() {
        SqlQuery query = new TestChainedSqlQuery();
        ChangeSetProvider changeSetProvider = ChangeSetFactory.get(query);

        assertThat(changeSetProvider, notNullValue());
        assertThat(changeSetProvider.getChangeSet(), hasSize(2));
        assertThat(changeSetProvider.getChangeSet().get(0).getQuery(), equalTo("insert into test_table(id) values(1);"));
        assertThat(changeSetProvider.getChangeSet().get(0).getParams().isEmpty(), equalTo(Boolean.TRUE));
        assertThat(changeSetProvider.getChangeSet().get(1).getQuery(), equalTo("insert into test_table(id) values(2);"));
        assertThat(changeSetProvider.getChangeSet().get(1).getParams().isEmpty(), equalTo(Boolean.TRUE));
    }

    private static class TestChainedSqlQuery implements SqlQuery, ChainedSqlQuery {

        @Override
        public SqlQuery next() {
            return () -> "insert into test_table(id) values(2);";
        }

        @Override
        public String get() {
            return "insert into test_table(id) values(1);";
        }
    }
}