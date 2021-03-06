# Spring Repository DTO

I saw many times how people use something like that for mapping entities to DTO. They use only one entity and after selecting them programmatically transfer one object to another.

This way is a bit superfluous, and you can use two other ways with JPQL query or SQL query.

This project shows all three approaches to select necessary objects from a database.

Choose which of them best for you.  

```java
List<User> findAll();

@Query(value = "select name, phone from \"user\"", nativeQuery = true)
List<UserDto> findAllDto();

@Query(value = "select new spring.dto.user.UserSnapshot(u.name, u.phone) from User u")
List<UserSnapshot> findAllSnapshot();
```