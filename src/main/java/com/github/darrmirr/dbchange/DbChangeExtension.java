package com.github.darrmirr.dbchange;

import com.github.darrmirr.dbchange.meta.DbChangeMeta;
import com.github.darrmirr.dbchange.meta.DbChangeMeta.ExecutionPhase;
import com.github.darrmirr.dbchange.meta.DbChangeMetaFactory;
import com.github.darrmirr.dbchange.util.Executor;
import com.github.darrmirr.dbchange.util.ParameterizedArgumentsViewer;
import com.github.darrmirr.dbchange.util.ParameterizedExtensionContext;
import com.github.darrmirr.dbchange.util.function.TestInstanceConsumer;
import com.github.darrmirr.dbchange.util.function.TestInstanceSupplier;
import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;

import static com.github.darrmirr.dbchange.util.function.Functions.CHANGESET_EXTRACTOR;
import static com.github.darrmirr.dbchange.util.function.Functions.SQL_EXECUTOR;

/**
 * Main class of DbChange JUnit extension.
 */
public class DbChangeExtension extends ParameterizedArgumentsViewer implements Extension, BeforeTestExecutionCallback,
        AfterTestExecutionCallback, BeforeAllCallback, AfterAllCallback, TestInstancePostProcessor,
        TestInstancePreDestroyCallback, TestWatcher {
    private static final Logger log = LoggerFactory.getLogger(DbChangeExtension.class);
    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DbChangeExtension.class);
    private static final String DB_CHANGE_BEFORE_ALL = "db_change_before_all";
    private static final String DB_CHANGE_AFTER_ALL = "db_change_after_all";

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        log.debug("{} start work before test execution", DbChangeExtension.class.getName());
        Supplier<List<DbChangeMeta>> dbChangeListSupplier = DbChangeMetaFactory
                .get(context, ExecutionPhase.BEFORE_TEST, DbChangeMeta.Source.ON_METHOD);
        handle(TestInstanceSupplier.from(context), dbChangeListSupplier);
        log.debug("{} finish work before test execution", DbChangeExtension.class.getName());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        log.debug("{} start work after test execution", DbChangeExtension.class.getName());
        Supplier<List<DbChangeMeta>> dbChangeListSupplier = DbChangeMetaFactory
                .get(context, ExecutionPhase.AFTER_TEST, DbChangeMeta.Source.ON_METHOD);
        handle(TestInstanceSupplier.from(context), dbChangeListSupplier);
        log.debug("{} finish work after test execution", DbChangeExtension.class.getName());
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        log.debug("{} start work before all tests execution", DbChangeExtension.class.getName());
        Supplier<List<DbChangeMeta>> dbChangeListSupplier = DbChangeMetaFactory
                .get(context, ExecutionPhase.BEFORE_ALL, DbChangeMeta.Source.ON_CLASS);
        TestInstanceConsumer dbChange2run = testInstance -> handle(() -> testInstance, dbChangeListSupplier);
        rootStore(context).put(DB_CHANGE_BEFORE_ALL, dbChange2run);
        log.debug("put delayed db change to extension store : object={}", dbChange2run);
        log.debug("{} finish work before all tests execution", DbChangeExtension.class.getName());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        log.debug("{} start work before all tests execution", DbChangeExtension.class.getName());
        Supplier<List<DbChangeMeta>> dbChangeListSupplier = DbChangeMetaFactory
                .get(context, ExecutionPhase.AFTER_ALL, DbChangeMeta.Source.ON_CLASS);
        Object testInstance = rootStore(context).remove(DB_CHANGE_AFTER_ALL);
        log.debug("get test instance from extension store : object={}", testInstance);
        handle(() -> testInstance, dbChangeListSupplier);
        log.debug("{} finish work before all tests execution", DbChangeExtension.class.getName());
    }

    @Override
    public void beforeMethodInvocation(List<Object> arguments, ExtensionContext extensionContext) {
        log.debug("{} start work before parameterized test execution", DbChangeExtension.class.getName());
        ParameterizedExtensionContext context = new ParameterizedExtensionContext(extensionContext, arguments);
        Supplier<List<DbChangeMeta>> dbChangeListSupplier = DbChangeMetaFactory
                .get(context, ExecutionPhase.BEFORE_TEST, DbChangeMeta.Source.FROM_PARAMETER);
        handle(TestInstanceSupplier.from(context), dbChangeListSupplier);
        log.debug("{} finish work before parameterized test execution", DbChangeExtension.class.getName());
    }

    @Override
    public void afterMethodInvocation(List<Object> arguments, ExtensionContext extensionContext) {
        log.debug("{} start work after parameterized test execution", DbChangeExtension.class.getName());
        ParameterizedExtensionContext context = new ParameterizedExtensionContext(extensionContext, arguments);
        Supplier<List<DbChangeMeta>> dbChangeListSupplier = DbChangeMetaFactory
                .get(context, ExecutionPhase.AFTER_TEST, DbChangeMeta.Source.FROM_PARAMETER);
        handle(TestInstanceSupplier.from(context), dbChangeListSupplier);
        log.debug("{} finish work after parameterized test execution", DbChangeExtension.class.getName());
    }

    private void handle(TestInstanceSupplier testInstanceSupplier, Supplier<List<DbChangeMeta>> dbChangeListSupplier) {
        List<DbChangeMeta> dbChangeList = dbChangeListSupplier.get();
        for (DbChangeMeta dbChange : dbChangeList) {
            if (dbChange.isChangeSetPresent()) {
                Executor.execute(dbChange)
                        .with(CHANGESET_EXTRACTOR, SQL_EXECUTOR)
                        .accept(dbChange, testInstanceSupplier);
            } else {
                log.warn("Change set is empty : [ object=" + dbChange + "]");
            }
        }
    }

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        TestInstanceConsumer dbChange2run;
        // There are multiply test instances in parallel execution.
        // It requires execute before all only once.
        synchronized (DbChangeExtension.NAMESPACE) {
            dbChange2run = rootStore(context).remove(DB_CHANGE_BEFORE_ALL, TestInstanceConsumer.class);
            log.debug("get delayed db change from extension store : object={}", dbChange2run);
        }
        if (dbChange2run != null) {
            log.debug("execute delayed db change");
            dbChange2run.accept(testInstance);
        }
    }

    @Override
    public void preDestroyTestInstance(ExtensionContext context) {
        Object testInstance = rootStore(context).get(DB_CHANGE_AFTER_ALL);
        if (testInstance == null) {
            testInstance = TestInstanceSupplier.from(context).get();
            rootStore(context).put(DB_CHANGE_AFTER_ALL, testInstance);
            log.debug("put test instance to extension store : object={}", testInstance);
        }
    }

    private ExtensionContext.Store rootStore(ExtensionContext context) {
        return context
                .getRoot()
                .getStore(DbChangeExtension.NAMESPACE);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        log.debug("start finalize db change activities because of test failure");
        afterTestExecution(context);
        log.debug("finish finalize db change activities because of test failure");
    }
}
