package com.github.darrmirr.dbchange.sql.query.factory;

import com.github.darrmirr.dbchange.meta.DbChangeMeta;
import com.github.darrmirr.dbchange.sql.query.EmptyTemplateSqlQuery;
import com.github.darrmirr.dbchange.sql.query.SqlQuery;
import com.github.darrmirr.dbchange.sql.query.getter.SqlQueryGetter;
import com.github.darrmirr.dbchange.util.FileUtils;
import com.github.darrmirr.dbchange.util.ObjectFactory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Factory to create stream of {@link SqlQuery} received from different sources.
 */
public final class SqlQueryFactory {

    private SqlQueryFactory() { }

    public static Stream<SqlQuery> getStream(DbChangeMeta dbChange, Object testClassInstance) {
        Stream<SqlQuery> fromClasses = streamFromClasses(dbChange.changeSet());
        Stream<SqlQuery> fromSqlQueryGetter = streamFromSqlQueryGetter(dbChange.sqlQueryGetter(), testClassInstance);
        Stream<SqlQuery> fromFile = streamFromFile(dbChange.sqlQueryFiles());
        Stream<SqlQuery> fromStatements = streamFromStatements(dbChange.statements());
        return Stream
                .of(fromClasses, fromSqlQueryGetter, fromFile, fromStatements)
                .flatMap(Function.identity());
    }

    private static Stream<SqlQuery> streamFromSqlQueryGetter(String methodName, Object testClassInstance) {
        if (methodName == null || methodName.isEmpty()) {
            return Stream.empty();
        }
        return ObjectFactory
                .getByMethod(SqlQueryGetter.class, methodName, testClassInstance)
                .map(SqlQueryGetter::get)
                .map(List::stream)
                .orElseGet(Stream::empty);
    }

    private static Stream<SqlQuery> streamFromClasses(List<Class<? extends SqlQuery>> changeSetClasses) {
        if (changeSetClasses == null || changeSetClasses.isEmpty()) {
            return Stream.empty();
        }
        return changeSetClasses
                .stream()
                .map(ObjectFactory::createByDefaultConstructor);
    }

    private static Stream<SqlQuery> streamFromFile(List<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) {
            return Stream.empty();
        }
        return filePaths
                .stream()
                .filter(filePath -> filePath != null && !filePath.isEmpty())
                .map(FileUtils::readFile)
                .flatMap(SqlQueryFactory::splitStatements)
                .map(SqlQueryFactory::deleteComments)
                .map(SqlQueryFactory::deleteLineSeparators)
                .map(sqlContent -> EmptyTemplateSqlQuery.of(() -> sqlContent));
    }

    private static Stream<String> splitStatements(String sqlContent) {
        if (sqlContent == null || sqlContent.isEmpty()) {
            return Stream.empty();
        }
        return Arrays
                .stream(sqlContent.split(";"))
                .filter(queryString ->
                        !queryString.isEmpty())
                .map(queryString -> queryString + ";");
    }

    // Notice: regex symbol "." does not correspond to new line separator
    private static String deleteComments(String sqlContent) {
        return sqlContent.replaceAll("--.*", "");
    }

    private static String deleteLineSeparators(String sqlContent) {
        return sqlContent.replaceAll(System.lineSeparator(), " ").trim();
    }

    private static Stream<SqlQuery> streamFromStatements(List<String> statements) {
        if (statements == null || statements.isEmpty()) {
            return Stream.empty();
        }
        return statements
                .stream()
                .map(statement -> () -> statement);
    }
}
