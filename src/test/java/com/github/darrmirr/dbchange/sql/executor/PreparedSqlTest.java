package com.github.darrmirr.dbchange.sql.executor;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class PreparedSqlTest {

    @Test
    void of() {
        String sql = "insert into test_table(id) values(:id)";

        PreparedSql preparedSql = PreparedSql.of(sql);

        assertThat(preparedSql.get(), equalTo(sql.replace(":id", "?")));
        assertThat(preparedSql.getIndexes("id"), hasSize(1));
        assertThat(preparedSql.getIndexes("id").get(0), equalTo(1));
    }

    @Test
    void parameterMeetsTwice() {
        String sql = "insert into test_table(id, time, name) values(:id, :time, 'test' || :id)";

        PreparedSql preparedSql = PreparedSql.of(sql);

        assertThat(preparedSql.get(), equalTo("insert into test_table(id, time, name) values(?, ?, 'test' || ?)"));
        assertThat(preparedSql.getIndexes("id"), hasSize(2));
        assertThat(preparedSql.getIndexes("id").get(0), equalTo(1));
        assertThat(preparedSql.getIndexes("id").get(1), equalTo(3));
    }

    @Test
    void parameterIgnoredInString() {
        String sql = "insert into test_table(id, time, name) values(:id, :time, 'test :id')";

        PreparedSql preparedSql = PreparedSql.of(sql);

        assertThat(preparedSql.get(), equalTo("insert into test_table(id, time, name) values(?, ?, 'test :id')"));
        assertThat(preparedSql.getIndexes("id"), hasSize(1));
        assertThat(preparedSql.getIndexes("id").get(0), equalTo(1));
    }

    @Test
    void parameterNotPresent() {
        String sql = "insert into test_table(id) values(:id)";

        PreparedSql preparedSql = PreparedSql.of(sql);

        assertThat(preparedSql.get(), equalTo(sql.replace(":id", "?")));
        assertThat(preparedSql.getIndexes("name"), hasSize(0));
    }

    @Test
    void queryWithoutParameters() {
        String sql = "insert into test_table(id) values(1)";

        PreparedSql preparedSql = PreparedSql.of(sql);

        assertThat(preparedSql.get(), equalTo(sql));
    }
}