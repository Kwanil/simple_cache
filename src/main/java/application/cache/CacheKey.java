package application.cache;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

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

    String name();

    default CacheKey compose(CacheKey before) {
        Objects.requireNonNull(before);
        return () -> StringUtils.join(before.name(), DELIMITER, this.name());
    }

    default CacheKey andThen(CacheKey after) {
        Objects.requireNonNull(after);
        return () -> StringUtils.join(this.name(), DELIMITER, after.name());
    }
}
