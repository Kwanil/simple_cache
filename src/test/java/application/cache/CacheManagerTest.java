package application.cache;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CacheManagerTest {
    @Test
    public void init_and_get_and_clear_in_single_thread() {
        CacheManager manager = CacheManager.cache();
        manager.init(()->"a", CacheValue.of(()-> Arrays.asList(1,2,3,4)));
        manager.init(()->"b", CacheValue.of(()-> Arrays.asList(5,6,7,8)));
        manager.reload(()->"c", CacheValue.of(()-> Arrays.asList(9,10,11)));

        Object cachedA = manager.get(() -> "a").cached();
        assertThat(cachedA, is(Arrays.asList(1,2,3,4)));

        List cachedB = (List) manager.get(() -> "b").cached();
        assertThat(cachedB, is(Arrays.asList(5,6,7,8)));

        List<Integer> cachedC = (List<Integer>) manager.get(() -> "c").cached();
        assertThat(cachedC, is(Arrays.asList(9,10,11)));

        manager.clear(()->"a");
    }


    @Test
    public void init_and_get_and_clear_in_multi_thread() {
        ExecutorService executor = Executors.newFixedThreadPool(8);
        CacheManager cacheManager = CacheManager.cache();

        try {
            executor.invokeAll(
                Arrays.asList(
                    ()-> cacheManager.init(()->"a", CacheValue.of(()-> Arrays.asList(1,2,3,4))),
                    ()-> cacheManager.init(()->"b", CacheValue.of(()-> Arrays.asList(5,6,7,8))),
                    ()-> cacheManager.init(()->"c", CacheValue.of(()-> Arrays.asList(9,10,11)))
                )
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        executor.shutdown();

        Object cachedA = cacheManager.get(() -> "a").cached();
        assertThat(cachedA, is(Arrays.asList(1,2,3,4)));

        List cachedB = (List) CacheManager.cache().get(() -> "b").cached();
        assertThat(cachedB, is(Arrays.asList(5,6,7,8)));

        List<Integer> cachedC = (List<Integer>) CacheManager.cache().get(() -> "c").cached();
        assertThat(cachedC, is(Arrays.asList(9,10,11)));
    }

}