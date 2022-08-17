package com.github.darrmirr.dbchange.util.function;

import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.function.Supplier;

@FunctionalInterface
public interface TestInstanceSupplier extends Supplier<Object> {

    static TestInstanceSupplier from(ExtensionContext context) {
        return () -> context
                .getRequiredTestInstances()
                .findInstance(context.getRequiredTestClass())
                .orElseThrow(() -> new IllegalStateException("Test instance is not found : name=" + context.getRequiredTestClass()));
    }
}
