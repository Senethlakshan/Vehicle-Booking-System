package athiq.veh.isn_backend.repository;

import athiq.veh.isn_backend.model.Role;
import athiq.veh.isn_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIsActivatedTrue();
    List<User> findByIsActivatedFalse();

    List<User> findByRole(Role role);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    User findUserById(long id);

    Optional<User> findByPasswordResetToken(String token);

    List<User> findByFirstNameContainingIgnoreCase(String firstName);

}
