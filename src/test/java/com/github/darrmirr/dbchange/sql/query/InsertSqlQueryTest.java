package com.github.darrmirr.dbchange.sql.query;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class InsertSqlQueryTest {

    @Test
    void insertBuilder() {
        Map<String, Object> expectedMap = new LinkedHashMap<>();
        expectedMap.put("id", 1);
        expectedMap.put("name", "test_name");

        InsertSqlQuery sqlQuery = InsertSqlQuery
                .insertBuilder("test_table")
                .withParams(expectedMap)
                .withParam("param1", "param1")
                .withParam("param2", "param2")
                .build();

        expectedMap.put("param1", "param1");
        expectedMap.put("param2", "param2");

        assertThat(sqlQuery.tableName(), equalTo("test_table"));
        assertThat(sqlQuery.getParameters().toString(), equalTo(expectedMap.toString()));
        assertThat(sqlQuery.queryTemplate(), equalTo("insert into test_table(id,name,param1,param2) values (:id,:name,:param1,:param2);"));
    }
}