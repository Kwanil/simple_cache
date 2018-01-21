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
public enum CacheStatus implements CacheKey {
    USER,
    LANGUAGE,
    CLASS,
    COMPANY;

    /**
     * Obtain all registered enum constant values.
     * all method is {@link Enum#valueOf(Class, String)}
     * @return
     */
    public static CacheStatus[] all() {
        return CacheStatus.values();
    }
}
