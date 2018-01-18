package application.cache;


import org.junit.Test;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.partitioningBy;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CacheValueSupplierTest {
    CacheService service = new CacheService();

    @Test
    public void transform_long_to_string() {
        CacheValueSupplier<Integer> supplier = service::selectOne;
        CacheValueSupplier<String> transform = supplier.transform(String::valueOf);
        String s = transform.get();
        assertThat(s, is("1"));
    }

    @Test
    public void transform_list_to_map() {
        CacheValueSupplier<List<Integer>> supplier = service::selectList;
        Map<Boolean, List<Integer>> actual = supplier.transform(service::toMap).get();
        assertThat(actual.containsKey(true), is(true));
        assertThat(actual.get(true), is(asList(2,4)));
        assertThat(actual.containsKey(false), is(true));
        assertThat(actual.get(false), is(asList(1,3)));
    }

    static class CacheService {
        private Integer selectOne(){
            return 1;
        }

        private List<Integer> selectList(){
            return asList(1,2,3,4);
        }

        private Map<Boolean, List<Integer>> toMap(List<Integer> list) {
            return list.stream().collect(partitioningBy(i -> i % 2 == 0));
        }
    }


}