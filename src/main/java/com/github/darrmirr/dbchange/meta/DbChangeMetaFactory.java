package com.github.darrmirr.dbchange.meta;

import com.github.darrmirr.dbchange.annotation.AnnotationSource;
import com.github.darrmirr.dbchange.annotation.onclass.DbChangeOnce;
import com.github.darrmirr.dbchange.annotation.onmethod.DbChange;
import com.github.darrmirr.dbchange.meta.DbChangeMeta.ExecutionPhase;
import com.github.darrmirr.dbchange.util.ParameterizedExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Factory class to gather all RDBMS changes from different sources and put it into one list.
 */
public final class DbChangeMetaFactory {

    private DbChangeMetaFactory() {
    }

    private static Predicate<DbChange> byPhaseMethod(ExecutionPhase currentPhase) {
        return dbChange -> Objects.equals(currentPhase.name(), dbChange.executionPhase().name());
    }

    private static Predicate<DbChangeOnce> byPhaseClass(ExecutionPhase currentPhase) {
        return dbChange -> Objects.equals(currentPhase.name(), dbChange.executionPhase().name());
    }

    private static Predicate<DbChangeMeta> byPhase(ExecutionPhase currentPhase) {
        return dbChange -> dbChange.executionPhase() == currentPhase;
    }

    private static BiFunction<ExtensionContext, ExecutionPhase, List<DbChangeMeta>> get(DbChangeMeta.Source source) {
        switch (source) {
            case ON_METHOD: return (context, currentPhase) -> AnnotationSource
                    .onMethod(context)
                    .filter(byPhaseMethod(currentPhase))
                    .map(DbChangeMetaBuilder::toDbChangeMeta)
                    .collect(Collectors.toList());
            case ON_CLASS: return (context, currentPhase) -> AnnotationSource
                    .onClass(context)
                    .filter(byPhaseClass(currentPhase))
                    .map(DbChangeMetaBuilder::toDbChangeMeta)
                    .collect(Collectors.toList());
            case FROM_PARAMETER: return (context, currentPhase) -> {
                List<DbChangeMeta> dbChangeMetaList = new ArrayList<>();
                if (context instanceof ParameterizedExtensionContext) {
                    ParameterizedExtensionContext parameterizedContext = (ParameterizedExtensionContext) context;
                    for (Object argument : parameterizedContext.getArguments()) {
                        if (argument instanceof Collection<?>) {
                            Collection<?> collection = (Collection<?>) argument;
                            collection
                                    .stream()
                                    .filter(DbChangeMeta.class::isInstance)
                                    .map(DbChangeMeta.class::cast)
                                    .filter(byPhase(currentPhase))
                                    .forEach(dbChangeMetaList::add);
                        }
                    }
                }
                return dbChangeMetaList;
            };
        }
        throw new IllegalStateException("Unknown source : source=" + source);
    }

    public static Supplier<List<DbChangeMeta>> get(ExtensionContext context, ExecutionPhase phase, DbChangeMeta.Source source) {
        return () -> get(source).apply(context, phase);
    }
}
