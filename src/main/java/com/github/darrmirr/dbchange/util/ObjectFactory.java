package com.github.darrmirr.dbchange.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Utility class dedicated to create different kind of objects.
 */
public final class ObjectFactory {

    private ObjectFactory() {
    }

    /**
     * Get object from method.
     *
     * @param objectClass class of returned object from method.
     * @param methodName method name to invoke.
     * @param testClassInstance class instance to invoke method.
     * @param <T> type of returned object.
     * @return object retrieved after method invocation.
     */
    public static <T> Optional<T> getByMethod(Class<T> objectClass, String methodName, Object testClassInstance) {
        try {
            Method method = testClassInstance
                    .getClass()
                    .getMethod(methodName);
            method.setAccessible(true);
            Object returnedObject = method.invoke(testClassInstance);
            return Optional
                    .ofNullable(returnedObject)
                    .filter(returnedObj -> objectClass.isAssignableFrom(returnedObj.getClass()))
                    .map(objectClass::cast);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            String message = String.format("Error to get object from method : method_name=%s, object_class=%s", methodName, objectClass);
            throw new IllegalStateException(message, e);
        }
    }

    /**
     * Create object by default constructor (constructor without parameters)
     *
     * @param objectClass class of object to create.
     * @param <T> type of object to create.
     * @return object created by default constructor.
     */
    public static <T> T createByDefaultConstructor(Class<T> objectClass) {
        try {
            return objectClass
                    .getConstructor()
                    .newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }
}
