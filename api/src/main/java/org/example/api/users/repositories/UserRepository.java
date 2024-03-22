package org.example.api.users.repositories;

import java.util.Optional;
import org.example.api.users.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

}
