![](docs/img/DbChange.drawio.png)

---
Easy and declarative way to execute sql queries in JUnit tests.

### Project goals

1. Provide rich API to code sql queries that are executed in tests written on JUnit 5.
2. Simplify sql queries maintaining in code base.
3. Provide library independent of various frameworks (Uses only standard Java library and Junit 5 compile dependency)

### Core concept

There are three annotations:
1. DbChange
2. DbChangeOnce
3. SqlExecutorGetter

#### DbChange
Provide meta information about RDBMS changes before/after EACH test execution in class.

#### DbChangeOnce
Provide meta information about RDBMS changes before/after ALL tests execution in class.

#### SqlExecutorGetter
Set default sql executor for all tests in class. Value in this annotation should be the name of public method in test class that returns instance of `DefaultSqlExecutor`.

*Annotations position in code:*

```java

@ExtendWith(DbChangeExtension.class)
@DbChangeOnce
@SqlExecutorGetter
public class DbChangeUsageTest {
    
    @Test
    @DbChange
    void test() {
    }
}
```

### How to plug library into project

#### Gradle
1. Open to edit `build.gradle.kts` (or `build.gradle` for groovy)
2. Add `Dbchange` dependency to project
```kotlin
dependencies {
    testImplementation "io.github.darrmirr:dbchange:1.0.0"
}
```

#### Maven
1. Open to edit your project `pom.xml`
2. Add `Dbchange` dependency to dependecies section
```xml
    <dependecy>
        <groupId>io.github.darrmirr</groupId>
        <artifactId>dbchange</artifactId>
        <version>1.0.0</version>
        <scope>test</scope>
    </dependecy>
```

### How to use extension
1. (mandatory) Put `@ExtendWith(DbChangeExtension.class)` on test class.
2. (mandatory) Create public method in test class that returns instance of `DefaultSqlExecutor`.
3. (optional) Put `@DbChangeOnce` on test class 
4. (optional) Put `@SqlExecutorGetter` on test class 
5. (optional) Put `@DbChange` on test method 

*Points to notice:* 
- If there are no annotations `@DbChangeOnce` or `@DbChange` in test class then Dbchange library does nothing during test execution.
- If `@SqlExecutorGetter` is not present on test class then it is mandatory to set value `sqlExecutorGetter` in each `@DbChangeOnce` and `@DbChange` annotation.
- If you use `@DbChangeOnce` in test then you have to initialize instance of DataSource class at test class constructor or in static context (for example, using JUnit annotation `@BeforeAll`)

*Simple example:*

```java
@ExtendWith(DbChangeExtension.class)
@DbChangeOnce(sqlQueryFiles = "sql/database_init.sql")
@DbChangeOnce(sqlQueryFiles = "sql/database_destroy.sql", executionPhase = ExecutionPhase.AFTER_ALL)
@SqlExecutorGetter("defaultSqlExecutor")
public class DbChangeUsageTest {
    private DataSource dataSource;
    
    public DbChangeUsageTest() {
        dataSource = // code to create instance of dataSource 
    }

    public SqlExecutor defaultSqlExecutor() {
        return new DefaultSqlExecutor(dataSource);
    }

    @Test
    @DbChange(changeSet = InsertEmployee6Chained.class )
    @DbChange(changeSet = DeleteEmployee6Chained.class , executionPhase = DbChange.ExecutionPhase.AFTER_TEST)
    void changeSetChained() {
        /* code omitted */
    }
}
```

### Sql queries suppliers

There are following sql queries suppliers:
- **statements**
```java
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
    void statements() { /* code omited */ }
```
- **sql query files**
```java
    @Test
    @DbChange(sqlQueryFiles = {"sql/sqlqueryfiles/sqlQueryFilesTest1_init.sql", "sql/sqlqueryfiles/sqlQueryFilesTest2_init.sql"})
    @DbChange(sqlQueryFiles = "sql/sqlqueryfiles/sqlQueryFilesTest_destroy.sql", executionPhase = DbChange.ExecutionPhase.AFTER_TEST)
    void sqlQueryFiles() { /* code omited */ }
```
- **sql query getter**
```java
    @Test
    @DbChange(sqlQueryGetter = "testSqlQueryGetterInit")
    @DbChange(sqlQueryGetter = "testSqlQueryGetterDestroy", executionPhase = DbChange.ExecutionPhase.AFTER_TEST)
    void sqlQueryGetter() { /* code omited */ }

    public SqlQueryGetter testSqlQueryGetterInit() { /* code omited */ }

    public SqlQueryGetter testSqlQueryGetterDestroy() { /* code omited */ }
```
- **changeset**
```java
    @Test
    @DbChange(changeSet = InsertEmployee6Chained.class )
    @DbChange(changeSet = DeleteEmployee6Chained.class , executionPhase = DbChange.ExecutionPhase.AFTER_TEST)
    void changeSetChained() { /* code omited */ }
```
- **@MethodSource** (only for JUnit parameterized test)
```java
    @ParameterizedTest
    @MethodSource("sourceSqlQueryGetterParameterized")
    void sqlQueryGetterParameterized(List<DbChangeMeta> dbChangeMetas, List<Department> expectedDepartments) { /* code omited */ }

    public static Stream<Arguments> sourceSqlQueryGetterParameterized() { /* code omited */ }
```

*Notice:*
- all sql queries suppliers (except `@MethodSource`) are supported by `@DbChange` and `@DbChangeOnce` annotations.


See usage example in `com.github.darrmirr.dbchange.component.DbChangeUsageTest` class.

Read article on [medium.com](https://darrmirr.medium.com/introduction-to-dbchange-junit-extension-7995a5a639e7) for more details

### Minimum requirements:

*Runtime:*
- JRE 8
- JUnit 5.9+ in classpath

*Development:*
- JDK 8
- Gradle 7
