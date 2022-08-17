package com.github.darrmirr.dbchange.component;

import com.github.darrmirr.dbchange.DbChangeExtension;
import com.github.darrmirr.dbchange.annotation.SqlExecutorGetter;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce.ExecutionPhase;
import com.github.darrmirr.dbchange.annotation.onmethod.DbChange;
import com.github.darrmirr.dbchange.changeset.ChangeSetItem;
import com.github.darrmirr.dbchange.meta.DbChangeMeta;
import com.github.darrmirr.dbchange.sql.Id;
import com.github.darrmirr.dbchange.sql.SqlUtils;
import com.github.darrmirr.dbchange.sql.executor.DefaultSqlExecutor;
import com.github.darrmirr.dbchange.sql.executor.SqlExecutor;
import com.github.darrmirr.dbchange.sql.mapper.ResultMapperFactory;
import com.github.darrmirr.dbchange.sql.model.Department;
import com.github.darrmirr.dbchange.sql.model.Employee;
import com.github.darrmirr.dbchange.sql.model.Occupation;
import com.github.darrmirr.dbchange.sql.query.JavaQueryTemplates;
import com.github.darrmirr.dbchange.sql.query.getter.SqlQueryGetter;
import com.github.darrmirr.dbchange.sql.query.template.DepartmentQuery;
import com.github.darrmirr.dbchange.sql.query.template.chained.*;
import com.github.darrmirr.dbchange.sql.query.template.insert.InsertDepartment11;
import com.github.darrmirr.dbchange.sql.query.template.insert.InsertEmployee7;
import com.github.darrmirr.dbchange.sql.query.template.insert.InsertOccupation5;
import com.github.darrmirr.dbchange.sql.query.template.specific.*;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Class contains usage examples.
 */
@ExtendWith(DbChangeExtension.class)
@DbChangeOnce(sqlQueryFiles = "sql/database_init.sql")
@DbChangeOnce(sqlQueryFiles = "sql/database_destroy.sql", executionPhase = ExecutionPhase.AFTER_ALL)
@SqlExecutorGetter("defaultSqlExecutor")
public class DbChangeUsageTest {

// -----------     initialization block     -----------

    private static final DataSource DATA_SOURCE;
    private final SqlUtils sqlUtils = new SqlUtils(DATA_SOURCE);

