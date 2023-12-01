package com.tje.cinema.services;

import com.tje.cinema.domain.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<User> userList = new ArrayList<>();

    public User getUserByEmail(String email) throws RuntimeException  {
        return userList.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public void saveUser(User user) {
        userList.add(user);
    }
}
