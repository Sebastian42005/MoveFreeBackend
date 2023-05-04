package com.example.movefree.database.user;

import com.example.movefree.database.spot.spot.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "SELECT username FROM users u WHERE lower(u.username) LIKE lower(concat('%', :search,'%')) LIMIT :max", nativeQuery = true)
    List<String> search(String search, int max);

    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM users u where u.username = :username and u.password = :password", nativeQuery = true)
    Optional<User> login(String username, String password);

    @Query("SELECT user.spots FROM User user WHERE user.username = :username")
    List<Spot> getUserSpots(String username);
}
