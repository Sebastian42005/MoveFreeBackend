package com.example.movefree.database.spot.rating;

import com.example.movefree.database.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByUser(User user);
}
