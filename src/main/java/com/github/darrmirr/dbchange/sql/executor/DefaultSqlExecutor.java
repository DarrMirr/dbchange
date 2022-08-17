package com.github.darrmirr.dbchange.sql.executor;

import com.github.darrmirr.dbchange.changeset.ChangeSetItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Simple implementation of {@link SqlExecutor} interfaces.
 * <p>
 * This implementation expects named JDBC parameters.
 * Named JDBC parameter has character ":" before parameter's name.
 * Example:
 * select * from table_name where column1 = :param_1;
 */
public final class DefaultSqlExecutor implements SqlExecutor {
    private static final Logger log = LoggerFactory.getLogger(DefaultSqlExecutor.class);
    private final DataSource dataSource;

    public DefaultSqlExecutor(DataSource dataSource) {
        Objects.requireNonNull(dataSource);
        this.dataSource = dataSource;
    }

    @Override
    public void execute(List<ChangeSetItem> changeSet) {
        if (changeSet == null || changeSet.isEmpty()) {
            log.debug("Stop sql query execution due to db change set is empty");
            return;
        }
        try (Connection connection = dataSource.getConnection()) {
            for (ChangeSetItem changeSetItem : changeSet) {
                String namedSql = changeSetItem.getQuery();
                PreparedSql preparedSql = PreparedSql.of(namedSql);
                PreparedStatement statement = connection.prepareStatement(preparedSql.get());
                Set<Map.Entry<String, Object>> params = changeSetItem.getParams().entrySet();

                for (Map.Entry<String, Object> param : params) {
                    List<Integer> indexes = preparedSql.getIndexes(param.getKey());
                    for (Integer index : indexes) {
                        statement.setObject(index, param.getValue());
                    }
                }
                log.debug("Execute sql query : sql={}, parameters={}", namedSql, changeSetItem.getParams());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
