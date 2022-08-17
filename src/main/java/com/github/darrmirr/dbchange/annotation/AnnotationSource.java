package com.github.darrmirr.dbchange.annotation;

import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangesOnce;
import com.github.darrmirr.dbchange.annotation.onmethod.DbChange;
import com.github.darrmirr.dbchange.annotation.onmethod.DbChanges;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Get stream of annotations from {@link ExtensionContext} depends on its source:
 *   - onMethod
 *   - onClass
 */
public final class AnnotationSource {

    private AnnotationSource() {
    }

    /**
     * Get stream of annotations from {@link ExtensionContext} placed on test method.
     *
     * @param context test execution context.
     * @return steam of annotations.
     */
    public static Stream<DbChange> onMethod(ExtensionContext context) {
        List<DbChange> dbChangeList = new ArrayList<>();
        getDbChange(context).ifPresent(dbChangeList::addAll);
        getDbChanges(context).ifPresent(dbChangeList::addAll);
        return dbChangeList.stream();
    }

    /**
     * Get stream of annotations from {@link ExtensionContext} placed on test class.
     *
     * @param context test execution context.
     * @return steam of annotations.
     */
    public static Stream<DbChangeOnce> onClass(ExtensionContext context) {
        List<DbChangeOnce> dbChangeList = new ArrayList<DbChangeOnce>();
        getDbChangeOnce(context).ifPresent(dbChangeList::addAll);
        getDbChangesOnce(context).ifPresent(dbChangeList::addAll);
        return dbChangeList.stream();
    }

    private static Optional<List<DbChange>> getDbChange(ExtensionContext context) {
        return Optional
                .of(context)
                .map(ExtensionContext::getRequiredTestMethod)
                .map(method -> method.getAnnotation(DbChange.class))
                .map(Collections::singletonList);
    }

    private static Optional<List<DbChange>> getDbChanges(ExtensionContext context) {
        return Optional
                .of(context)
                .map(ExtensionContext::getRequiredTestMethod)
                .map(method -> method.getAnnotation(DbChanges.class))
                .map(DbChanges::value)
                .map(Arrays::asList);
    }

    private static Optional<List<DbChangeOnce>> getDbChangeOnce(ExtensionContext context) {
        return Optional
                .of(context)
                .map(ExtensionContext::getRequiredTestClass)
                .map(method -> method.getAnnotation(DbChangeOnce.class))
                .map(Collections::singletonList);
    }

    private static Optional<List<DbChangeOnce>> getDbChangesOnce(ExtensionContext context) {
        return Optional
                .of(context)
                .map(ExtensionContext::getRequiredTestClass)
                .map(method -> method.getAnnotation(DbChangesOnce.class))
                .map(DbChangesOnce::value)
                .map(Arrays::asList);
    }
}
