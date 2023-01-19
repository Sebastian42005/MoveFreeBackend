package com.example.movefree.database.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDTO, Integer> {

    @Query(value = "SELECT * FROM users u WHERE lower(u.username) LIKE lower(concat('%', :search,'%')) LIMIT :max", nativeQuery = true)
    List<UserDTO> search(String search, int max);

    Optional<UserDTO> findByUsername(String username);

    @Query(value = "SELECT * FROM users u where u.username = :username and u.password = :password", nativeQuery = true)
    Optional<UserDTO> login(String username, String password);
}
