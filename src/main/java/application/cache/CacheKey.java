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

    String name();

    default CacheKey compose(CacheKey before) {
        Objects.requireNonNull(before);
        return () -> new StringJoiner(DELIMITER).add(before.name()).add(this.name()).toString();
    }

    default CacheKey andThen(CacheKey after) {
        Objects.requireNonNull(after);
        return () -> new StringJoiner(DELIMITER).add(this.name()).add(after.name()).toString();
    }
}