    static {
        try {
            DATA_SOURCE = loadDataSource();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static DataSource loadDataSource() throws IOException {
        Properties properties = new Properties();
        try (InputStream io = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties")) {
            properties.load(io);
        }

        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL(properties.getProperty("datasource.url.schema.usage"));
        jdbcDataSource.setUser(properties.getProperty("datasource.username"));
        jdbcDataSource.setPassword(properties.getProperty("datasource.password"));

        return jdbcDataSource;
    }

    public SqlExecutor defaultSqlExecutor() {
        return new DefaultSqlExecutor(DATA_SOURCE);
    }

// -----------     sqlQueryGetter test block     -----------

    @Test
    @DbChange(sqlQueryGetter = "testSqlQueryGetterInit")
    @DbChange(sqlQueryGetter = "testSqlQueryGetterDestroy", executionPhase = DbChange.ExecutionPhase.AFTER_TEST)
    void sqlQueryGetter() throws SQLException {
        String sql = "select * from department order by id";
        List<Department> departments = sqlUtils.select(sql, ResultMapperFactory.department());

        assertThat(departments, hasSize(2));
        departments.forEach(department -> {
            assertThat(department.getId(), anyOf(equalTo(Id.DEP_1), equalTo(Id.DEP_2)));
            assertThat(department.getName(), anyOf(equalTo("dep" + Id.DEP_1), equalTo("dep" + Id.DEP_2)));
        });
    }

    public SqlQueryGetter testSqlQueryGetterInit() {
        return () -> Arrays.asList(
                () -> format(JavaQueryTemplates.DEPARTMENT_INSERT, Id.DEP_1, Id.DEP_1),
                () -> format(JavaQueryTemplates.DEPARTMENT_INSERT, Id.DEP_2, Id.DEP_2)
        );
    }

    public static SqlQueryGetter testSqlQueryGetterDestroy() {
        return () -> Collections.singletonList(
                () -> format(JavaQueryTemplates.DEPARTMENT_DELETE_TWO, Id.DEP_1, Id.DEP_2)
        );
    }

// -----------     sqlQueryGetter parameterized test block     -----------

    @ParameterizedTest
    @MethodSource("sourceSqlQueryGetterParameterized")
    void sqlQueryGetterParameterized(List<DbChangeMeta> dbChangeMetas, List<Department> expectedDepartments) throws SQLException {
        String sql = "select * from department order by id";
        List<Department> departments = sqlUtils.select(sql, ResultMapperFactory.department());

        assertThat(departments, hasSize(2));
        assertThat(departments.toString(), equalTo(expectedDepartments.toString()));
    }

    public static Stream<Arguments> sourceSqlQueryGetterParameterized() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList(
                                new DbChangeMeta().setSqlQueryGetter("testSqlQueryGetterParameterizedInit1").setExecutionPhase(DbChangeMeta.ExecutionPhase.BEFORE_TEST),
                                new DbChangeMeta().setSqlQueryGetter("testSqlQueryGetterParameterizedDestroy1").setExecutionPhase(DbChangeMeta.ExecutionPhase.AFTER_TEST)
                        ),
                        Arrays.asList(new Department(Id.DEP_3, "dep" + Id.DEP_3), new Department(Id.DEP_4, "dep" + Id.DEP_4))
                ),
                Arguments.of(
                        Arrays.asList(
                                new DbChangeMeta().setSqlQueryGetter("testSqlQueryGetterParameterizedInit2").setExecutionPhase(DbChangeMeta.ExecutionPhase.BEFORE_TEST),
                                new DbChangeMeta().setSqlQueryGetter("testSqlQueryGetterParameterizedDestroy2").setExecutionPhase(DbChangeMeta.ExecutionPhase.AFTER_TEST)
                        ),
                        Arrays.asList(new Department(Id.DEP_5, "dep" + Id.DEP_5), new Department(Id.DEP_6, "dep" + Id.DEP_6))
                )
        );
    }

    // It is possible to mix different types of sql query declaration
    public SqlQueryGetter testSqlQueryGetterParameterizedInit1() {
        return () -> Arrays.asList(
                () -> format(JavaQueryTemplates.DEPARTMENT_INSERT, Id.DEP_3, Id.DEP_3),
                DepartmentQuery
                        .Insert
                        .withParam(DepartmentQuery.PARAM_ID, Id.DEP_4)
                        .withParam(DepartmentQuery.PARAM_NAME, "dep" + Id.DEP_4)
                        .build()
        );
    }

    public static SqlQueryGetter testSqlQueryGetterParameterizedDestroy1() {
        return () -> Collections.singletonList(
                () -> format(JavaQueryTemplates.DEPARTMENT_DELETE_TWO, Id.DEP_3, Id.DEP_4)
        );
    }

    public SqlQueryGetter testSqlQueryGetterParameterizedInit2() {
        return () -> Arrays.asList(
                () -> format(JavaQueryTemplates.DEPARTMENT_INSERT, Id.DEP_5, Id.DEP_5),
                DepartmentQuery
                        .Insert
                        .withParam(DepartmentQuery.PARAM_ID, Id.DEP_6)
                        .withParam(DepartmentQuery.PARAM_NAME, "dep" + Id.DEP_6)
                        .build()
        );
    }

    public static SqlQueryGetter testSqlQueryGetterParameterizedDestroy2() {
        return () -> Arrays.asList(
                DepartmentQuery.Delete.withParam(DepartmentQuery.PARAM_ID, Id.DEP_5).build(),
                DepartmentQuery.Delete.withParam(DepartmentQuery.PARAM_ID, Id.DEP_6).build()
        );
    }

