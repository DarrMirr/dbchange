package com.github.darrmirr.dbchange.util.function;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Simple Memorize function.
 * <br><br>
 * Memorized values are stored in {@link ConcurrentHashMap}.
 * Each stored value wrapped by {@link SoftReference} in order to prevent OutOfMemory error.
 * <br><br>
 * Memorized function support capability to restore values deleted by garbage collector.
 * <br><br>
 * Point to notice:<br>
 *   - Memorization is not executed if input value is null. <br>
 *   - Method {@link Object#hashCode()} is used as key supplier for Map.
 *
 * @param <T> input value type.
 * @param <R> output value type.
 */
@FunctionalInterface
public interface MemorizeFunction<T, R> extends Function<T, R> {

    /**
     * Method to create {@link MemorizeFunction}
     *
     * @param function function to wrap up by memorize one.
     * @return memorize function.
     * @param <T> input value type.
     * @param <R> output value type.
     */
    static <T, R> MemorizeFunction<T, R> of(Function<T, R> function) {
        Objects.requireNonNull(function);
        final Map<Integer, MonoCache<R>> cacheMap = new ConcurrentHashMap<>();
        return input -> {
            if (input == null) {
                return function.apply(input);
            }
            int inputHashCode = input.hashCode();
            MonoCache<R> monoCache = cacheMap.get(inputHashCode);
            if (monoCache != null && monoCache.get() != null) {
                return monoCache.get();
            }
            R output = function.apply(input);
            if (monoCache != null) {
                monoCache.recover(output);
            } else {
                cacheMap.put(inputHashCode, new MonoCache<>(output));
            }
            return output;
        };
    }

    /**
     * Simple cache
     *
     * @param <T> type of cached value.
     */
    class MonoCache<T> {
        private SoftReference<T> monoCache;

        private MonoCache(T t) {
            this.monoCache = new SoftReference<>(t);
        }

        private void recover(T t) {
            monoCache = new SoftReference<>(t);
        }

        private T get() {
            return monoCache.get();
        }
    }
}
