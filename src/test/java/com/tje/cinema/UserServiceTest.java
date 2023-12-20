package com.tje.cinema;

import com.tje.cinema.domain.User;
import com.tje.cinema.repositories.UserRepository;
import com.tje.cinema.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void testGetUserByEmail() {
        // Arrange
        String email = "user1@example.com";
        User user = new User(email, "k-krawczykiewicz", "ABC!@#abc123");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserByEmail(email);

        // Assert
        assertEquals(user, result);
    }

    @Test
    void testGetUserByEmailUserNotFound() {
        // Arrange
        String nonExistingEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(RuntimeException.class, () -> userService.getUserByEmail(nonExistingEmail));
    }

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User("user1@example.com", "k-krawczykiewicz", "ABC!@#abc123");

        // Act
        userService.saveUser(user);

        // Assert
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserList() {
        // Arrange
        User user1 = new User("user1@example.com", "k-krawczykiewicz", "ABC!@#abc123");
        User user2 = new User("user2@example.com", "john_doe", "Passw0rd");
        List<User> userList = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> result = userService.getUserList();

        // Assert
        assertEquals(userList, result);
    }


}
