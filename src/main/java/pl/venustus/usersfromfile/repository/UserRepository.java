package pl.venustus.usersfromfile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.venustus.usersfromfile.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByPhoneNo(String phoneNumber);

    List<User> findAllByOrderByBirthDateDesc();
}
