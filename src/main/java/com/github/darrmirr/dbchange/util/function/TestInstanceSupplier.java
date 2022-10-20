package com.github.darrmirr.dbchange.util.function;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstances;

import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface TestInstanceSupplier extends Supplier<Object> {

    static TestInstanceSupplier from(ExtensionContext context) {
        return () -> MemorizeFunction
                .of(extractOne(context.getRequiredTestInstances()))
                .apply(context.getRequiredTestClass());
    }

    static Function<Class<?>, Object> extractOne(TestInstances testInstances) {
        return requiredTestClass -> testInstances
                .findInstance(requiredTestClass)
                .orElseThrow(() -> new IllegalStateException("Test instance is not found : name=" + requiredTestClass));
    }
}
