package application.cache;

/**
 * This is cache status of all purchase system and is enumeration types.
 *
 * {@link CacheStatus} to be used as the {@link CacheKey}.
 * {@link CacheKey} is inherited.
 *
 * @author Kwanil, Lee
 *
 * @see CacheKey
 */
public enum CacheStatus implements EnumCacheKey {
    USER,
    LANGUAGE,
    CLASS,
    COMPANY;

}
