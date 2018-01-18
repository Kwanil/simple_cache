package application.cache;


import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;

/**
 * Value object managed by {@link CacheManager}.
 * This Object is created by the {@link CacheValueSupplier},
 * and the type <T> is the class type of the cache value.
 *
 * @param <T> cache raw type
 * @author Kwanil, Lee
 *
 * @see CacheManager
 * @see CacheValueSupplier
 */
public interface CacheValue<T> {
    /**
     * Source data to register as the value of the cache
     *
     * @return raw type
     */
    T cached();

    /**
     * cached loading time
     * default value is {@link LocalDateTime#now()}
     *
     * @return cached loading time
     */
    LocalDateTime reloadTime();

    /**
     * Get a cached value and then get transform cache value
     *
     * @param after the function to apply after cache value is get
     * @param <R> the type of output of the {@code after} function, and of the composed function
     * @return Cache values {@code after} function applied
     * @throws NullPointerException if transformer is null
     */
    default <R> R cached(Function<? super T,? extends R> after) {
        Objects.requireNonNull(after);
        return after.apply(cached());
    }

    /**
     * static construnctor. create cache values.
     * default reload time is {@link LocalDateTime#now()}
     *
     * @param supplier {@link CacheValueSupplier} Source data to register as the value of the cache
     * @param <T> cached raw type
     * @return CacheValue is Source data and time of registration
     * @throws NullPointerException if transformer is null
     */
    static <T> CacheValue<T> of(CacheValueSupplier<? extends T> supplier) {
        return of(supplier, LocalDateTime.now());
    }

    /**
     * static construnctor
     * Custom Time when the cache value was registered
     *
     * @param supplier {@link CacheValueSupplier} Source data to register as the value of the cache
     * @param reloadTime {@link java.time.LocalDate} Time when the cache value was registered
     * @param <T> cached raw type
     * @return CacheValue is Source data and time of registration
     * @throws NullPointerException if transformer is null
     */
    static <T> CacheValue<T> of(CacheValueSupplier<? extends T> supplier, LocalDateTime reloadTime) {
        T cacheValue = Objects.requireNonNull(supplier).get();
        LocalDateTime time = Objects.requireNonNull(reloadTime);
        return new CacheValue<T>() {
            @Override
            public T cached() {
                return cacheValue;
            }
            @Override
            public LocalDateTime reloadTime() {
                return time;
            }
        };
    }
}
