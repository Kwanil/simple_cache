package application.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 * This is cache manager of all purchase system.
 *
 * Class that manages the registration, management, removal and reloading of the cache.
 * The key that is registered as cache is the name of the {@link CacheKey},
 * and the value to be registered is the {@link CacheValue} object.
 *
 * This object is created only with Singleton,
 * and the object managing the cache checks concurrency.
 *
 * @author Kwanil, Lee
 *
 * @see CacheKey
 * @see CacheValue
 */
public class CacheManager {
    private static final Map<String, CacheValue<?>> CACHE_MAP = new ConcurrentHashMap<>();
    private static final CacheManager INSTANCE = new CacheManager();

    /**
     * A method for creating cache manager.
     * This object is singleton.
     *
     * @return instance
     */
    public static CacheManager cache() {
        return INSTANCE;
    }

    /**
     * To create with Singleton, the creator is not publicly available.
     */
    private CacheManager(){
    }

    /**
     * Insert the data to manage with the cache.
     *
     * @param key Key to manage as cache
     * @param value Values to register with Key
     */
    public boolean init(CacheKey key, CacheValue<?> value) {
        return putIfNotNull(key, value);
    }

    /**
     * Insert the data to manage with the cache.
     *
     * @param key Key to manage as cache
     * @param value Values to register with Key
     */
    public boolean reload(CacheKey key, CacheValue<?> value) {
        return putIfNotNull(key, value);
    }

    /**
     * Get the registered cache value.
     *
     * @param key registered Cache Key
     * @return registered Cache Value
     */
    public <T> CacheValue<T> get(CacheKey key) {
        return (CacheValue<T>) CACHE_MAP.get(requireNonNull(key).cacheKey());
    }


    /**
     * Get all the registered cache key names.
     *
     * @return registered all Cache key names
     */
    public Collection<String> getKeys() {
        return Collections.unmodifiableCollection(CACHE_MAP.keySet());
    }

    /**
     * Remove the corresponding data for the CacheKey.
     *
     * @param key registered Cache Key
     */
    public void clear(CacheKey key) {
        CACHE_MAP.remove(requireNonNull(key).cacheKey());
    }

    /**
     * Remove all the cache.
     */
    public void clearAll(){
        CACHE_MAP.clear();
    }

    private boolean putIfNotNull(CacheKey key, CacheValue<?> value){
        CacheKey cacheKey = requireNonNull(key);
        CacheValue<?> cacheValue = requireNonNull(value);
        return CACHE_MAP.put(cacheKey.cacheKey(), cacheValue) == null;
    }
}