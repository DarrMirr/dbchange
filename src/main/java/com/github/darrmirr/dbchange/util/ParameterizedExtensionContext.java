package com.github.darrmirr.dbchange.util;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExecutableInvoker;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstances;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * ExtensionContext that provide list of arguments in JUnit ParameterizedTest.
 */
public final class ParameterizedExtensionContext implements ExtensionContext {
    private final ExtensionContext context;
    private final List<Object> arguments;

    public ParameterizedExtensionContext(ExtensionContext context, List<Object> arguments) {
        this.context = context;
        this.arguments = arguments;
    }

    @Override
    public Optional<ExtensionContext> getParent() {
        return context.getParent();
    }

    @Override
    public ExtensionContext getRoot() {
        return context.getRoot();
    }

    @Override
    public String getUniqueId() {
        return context.getUniqueId();
    }

    @Override
    public String getDisplayName() {
        return context.getDisplayName();
    }

    @Override
    public Set<String> getTags() {
        return context.getTags();
    }

    @Override
    public Optional<AnnotatedElement> getElement() {
        return context.getElement();
    }

    @Override
    public Optional<Class<?>> getTestClass() {
        return context.getTestClass();
    }

    @Override
    public Optional<TestInstance.Lifecycle> getTestInstanceLifecycle() {
        return context.getTestInstanceLifecycle();
    }

    @Override
    public Optional<Object> getTestInstance() {
        return context.getTestInstance();
    }

    @Override
    public Optional<TestInstances> getTestInstances() {
        return context.getTestInstances();
    }

    @Override
    public Optional<Method> getTestMethod() {
        return context.getTestMethod();
    }

    @Override
    public Optional<Throwable> getExecutionException() {
        return context.getExecutionException();
    }

    @Override
    public Optional<String> getConfigurationParameter(String key) {
        return context.getConfigurationParameter(key);
    }

    @Override
    public <T> Optional<T> getConfigurationParameter(String key, Function<String, T> transformer) {
        return context.getConfigurationParameter(key, transformer);
    }

    @Override
    public void publishReportEntry(Map<String, String> map) {
        context.publishReportEntry(map);
    }

    @Override
    public Store getStore(Namespace namespace) {
        return context.getStore(namespace);
    }

    @Override
    public ExecutionMode getExecutionMode() {
        return context.getExecutionMode();
    }

    @Override
    public ExecutableInvoker getExecutableInvoker() {
        return context.getExecutableInvoker();
    }

    public List<Object> getArguments() {
        return arguments;
    }
}
