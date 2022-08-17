package com.github.darrmirr.dbchange.sql.executor;

import com.github.darrmirr.dbchange.changeset.ChangeSetItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DefaultSqlExecutorTest {
    private final DataSource mockDataSource = Mockito.mock(DataSource.class);
    private final DefaultSqlExecutor sqlExecutor = new DefaultSqlExecutor(mockDataSource);
    private final Connection mockConnection = Mockito.mock(Connection.class);
    private final PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);

    @BeforeEach
    void setUp() throws SQLException {
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);
        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    void execute() throws SQLException {
        ChangeSetItem item1 = new ChangeSetItem("insert into test_table(id) values(1)", Collections.emptyMap());
        ChangeSetItem item2 = new ChangeSetItem("insert into test_table(id) values(2)", Collections.emptyMap());
        ChangeSetItem item3 = new ChangeSetItem("insert into test_table(id) values(3)", Collections.emptyMap());

        List<ChangeSetItem> items = Arrays.asList(item1, item2, item3);
        sqlExecutor.execute(items);

        Mockito.verify(mockPreparedStatement, Mockito.times(3)).executeUpdate();
    }

    @Test
    void setObject() throws SQLException {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        params.put("time", "1234567890");
        ChangeSetItem item = new ChangeSetItem("insert into test_table(id, time, name) values(:id, :time, 'test' || :id)", params);

        List<ChangeSetItem> items = Collections.singletonList(item);
        sqlExecutor.execute(items);

        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(1, 1);
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(2, "1234567890");
        Mockito.verify(mockPreparedStatement, Mockito.times(1)).setObject(3, 1);
    }

    @Test
    void nullList() throws SQLException {
        sqlExecutor.execute(null);

        Mockito.verify(mockDataSource, Mockito.never()).getConnection();
        Mockito.verify(mockConnection, Mockito.never()).prepareStatement(Mockito.anyString());
        Mockito.verify(mockPreparedStatement, Mockito.never()).setObject(Mockito.anyInt(), Mockito.any());
        Mockito.verify(mockPreparedStatement, Mockito.never()).executeUpdate();
    }

    @Test
    void emptyList() throws SQLException {
        sqlExecutor.execute(Collections.emptyList());

        Mockito.verify(mockDataSource, Mockito.never()).getConnection();
        Mockito.verify(mockConnection, Mockito.never()).prepareStatement(Mockito.anyString());
        Mockito.verify(mockPreparedStatement, Mockito.never()).setObject(Mockito.anyInt(), Mockito.any());
        Mockito.verify(mockPreparedStatement, Mockito.never()).executeUpdate();
    }
}