// -----------     sqlQueryFiles test block     -----------

    @Test
    @DbChange(sqlQueryFiles = {"sql/sqlqueryfiles/sqlQueryFilesTest1_init.sql", "sql/sqlqueryfiles/sqlQueryFilesTest2_init.sql"})
    @DbChange(sqlQueryFiles = "sql/sqlqueryfiles/sqlQueryFilesTest_destroy.sql", executionPhase = DbChange.ExecutionPhase.AFTER_TEST)
    void sqlQueryFiles() throws SQLException {
        String selectDep = "select * from department where id = " + Id.DEP_7;
        String selectOcc = "select * from occupation where id = " + Id.OCC_1;
        String selectEmp = format("select * from employee where id in (%s, %s)", Id.EMP_1, Id.EMP_2);

        List<Department> departments = sqlUtils.select(selectDep, ResultMapperFactory.department());
        List<Occupation> occupations = sqlUtils.select(selectOcc, ResultMapperFactory.occupation());
        List<Employee> employees = sqlUtils.select(selectEmp, ResultMapperFactory.employee());

        assertThat(departments, hasSize(1));
        assertThat(departments.get(0).getId(), equalTo(Id.DEP_7));
        assertThat(departments.get(0).getName(), equalTo("dep" + Id.DEP_7));

        assertThat(occupations, hasSize(1));
        assertThat(occupations.get(0).getId(), equalTo(Id.OCC_1));
        assertThat(occupations.get(0).getName(), equalTo("occ" + Id.OCC_1));

        assertThat(employees, hasSize(2));
        employees.forEach(employee -> {
            assertThat(employee.getId(), anyOf(equalTo(Id.EMP_1), equalTo(Id.EMP_2)));
            assertThat(employee.getDepartmentId(), equalTo(Id.DEP_7));
            assertThat(employee.getOccupationId(), equalTo(Id.OCC_1));
            assertThat(employee.getFirstName(), anyOf(equalTo("Ivan"), equalTo("Petr")));
            assertThat(employee.getLastName(), anyOf(equalTo("Ivanov"), equalTo("Petrov")));
        });
    }

// -----------     sqlQueryFiles parameterized test block     -----------

    @ParameterizedTest
    @MethodSource("sourceSqlQueryFilesParameterized")
    void sqlQueryFilesParameterized(List<DbChangeMeta> dbChangeMetas, String[] expectedResults) throws SQLException {
        String selectDep = "select * from department where id = " + Id.DEP_8;
        String selectOcc = "select * from occupation where id = " + Id.OCC_2;
        String selectEmp = "select * from employee where id = " + expectedResults[0];

        List<Department> departments = sqlUtils.select(selectDep, ResultMapperFactory.department());
        List<Occupation> occupations = sqlUtils.select(selectOcc, ResultMapperFactory.occupation());
        List<Employee> employees = sqlUtils.select(selectEmp, ResultMapperFactory.employee());

        assertThat(departments, hasSize(1));
        assertThat(departments.get(0).getId(), equalTo(Id.DEP_8));
        assertThat(departments.get(0).getName(), equalTo("dep" + Id.DEP_8));

        assertThat(occupations, hasSize(1));
        assertThat(occupations.get(0).getId(), equalTo(Id.OCC_2));
        assertThat(occupations.get(0).getName(), equalTo("occ" + Id.OCC_2));

        assertThat(employees, hasSize(1));
        Employee employee = employees.get(0);
        assertThat(employee.getId(), equalTo(Integer.valueOf(expectedResults[0])));
        assertThat(employee.getDepartmentId(), equalTo(Id.DEP_8));
        assertThat(employee.getOccupationId(), equalTo(Id.OCC_2));
        assertThat(employee.getFirstName(), equalTo(expectedResults[1]));
        assertThat(employee.getLastName(), equalTo(expectedResults[2]));

    }

    public static Stream<Arguments> sourceSqlQueryFilesParameterized() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList(
                                new DbChangeMeta()
                                        .setSqlQueryFiles("sql/sqlqueryfiles/sqlQueryFilesTestParameterized1_init.sql")
                                        .setExecutionPhase(DbChangeMeta.ExecutionPhase.BEFORE_TEST),
                                new DbChangeMeta()
                                        .setSqlQueryFiles("sql/sqlqueryfiles/sqlQueryFilesTestParameterized1_destroy.sql")
                                        .setExecutionPhase(DbChangeMeta.ExecutionPhase.AFTER_TEST)
                        ),
                        new String[]{Id.EMP_3.toString(), "Ivan", "Ivanov"}
                ),
                Arguments.of(
                        Arrays.asList(
                                new DbChangeMeta()
                                        .setSqlQueryFiles("sql/sqlqueryfiles/sqlQueryFilesTestParameterized2_init.sql")
                                        .setExecutionPhase(DbChangeMeta.ExecutionPhase.BEFORE_TEST),
                                new DbChangeMeta()
                                        .setSqlQueryFiles("sql/sqlqueryfiles/sqlQueryFilesTestParameterized2_destroy.sql")
                                        .setExecutionPhase(DbChangeMeta.ExecutionPhase.AFTER_TEST)
                        ),
                        new String[]{Id.EMP_4.toString(), "Petr", "Petrov"}
                )
        );
    }

