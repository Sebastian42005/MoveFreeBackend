package com.example.movefree.service;

import com.example.movefree.database.user.User;
import com.example.movefree.database.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    void getTopUsers() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setFollower(List.of(new User(), new User()));
        User user2 = new User();
        user1.setUsername("user2");
        user2.setFollower(List.of(new User(), new User(), new User()));
        User user3 = new User();
        user1.setUsername("user3");
        user3.setFollower(List.of(new User()));
        List<User> users = List.of(user1, user2, user3);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<String> topUsers = userService.getTopUsers();

        Assertions.assertEquals(topUsers.get(0), user2.getUsername());
        Assertions.assertEquals(topUsers.get(2), user3.getUsername());
    }
}
