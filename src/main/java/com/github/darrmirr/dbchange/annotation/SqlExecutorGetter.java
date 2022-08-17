package com.github.darrmirr.dbchange.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Set default sql executor for all tests in class.
 * SqlExecutor value could be overridden by annotation on method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface SqlExecutorGetter {

    /**
     * Method name defined in test class
     * that returns instance of {@link com.github.darrmirr.dbchange.sql.executor.SqlExecutor} class.
     * Method could have static modifier, but it allows omitting static modifier and make method as instance one.
     *
     * @return method name defined in test class.
     */
    String value();
}