// -----------     changeSet test block (without chained queries)     -----------

    @Test
    @DbChange(changeSet = {InsertDepartment9.class, InsertOccupation3.class, InsertEmployee5.class})
    @DbChange(changeSet = {DeleteEmployee5.class, DeleteOccupation3.class, DeleteDepartment9.class}, executionPhase = DbChange.ExecutionPhase.AFTER_TEST)
    void changeSet() throws SQLException {
        String selectDep = "select * from department where id = " + Id.DEP_9;
        String selectOcc = "select * from occupation where id = " + Id.OCC_3;
        String selectEmp = "select * from employee where id = " + Id.EMP_5;

        List<Department> departments = sqlUtils.select(selectDep, ResultMapperFactory.department());
        List<Occupation> occupations = sqlUtils.select(selectOcc, ResultMapperFactory.occupation());
        List<Employee> employees = sqlUtils.select(selectEmp, ResultMapperFactory.employee());

        assertThat(departments, hasSize(1));
        assertThat(departments.get(0).getId(), equalTo(Id.DEP_9));
        assertThat(departments.get(0).getName(), equalTo("default_department_name"));

        assertThat(occupations, hasSize(1));
        assertThat(occupations.get(0).getId(), equalTo(Id.OCC_3));
        assertThat(occupations.get(0).getName(), equalTo("default_occupation_name"));

        assertThat(employees, hasSize(1));
        Employee employee = employees.get(0);
        assertThat(employee.getId(), equalTo(Id.EMP_5));
        assertThat(employee.getDepartmentId(), equalTo(Id.DEP_9));
        assertThat(employee.getOccupationId(), equalTo(Id.OCC_3));
        assertThat(employee.getFirstName(), equalTo("default_employee_first_name"));
        assertThat(employee.getLastName(), equalTo("default_employee_last_name"));
    }

