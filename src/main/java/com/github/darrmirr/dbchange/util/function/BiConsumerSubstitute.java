package com.github.darrmirr.dbchange.util.function;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Extension of {@link BiConsumer} function.
 * This function provides capability to replace origin input values by new ones.
 *
 * @param <T> type of first origin input value.
 * @param <U> type of second origin input value.
 */
@FunctionalInterface
public interface BiConsumerSubstitute<T, U> extends BiConsumer<T, U> {

    /**
     * Replace origin input values by new ones.
     * New input values will be used for creation origin one.
     * Therefore, two {@link BiFunction} presents in {@link BiConsumerSubstitute#with(BiFunction, BiFunction)} method signature.
     *
     * @param tBiFunction supplier first origin input value.
     * @param uBiFunction supplier second origin input value
     * @return consumer with new input values.
     * @param <A> type of first new input value.
     * @param <B> type of second new input value.
     */
    default <A, B> BiConsumerSubstitute<A, B> with(BiFunction<A, B, T> tBiFunction, BiFunction<A, B, U> uBiFunction) {
        Objects.requireNonNull(tBiFunction);
        Objects.requireNonNull(uBiFunction);
        return (a, b) -> this.accept(tBiFunction.apply(a, b), uBiFunction.apply(a, b));
    }
}
