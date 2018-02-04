# Simple cache
- 간단한 어플리케이션 캐쉬
- JAVA8 이상

간당한 어플리케이션 캐쉬를 구현하였다
RDB에서 조회해온 데이터를 재조회 하지 않도록 하는것을 목적으로 하였으며,
스케쥴링을 통한 reload등은 구현하지 않았다. 
또한 메모리를 많이 사용하지 않으며, 자주 전체 조회를 해야하는 경우에 사용하려고 만들었다

여기서 작성한 아이디어는 DB등을 통해서 조회해온 iterable한 객체를
여러 타입(list, map, string, object 등..)으로 캐싱이 될수 있도록 한 것이다.


```
enum CachType implements EnumCacheKey{
USER
}

@Getter
@Setter
class User {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
}

List<User> users = repository.finaAll();

CacheManager.init(USER,of(users));
```
예를 들어, 위와 같이 스키마로 User정보를  DB에서 가져온다고하면, List등의 Iterable한 객체로
그것을 Cache를 할 것이다.
그러나 User가 가장 많이 조회되는 것이, id와 lastName이라면 list로 캐싱이 되어있다면
조회시에 매우 불리할 것이다.

그렇다면, 아래 두객체를 캐싱하는 편이 좋다.
```
Map<Long, User> userIdMap = users.stream().collect(toMap(User::getId,Function.identity()));
Map<String, List<User>> userLastNameMap = users.stream().collect(groupingBy(User::getLastName));

CacheManager.init(USER.andThen(ID),of(userIdMap));
CacheManager.init(USER.andThen(LAST_NAME),of(userLastNameMap));
```
이렇게 구성하면 USER의 ID로 캐쉬한 데이터는 ID로 조회할 때 바로 가져올수있고,
LAST_NAME으로 캐쉬한 데이터는 LAST_NAME으로 바로 가져올수 있을 것이다
```
Map<Long, User> userIdMap = CacheManager.cache().get(USER.andThen(ID));
Map<String, List<User>> userLastNameMap = CacheManager.cache().get(USER.andThen(LAST_NAME));

```

test case는 spring boot, jpa로 구현하였다