// -----------     changeSet test block (the same as previous one but with chained queries)     -----------

    @Test
    @DbChange(changeSet = InsertEmployee6Chained.class)
    @DbChange(changeSet = DeleteEmployee6Chained.class, executionPhase = DbChange.ExecutionPhase.AFTER_TEST)
    void changeSetChained() throws SQLException {
        String selectDep = "select * from department where id = " + Id.DEP_10;
        String selectOcc = "select * from occupation where id = " + Id.OCC_4;
        String selectEmp = "select * from employee where id = " + Id.EMP_6;

        List<Department> departments = sqlUtils.select(selectDep, ResultMapperFactory.department());
        List<Occupation> occupations = sqlUtils.select(selectOcc, ResultMapperFactory.occupation());
        List<Employee> employees = sqlUtils.select(selectEmp, ResultMapperFactory.employee());

        assertThat(departments, hasSize(1));
        assertThat(departments.get(0).getId(), equalTo(Id.DEP_10));
        assertThat(departments.get(0).getName(), equalTo("default_department_name"));

        assertThat(occupations, hasSize(1));
        assertThat(occupations.get(0).getId(), equalTo(Id.OCC_4));
        assertThat(occupations.get(0).getName(), equalTo("default_occupation_name"));

        assertThat(employees, hasSize(1));
        Employee employee = employees.get(0);
        assertThat(employee.getId(), equalTo(Id.EMP_6));
        assertThat(employee.getDepartmentId(), equalTo(Id.DEP_10));
        assertThat(employee.getOccupationId(), equalTo(Id.OCC_4));
        assertThat(employee.getFirstName(), equalTo("default_employee_first_name"));
        assertThat(employee.getLastName(), equalTo("default_employee_last_name"));
    }

// -----------     changeSet test block (with InsertSqlQuery)     -----------

    @Test
    @DbChange(changeSet = {InsertDepartment11.class, InsertOccupation5.class, InsertEmployee7.class})
    @DbChange(changeSet = {DeleteEmployee7.class, DeleteOccupation5.class, DeleteDepartment11.class}, executionPhase = DbChange.ExecutionPhase.AFTER_TEST)
    void changeSetInsertSqlQuery() throws SQLException {
        String selectDep = "select * from department where id = " + Id.DEP_11;
        String selectOcc = "select * from occupation where id = " + Id.OCC_5;
        String selectEmp = "select * from employee where id = " + Id.EMP_7;

        List<Department> departments = sqlUtils.select(selectDep, ResultMapperFactory.department());
        List<Occupation> occupations = sqlUtils.select(selectOcc, ResultMapperFactory.occupation());
        List<Employee> employees = sqlUtils.select(selectEmp, ResultMapperFactory.employee());

        assertThat(departments, hasSize(1));
        assertThat(departments.get(0).getId(), equalTo(Id.DEP_11));
        assertThat(departments.get(0).getName(), equalTo("department for test insert sql query class"));

        assertThat(occupations, hasSize(1));
        assertThat(occupations.get(0).getId(), equalTo(Id.OCC_5));
        assertThat(occupations.get(0).getName(), equalTo("occupation for test insert sql query class"));

        assertThat(employees, hasSize(1));
        Employee employee = employees.get(0);
        assertThat(employee.getId(), equalTo(Id.EMP_7));
        assertThat(employee.getDepartmentId(), equalTo(Id.DEP_11));
        assertThat(employee.getOccupationId(), equalTo(Id.OCC_5));
        assertThat(employee.getFirstName(), nullValue());
        assertThat(employee.getLastName(), nullValue());
    }

