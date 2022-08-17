package com.github.darrmirr.dbchange.sql;

import com.github.darrmirr.dbchange.sql.mapper.ResultMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class SqlUtils {
    public final DataSource dataSource;

    public SqlUtils(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> select(String sql, ResultMapper<T> resultMapper) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()
        ) {
            ResultSet resultSet = statement.executeQuery(sql);
            List<T> list = new ArrayList<>();
            while (resultSet.next()) {
                T t = resultMapper.map(resultSet);
                list.add(t);
            }
            return list;
        }
    }
}
