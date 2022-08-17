package com.github.darrmirr.dbchange.annotation.onclass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation used only for {@link java.lang.annotation.Repeatable}
 * and therefore should not be used directly.
 *
 * Use @DbChange instead.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DbChangesOnce {

    DbChangeOnce[] value();
}