// -----------     changeSet parameterized test block      -----------

    @ParameterizedTest
    @MethodSource("sourceChangeSetParameterized")
    void changeSetParameterized(List<DbChangeMeta> dbChangeMetas, Integer[] ids) throws SQLException {
        String selectDep = "select * from department where id = " + ids[2];
        String selectOcc = "select * from occupation where id = " + ids[1];
        String selectEmp = "select * from employee where id = " + ids[0];

        List<Department> departments = sqlUtils.select(selectDep, ResultMapperFactory.department());
        List<Occupation> occupations = sqlUtils.select(selectOcc, ResultMapperFactory.occupation());
        List<Employee> employees = sqlUtils.select(selectEmp, ResultMapperFactory.employee());

        assertThat(departments, hasSize(1));
        assertThat(departments.get(0).getId(), equalTo(ids[2]));

        assertThat(occupations, hasSize(1));
        assertThat(occupations.get(0).getId(), equalTo(ids[1]));

        assertThat(employees, hasSize(1));
        Employee employee = employees.get(0);
        assertThat(employee.getId(), equalTo(ids[0]));
        assertThat(employee.getDepartmentId(), equalTo(ids[2]));
        assertThat(employee.getOccupationId(), equalTo(ids[1]));
    }

    public static Stream<Arguments> sourceChangeSetParameterized() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList(
                                new DbChangeMeta()
                                        .setChangeSet(InsertEmployee8Chained.class)
                                        .setExecutionPhase(DbChangeMeta.ExecutionPhase.BEFORE_TEST),
                                new DbChangeMeta()
                                        .setChangeSet(DeleteEmployee8Chained.class)
                                        .setExecutionPhase(DbChangeMeta.ExecutionPhase.AFTER_TEST)
                        ),
                        new Integer[]{Id.EMP_8, Id.OCC_6, Id.DEP_12}
                ),
                Arguments.of(
                        Arrays.asList(
                                new DbChangeMeta()
                                        .setChangeSet(InsertEmployee9Chained.class)
                                        .setExecutionPhase(DbChangeMeta.ExecutionPhase.BEFORE_TEST),
                                new DbChangeMeta()
                                        .setChangeSet(DeleteEmployee9Chained.class)
                                        .setExecutionPhase(DbChangeMeta.ExecutionPhase.AFTER_TEST)
                        ),
                        new Integer[]{Id.EMP_9, Id.OCC_7, Id.DEP_13}
                )
        );
    }

// -----------     statements test block      -----------

    @Test
    @DbChange(statements = {
            "insert into department(id, name) values (14, 'dep14');",
            "insert into occupation(id, name) values (8, 'occ8');",
            "insert into employee(id, department_id, occupation_id, first_name, last_name) values (10, 14, 8, 'Ivan', 'Ivanov')"
    })
    @DbChange(statements = {
            "delete from employee where id = 10;",
            "delete from occupation where id = 8;",
            "delete from department where id = 14;"
    }, executionPhase = DbChange.ExecutionPhase.AFTER_TEST)
    void statements() throws SQLException {
        String selectDep = "select * from department where id = " + Id.DEP_14;
        String selectOcc = "select * from occupation where id = " + Id.OCC_8;
        String selectEmp = "select * from employee where id = " + Id.EMP_10;

        List<Department> departments = sqlUtils.select(selectDep, ResultMapperFactory.department());
        List<Occupation> occupations = sqlUtils.select(selectOcc, ResultMapperFactory.occupation());
        List<Employee> employees = sqlUtils.select(selectEmp, ResultMapperFactory.employee());

        assertThat(departments, hasSize(1));
        assertThat(departments.get(0).getId(), equalTo(Id.DEP_14));
        assertThat(departments.get(0).getName(), equalTo("dep14"));

        assertThat(occupations, hasSize(1));
        assertThat(occupations.get(0).getId(), equalTo(Id.OCC_8));
        assertThat(occupations.get(0).getName(), equalTo("occ8"));

        assertThat(employees, hasSize(1));
        Employee employee = employees.get(0);
        assertThat(employee.getId(), equalTo(Id.EMP_10));
        assertThat(employee.getDepartmentId(), equalTo(Id.DEP_14));
        assertThat(employee.getOccupationId(), equalTo(Id.OCC_8));
        assertThat(employee.getFirstName(), equalTo("Ivan"));
        assertThat(employee.getLastName(), equalTo("Ivanov"));
    }

