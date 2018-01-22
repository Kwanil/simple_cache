package application.cache;

/**
 * Represents a supplier of cache key.
 * Inherit Cache key for Enum Constant.
 * Indicates the source data that should apply to the cache values.
 * The {@link CacheKey} is provided with {@link #compose(CacheKey)}, {@link #andThen(CacheKey)} to enable the source data to be changed.
 * This interface is a {@link FunctionalInterface}.
 *
 * @author Kwanil, Lee
 * @see CacheManager
 * @see CacheKey
 */
@FunctionalInterface
public interface EnumCacheKey extends CacheKey{

    /**
     * Name used as a key by the CacheManager
     * @return cache key
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
}
