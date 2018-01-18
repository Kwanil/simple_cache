package application.cache;


import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a supplier of results.
 * Indicates the source data that should apply to the cache values.
 * The {@link Supplier} is inherited and is provided with a {@link #transform(Function)} to enable the source data to be changed.
 * This interface is a {@link FunctionalInterface}.
 *
 * @param <T> cache raw type
 * @author Kwanil, Lee
 *
 * @see CacheValue
 */
@FunctionalInterface
public interface CacheValueSupplier<T> extends Supplier<T> {

    /**
     * A transform method that enables you to change the source data to register in cache.
     *
     * @param after after function
     * @param <R> Data type changed by after function to be registered in cache
     * @return Data changed by after function to be registered in cache.
     */
    default <R> CacheValueSupplier<R> transform(Function<? super T, ? extends R> after) {
        Objects.requireNonNull(after);
        return () -> after.apply(get());
    }
}