// -----------     statements parameterized test block      -----------

    @ParameterizedTest
    @MethodSource("sourceStatementsParameterized")
    void statementsParameterized(List<DbChangeMeta> dbChangeMetas, Integer[] ids, String[] firstLastNames) throws SQLException {
        String selectDep = "select * from department where id = " + ids[2];
        String selectOcc = "select * from occupation where id = " + ids[1];
        String selectEmp = "select * from employee where id = " + ids[0];

        List<Department> departments = sqlUtils.select(selectDep, ResultMapperFactory.department());
        List<Occupation> occupations = sqlUtils.select(selectOcc, ResultMapperFactory.occupation());
        List<Employee> employees = sqlUtils.select(selectEmp, ResultMapperFactory.employee());

        assertThat(departments, hasSize(1));
        assertThat(departments.get(0).getId(), equalTo(ids[2]));
        assertThat(departments.get(0).getName(), equalTo("dep" + ids[2]));

        assertThat(occupations, hasSize(1));
        assertThat(occupations.get(0).getId(), equalTo(ids[1]));
        assertThat(occupations.get(0).getName(), equalTo("occ" + ids[1]));

        assertThat(employees, hasSize(1));
        Employee employee = employees.get(0);
        assertThat(employee.getId(), equalTo(ids[0]));
        assertThat(employee.getDepartmentId(), equalTo(ids[2]));
        assertThat(employee.getOccupationId(), equalTo(ids[1]));
        assertThat(employee.getFirstName(), equalTo(firstLastNames[0]));
        assertThat(employee.getLastName(), equalTo(firstLastNames[1]));
    }

    public static Stream<Arguments> sourceStatementsParameterized() {
        return Stream.of(
                Arguments.of(
                        Arrays.asList(
                                new DbChangeMeta()
                                        .setStatements(Arrays.asList(
                                                "insert into department(id, name) values (15, 'dep15');",
                                                "insert into occupation(id, name) values (9, 'occ9');",
                                                "insert into employee(id, department_id, occupation_id, first_name, last_name) values (11, 15, 9, 'Ivan', 'Ivanov')"
                                        ))
                                        .setExecutionPhase(DbChangeMeta.ExecutionPhase.BEFORE_TEST),
                                new DbChangeMeta()
                                        .setStatements(Arrays.asList(
                                                "delete from employee where id = 11;",
                                                "delete from occupation where id = 9;",
                                                "delete from department where id = 15"
                                        ))
                                        .setExecutionPhase(DbChangeMeta.ExecutionPhase.AFTER_TEST)
                        ),
                        new Integer[]{Id.EMP_11, Id.OCC_9, Id.DEP_15},
                        new String[]{"Ivan", "Ivanov"}
                ),
                Arguments.of(
                        Arrays.asList(
                                new DbChangeMeta()
                                        .setStatements(Arrays.asList(
                                                "insert into department(id, name) values (16, 'dep16');",
                                                "insert into occupation(id, name) values (10, 'occ10');",
                                                "insert into employee(id, department_id, occupation_id, first_name, last_name) values (12, 16, 10, 'Petr', 'Petrov')"
                                        ))
                                        .setExecutionPhase(DbChangeMeta.ExecutionPhase.BEFORE_TEST),
                                new DbChangeMeta()
                                        .setStatements(Arrays.asList(
                                                "delete from employee where id = 12;",
                                                "delete from occupation where id = 10;",
                                                "delete from department where id = 16;"
                                        ))
                                        .setExecutionPhase(DbChangeMeta.ExecutionPhase.AFTER_TEST)
                        ),
                        new Integer[]{Id.EMP_12, Id.OCC_10, Id.DEP_16},
                        new String[]{"Petr", "Petrov"}
                )
        );
    }

