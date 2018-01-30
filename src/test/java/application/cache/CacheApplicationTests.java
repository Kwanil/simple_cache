package application.cache;

import application.cache.user.User;
import application.cache.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.function.Function;

import static application.cache.CacheManager.cache;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class CacheApplicationTests {
	private static final CacheKey ADULT = () ->	"ADULT";

	private static final CacheKey USER = CacheStatus.USER;
	private static final CacheKey ADULT_USER = CacheStatus.USER.andThen(ADULT);

	@Autowired
	UserRepository userRepository;

	@Before
	public void before() {
		userRepository.save(new User("first", "last", 17));
		userRepository.save(new User("Jack", "Bauer", 18));
		userRepository.save(new User("Chloe", "O'Brian", 21));
		userRepository.save(new User("Kim", "Bauer",40));
		userRepository.save(new User("David", "Palmer",10));
		userRepository.save(new User("Michelle", "Dessler",32));

		List<User> allUsers = userRepository.findAll();
		Function<List<User>, List<User>> adultFunction = list -> list.stream().filter(u -> u.getAge() >19).collect(toList());

		CacheValueSupplier<List<User>> userSupplier = () -> allUsers;
		CacheValueSupplier<List<User>> adultUserSupplier = userSupplier.transform(adultFunction);

		cache().init(USER, CacheValue.of(userSupplier));
		cache().init(ADULT_USER, CacheValue.of(adultUserSupplier));
	}

	@Test
	public void contextLoads_by_repository() {
		List<User> findByLastName = userRepository.findByLastName("last");

		assertThat(findByLastName).extracting(User::getLastName).containsOnly("last");
	}

	@Test
	public void contextLoads_by_cache() {
		CacheValue<List<User>> cacheValue = cache().get(USER);

		List<User> users = cacheValue.cached();
		List<User> findByLastName = users.stream().filter(u -> u.getLastName().equals("last")).collect(toList());

		assertThat(findByLastName).extracting(User::getLastName).containsOnly("last");
	}

	@Test
	public void contextLoads_by_cache_adult() {
		CacheValue<List<User>> cacheValue = cache().get(ADULT_USER);

		List<User> users = cacheValue.cached();
		List<User> findByLastName = users.stream().filter(u -> u.getLastName().equals("last")).collect(toList());

		assertThat(findByLastName).extracting(User::getLastName).doesNotContain("last");
	}

}
