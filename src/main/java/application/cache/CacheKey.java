package application.cache;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a supplier of results.
 * Indicates the source data that should apply to the cache values.
 * The {@link CacheKey} is provided with {@link #compose(CacheKey)}, {@link #andThen(CacheKey)} to enable the source data to be changed.
 * This interface is a {@link FunctionalInterface}.
 *
 * @author Kwanil, Lee
 * @see CacheManager
 */
@FunctionalInterface
public interface CacheKey {
    String DELIMITER = "@";

    /**
     * Name used as a key by the CacheManager
     * @return
     */
    String name();

    /**
     * Name used as a key by the CacheManager.
     * default name is {@link #name()}
     * In the implements class allow the user to add one more processing name for the cache.
     *
     * @return Cache Key Name
     *
     * @see #name()
     */
    default String cacheKey() {
        return name();
    }

    /**
     * Returns a composed CacheKey that first applies the {@code before}
     * CacheKey to its input, and then applies this CacheKey name to the result.
     *
     * @param before the function to apply before this function is applied
     * @return a composed CacheKey that first applies the {@code before}
     * CacheKey and then applies this function
     * @throws NullPointerException if before is null
     *
     * @see #andThen(CacheKey)
     */
    default CacheKey compose(CacheKey before) {
        Objects.requireNonNull(before);
        return () -> new StringJoiner(DELIMITER).add(before.name()).add(this.name()).toString();
    }


    /**
     * Returns a composed CacheKey that first applies this CacheKey to
     * its input, and then applies the {@code after} function to the result.
     *
     * @param after the CacheKey to apply after this CacheKey is applied
     * @return a composed CacheKey that first applies this CacheKey and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     *
     * @see #compose(CacheKey)
     */
    default CacheKey andThen(CacheKey after) {
        Objects.requireNonNull(after);
        return () -> new StringJoiner(DELIMITER).add(this.name()).add(after.name()).toString();
    }
}
