package application.cache;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static application.cache.CacheManager.cache;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CacheManagerTest {
    @Test
    public void init_and_get_and_clear_in_single_thread() {
        cache().init(()->"a", CacheValue.of(()-> Arrays.asList(1,2,3,4)));
        cache().init(()->"b", CacheValue.of(()-> Arrays.asList(5,6,7,8)));
        cache().reload(()->"c", CacheValue.of(()-> Arrays.asList(9,10,11)));

        Object cachedA = cache().get(() -> "a").cached();
        assertThat(cachedA, is(Arrays.asList(1,2,3,4)));

        List cachedB = (List) cache().get(() -> "b").cached();
        assertThat(cachedB, is(Arrays.asList(5,6,7,8)));

        List<Integer> cachedC = (List<Integer>) cache().get(() -> "c").cached();
        assertThat(cachedC, is(Arrays.asList(9,10,11)));

        cache().clear(()->"a");
    }


    @Test
    public void init_and_get_and_clear_in_multi_thread() {
        ExecutorService executor = Executors.newFixedThreadPool(8);
        try {
            executor.invokeAll(
                Arrays.asList(
                    ()-> cache().init(()->"a", CacheValue.of(()-> Arrays.asList(1,2,3,4))),
                    ()-> cache().init(()->"b", CacheValue.of(()-> Arrays.asList(5,6,7,8))),
                    ()-> cache().init(()->"c", CacheValue.of(()-> Arrays.asList(9,10,11)))
                )
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        executor.shutdown();

        Object cachedA = cache().get(() -> "a").cached();
        assertThat(cachedA, is(Arrays.asList(1,2,3,4)));

        List cachedB = (List) cache().get(() -> "b").cached();
        assertThat(cachedB, is(Arrays.asList(5,6,7,8)));

        List<Integer> cachedC = (List<Integer>) cache().get(() -> "c").cached();
        assertThat(cachedC, is(Arrays.asList(9,10,11)));
    }

}