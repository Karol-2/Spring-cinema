package com.tje.cinema.services;

import com.tje.cinema.domain.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final List<User> userList = new ArrayList<>();
    private User loggedUser;
    private long userIdCounter = 1;

    public User getUserByEmail(String email) throws RuntimeException  {
        return userList.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public void saveUser(User user) {
        user.setId(userIdCounter++);
        System.out.println("Utworzono użytkownika: ");
        System.out.println(user.toString());
        userList.add(user);
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        System.out.println("Zalogowany został użytkownik: ");
        System.out.println(loggedUser.toString());
        this.loggedUser = loggedUser;
    }

    public void removeLoggedUser() {
        System.out.println("Wylogowany został użytkownik: ");
        System.out.println(loggedUser.toString());
        this.loggedUser = null;
    }
}
