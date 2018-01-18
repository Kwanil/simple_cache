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

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class CacheApplicationTests {
	public static final CacheKey CACHE_KEY = () -> "user";

	@Autowired
	UserRepository userRepository;

	@Before
	public void before() {
		userRepository.save(new User("first", "last"));
		userRepository.save(new User("Jack", "Bauer"));
		userRepository.save(new User("Chloe", "O'Brian"));
		userRepository.save(new User("Kim", "Bauer"));
		userRepository.save(new User("David", "Palmer"));
		userRepository.save(new User("Michelle", "Dessler"));

		CacheManager.cache().init(CACHE_KEY, CacheValue.of(userRepository::findAll));
	}

	@Test
	public void contextLoads_by_repository() {
		List<User> findByLastName = userRepository.findByLastName("last");

		assertThat(findByLastName).extracting(User::getLastName).containsOnly("last");
	}

	@Test
	public void contextLoads_by_cache() {
		CacheValue<List<User>> cacheValue = CacheManager.cache().get(CACHE_KEY);

		List<User> users = cacheValue.cached();
		List<User> findByLastName = users.stream().filter(u -> u.getLastName().equals("last")).collect(toList());

		assertThat(findByLastName).extracting(User::getLastName).containsOnly("last");
	}

}
