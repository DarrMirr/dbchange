package com.github.darrmirr.dbchange.util;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Class represents JUnit extension to supply arguments lists in JUnit ParameterizedTest.
 */
public abstract class ParameterizedArgumentsViewer implements InvocationInterceptor {

    @Override
    public final void interceptTestTemplateMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        List<Object> arguments = invocationContext.getArguments();
        beforeMethodInvocation(arguments, extensionContext);
        try {
            InvocationInterceptor.super.interceptTestTemplateMethod(invocation, invocationContext, extensionContext);
        } finally {
            afterMethodInvocation(arguments, extensionContext);
        }
    }

    /**
     * Get list arguments before test method invocation.
     *
     * @param arguments list of arguments.
     * @param extensionContext JUnit extension context.
     */
    public abstract void beforeMethodInvocation(List<Object> arguments, ExtensionContext extensionContext);

    /**
     * Get list arguments after test method invocation.
     *
     * @param arguments list of arguments.
     * @param extensionContext JUnit extension context.
     */
    public abstract void afterMethodInvocation(List<Object> arguments, ExtensionContext extensionContext);
}
