package application.cache;

/**
 * This is cache type of all purchase system and is enumeration types.
 *
 * {@link CacheClassType} to be used as the {@link CacheKey}.
 * {@link CacheKey} is inherited.
 *
 * @author Kwanil, Lee
 *
 * @see CacheKey
 */
public enum CacheClassType implements CacheKey {
    MAP,
    LIST
}