// -----------     all sql queries suppliers test block      -----------

    @Test
    // order of supplier could be various, but suppliers are handled by DbChange at following order:
    // changeSet -> sqlQueryGetter -> sqlQueryFiles -> statements
    @DbChange(
            changeSet = InsertDepartment17.class,
            sqlQueryGetter = "testAllSqlQueriesSuppliersInit",
            sqlQueryFiles = "sql/sqlqueryfiles/allSqlQueriesSuppliers_init.sql",
            statements = "insert into employee(id, department_id, occupation_id, first_name, last_name) values (14, 17, 11, 'Petr', 'Petrov');"
    )
    @DbChange(
            changeSet = DeleteEmployee14.class,
            sqlQueryGetter = "testAllSqlQueriesSuppliersDestroy",
            sqlQueryFiles = "sql/sqlqueryfiles/allSqlQueriesSuppliers_destroy.sql",
            statements = "delete from department where id = 17;",
            executionPhase = DbChange.ExecutionPhase.AFTER_TEST
    )
    void allSqlQueriesSuppliers() throws SQLException {
        String selectDep = "select * from department where id = " + Id.DEP_17;
        String selectOcc = "select * from occupation where id = " + Id.OCC_11;
        String selectEmp = format("select * from employee where id in (%s, %s)", Id.EMP_13, Id.EMP_14);

        List<Department> departments = sqlUtils.select(selectDep, ResultMapperFactory.department());
        List<Occupation> occupations = sqlUtils.select(selectOcc, ResultMapperFactory.occupation());
        List<Employee> employees = sqlUtils.select(selectEmp, ResultMapperFactory.employee());

        assertThat(departments, hasSize(1));
        assertThat(departments.get(0).getId(), equalTo(Id.DEP_17));
        assertThat(departments.get(0).getName(), equalTo("default_department_name"));

        assertThat(occupations, hasSize(1));
        assertThat(occupations.get(0).getId(), equalTo(Id.OCC_11));
        assertThat(occupations.get(0).getName(), equalTo("occ" + Id.OCC_11));

        assertThat(employees, hasSize(2));
        employees.forEach(employee -> {
            assertThat(employee.getId(), anyOf(equalTo(Id.EMP_13), equalTo(Id.EMP_14)));
            assertThat(employee.getDepartmentId(), equalTo(Id.DEP_17));
            assertThat(employee.getOccupationId(), equalTo(Id.OCC_11));
            assertThat(employee.getFirstName(), anyOf(equalTo("Ivan"), equalTo("Petr")));
            assertThat(employee.getLastName(), anyOf(equalTo("Ivanov"), equalTo("Petrov")));
        });
    }

    public SqlQueryGetter testAllSqlQueriesSuppliersInit() {
        return () -> Collections.singletonList(() -> "insert into occupation(id, name) values (11, 'occ11');");
    }

    public SqlQueryGetter testAllSqlQueriesSuppliersDestroy() {
        return () -> Collections.singletonList(() -> "delete from employee where id = 13;");
    }

// -----------     override default sqlExecutor test block      -----------

    @Test
    @DbChange(statements = "insert into department(id, name) values(18, 'dep18');")
    @DbChange(statements = "insert into department(id, name) values(19, 'dep19');", sqlExecutorGetter = "anotherSqlExecutor")
    @DbChange(statements = "delete from department where id = 18;", executionPhase = DbChange.ExecutionPhase.AFTER_TEST)
    void overrideDefaultSqlExecutor() throws SQLException {
        String sql = "select * from department order by id";
        List<Department> departments = sqlUtils.select(sql, ResultMapperFactory.department());

        assertThat(departments, hasSize(1));
        assertThat(departments.get(0).getId(), equalTo(Id.DEP_18));
        assertThat(departments.get(0).getName(), equalTo("dep" + Id.DEP_18));

        ArgumentCaptor<List> captorList = ArgumentCaptor.forClass(List.class);
        Mockito.verify(mockSqlExecutor, Mockito.times(1)).execute(captorList.capture());
        List values = captorList.getAllValues().get(0);
        assertThat(values.size(), is(1));
        assertThat(values.get(0), instanceOf(ChangeSetItem.class));
        assertThat(((ChangeSetItem) values.get(0)).getQuery(), equalTo("insert into department(id, name) values(19, 'dep19');"));
    }

    private final SqlExecutor mockSqlExecutor = Mockito.mock(SqlExecutor.class);

    public SqlExecutor anotherSqlExecutor() {
        return mockSqlExecutor;
    }
}
