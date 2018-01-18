package application.cache;


import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CacheValueTest {

    @Test
    public void create_value_object_without_time() {
        CacheValue<Integer> cacheValue = CacheValue.of(() -> 1);
        assertThat(cacheValue.cached(),is(1));
    }

    @Test
    public void create_value_object() {
        LocalDateTime reloadTime = LocalDateTime.of(2018, 1, 1, 0, 0);
        CacheValueSupplier<Integer> supplier = () -> 1;
        CacheValue<String> cacheValue = CacheValue.of(supplier.transform(String::valueOf), reloadTime);
        assertThat(cacheValue.cached(),is("1"));
        assertThat(cacheValue.reloadTime(),is(reloadTime));
    }

    @Test
    public void create_value_object_and_transformer() {
        LocalDateTime reloadTime = LocalDateTime.of(2018, 1, 1, 0, 0);
        CacheValueSupplier<String> supplier = () -> "abc";
        CacheValue<String> cacheValue = CacheValue.of(supplier, reloadTime);
        assertThat(cacheValue.cached(String::toUpperCase),is("ABC"));
        assertThat(cacheValue.reloadTime(),is(reloadTime));
    }
}