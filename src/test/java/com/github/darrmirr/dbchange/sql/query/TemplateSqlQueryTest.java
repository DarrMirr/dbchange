package com.github.darrmirr.dbchange.sql.query;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class TemplateSqlQueryTest {

    @Test
    void templateBuilder() {
        Map<String, Object> expectedMap = new LinkedHashMap<>();
        expectedMap.put("id", 1);
        expectedMap.put("name", "test_name");

        TemplateSqlQuery sqlQuery = TemplateSqlQuery
                .templateBuilder("insert into test_table(id,name,param1,param2) values (:id,:name,:param1,:param2);")
                .withParams(expectedMap)
                .withParam("param1", "param1")
                .withParam("param2", "param2")
                .build();

        expectedMap.put("param1", "param1");
        expectedMap.put("param2", "param2");

        assertThat(sqlQuery.queryTemplate(), equalTo("insert into test_table(id,name,param1,param2) values (:id,:name,:param1,:param2);"));
        assertThat(sqlQuery.getParameters().toString(), equalTo(expectedMap.toString()));
    }
}