package spring.dto.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    interface UserDto {
        String getPhone();
        String getName();
    }

    @Query(value = "select name, phone from \"user\"", nativeQuery = true)
    List<UserDto> findAllDto();

    @Query(value = "select new spring.dto.user.UserSnapshot(u.name, u.phone) from User u")
    List<UserSnapshot> findAllSnapshot();
}
