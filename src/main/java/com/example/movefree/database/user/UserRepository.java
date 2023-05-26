package com.example.movefree.database.user;

import com.example.movefree.database.spot.spot.Spot;
import com.example.movefree.service.jwt.JwtUser;
import com.example.movefree.service.user.UserProfilePicture;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "SELECT username FROM users u WHERE lower(u.username) LIKE lower(concat('%', :search,'%')) LIMIT :max", nativeQuery = true)
    List<String> search(String search, int max);


    @Query(value = "SELECT * FROM users u where u.username = :username and u.password = :password", nativeQuery = true)
    Optional<User> login(String username, String password);

    @Query("SELECT spot FROM User user JOIN user.spots spot WHERE user.username = :username AND spot.id NOT IN(:alreadySeenList) ORDER BY spot.createdAt DESC")
    List<Spot> getUserSpots(String username, List<UUID> alreadySeenList, Pageable pageable);

    @Query("SELECT new com.example.movefree.database.user.UserDTO(user.username, user.role, user.description, user.spots.size, user.follower.size, (user.follows.size + user.followCompanies.size))" +
            " FROM User user WHERE user.username = :username")
    Optional<UserDTO> findDTOByUsername(String username);

    Optional<User> findByUsername(String username);

    @Query("SELECT user.role FROM User user WHERE user.username = :username")
    Optional<String> findRoleByUsername(String username);

    @Query("SELECT new com.example.movefree.service.jwt.JwtUser(user.username, user.password, user.role) FROM User user WHERE user.username = :username")
    Optional<JwtUser> findUserDetailsByUsername(String username);

    @Query("SELECT new com.example.movefree.service.user.UserProfilePicture(user.profilePicture, user.contentType) FROM User user WHERE user.username = :username")
    Optional<UserProfilePicture> getUserProfilePictureByUsername(String username);

    @Query("SELECT follower.username FROM User user JOIN user.follower follower WHERE user.username = :username")
    List<String> getUserFollowers(String username);
}
