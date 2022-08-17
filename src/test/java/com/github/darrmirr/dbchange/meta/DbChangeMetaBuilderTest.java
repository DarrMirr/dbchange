package com.github.darrmirr.dbchange.meta;

import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import com.github.darrmirr.dbchange.annotation.onmethod.DbChange;
import com.github.darrmirr.dbchange.sql.query.SqlQuery;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

class DbChangeMetaBuilderTest {
    private final DbChange mockDbChange = Mockito.mock(DbChange.class);
    private final DbChangeOnce mockDbChangeOnce = Mockito.mock(DbChangeOnce.class);

    @Test
    void toDbChangeMeta() {
        Mockito.when(mockDbChange.changeSet()).thenReturn(new Class[]{ TestSqlQuery.class });
        Mockito.when(mockDbChange.sqlExecutorGetter()).thenReturn("testSqlExecutorGetter");
        Mockito.when(mockDbChange.sqlQueryGetter()).thenReturn("testSqlQueryGetter");
        Mockito.when(mockDbChange.sqlQueryFiles()).thenReturn(new String[]{ "testSqlQueryFiles" });
        Mockito.when(mockDbChange.statements()).thenReturn(new String[] { "insert into test_table(id) values(1);" });
        Mockito.when(mockDbChange.executionPhase()).thenReturn(DbChange.ExecutionPhase.BEFORE_TEST);

        DbChangeMeta meta = DbChangeMetaBuilder.toDbChangeMeta(mockDbChange);

        assertThat(meta.changeSet(), hasSize(1));
        assertThat(meta.changeSet(), hasItem(TestSqlQuery.class));
        assertThat(meta.sqlExecutorGetter(), equalTo("testSqlExecutorGetter"));
        assertThat(meta.sqlQueryGetter(), equalTo("testSqlQueryGetter"));
        assertThat(meta.sqlQueryFiles(), hasSize(1));
        assertThat(meta.sqlQueryFiles(), hasItem("testSqlQueryFiles"));
        assertThat(meta.statements(),  hasSize(1));
        assertThat(meta.statements(),  hasItem("insert into test_table(id) values(1);"));
        assertThat(meta.executionPhase(),  is(DbChangeMeta.ExecutionPhase.BEFORE_TEST));
        assertThat(meta.source(),  is(DbChangeMeta.Source.ON_METHOD));
    }

    @Test
    void testToDbChangeMeta() {
        Mockito.when(mockDbChangeOnce.changeSet()).thenReturn(new Class[]{ TestSqlQuery.class });
        Mockito.when(mockDbChangeOnce.sqlExecutorGetter()).thenReturn("testSqlExecutorGetter");
        Mockito.when(mockDbChangeOnce.sqlQueryGetter()).thenReturn("testSqlQueryGetter");
        Mockito.when(mockDbChangeOnce.sqlQueryFiles()).thenReturn(new String[]{ "testSqlQueryFiles" });
        Mockito.when(mockDbChangeOnce.statements()).thenReturn(new String[] { "insert into test_table(id) values(1);" });
        Mockito.when(mockDbChangeOnce.executionPhase()).thenReturn(DbChangeOnce.ExecutionPhase.AFTER_ALL);

        DbChangeMeta meta = DbChangeMetaBuilder.toDbChangeMeta(mockDbChangeOnce);

        assertThat(meta.changeSet(), hasSize(1));
        assertThat(meta.changeSet(), hasItem(TestSqlQuery.class));
        assertThat(meta.sqlExecutorGetter(), equalTo("testSqlExecutorGetter"));
        assertThat(meta.sqlQueryGetter(), equalTo("testSqlQueryGetter"));
        assertThat(meta.sqlQueryFiles(), hasSize(1));
        assertThat(meta.sqlQueryFiles(), hasItem("testSqlQueryFiles"));
        assertThat(meta.statements(),  hasSize(1));
        assertThat(meta.statements(),  hasItem("insert into test_table(id) values(1);"));
        assertThat(meta.executionPhase(),  is(DbChangeMeta.ExecutionPhase.AFTER_ALL));
        assertThat(meta.source(),  is(DbChangeMeta.Source.ON_CLASS));
    }

    private static class TestSqlQuery implements SqlQuery {

        @Override
        public String get() {
            return "null";
        }
    }
}