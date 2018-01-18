package application.cache;


import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CacheKeyTest {

    @Test
    public void name_compose(){
        CacheKey cacheKey = () -> "name";
        CacheKey actual = cacheKey.compose(() -> "prefix");
        assertThat(actual.name(), is("prefix@name"));
    }

    @Test
    public void name_and_then(){
        CacheKey cacheKey = () -> "name";
        CacheKey actual = cacheKey.andThen(() -> "postfix");
        assertThat(actual.name(), is("name@postfix"));
    }

    @Test
    public void name_compose_and_then(){
        CacheKey cacheKey = () -> "name";
        CacheKey actual = cacheKey.compose(() -> "prefix").andThen(() -> "postfix");
        assertThat(actual.name(), is("prefix@name@postfix"));
    }

    @Test
    public void enum_cache_key(){
        CacheKey actual = EnumCacheKey.ALPHA.andThen(ClassCacheKey.ID);
        assertThat(actual.name(), is("ALPHA@ID"));
    }

    enum EnumCacheKey implements CacheKey {
        ALPHA, BETA
    }

    abstract static class ClassCacheKey implements CacheKey {
        static final ClassCacheKey ID = new ClassCacheKey(){
            @Override
            public String name() {
                return "ID";
            }
        };

    }
}