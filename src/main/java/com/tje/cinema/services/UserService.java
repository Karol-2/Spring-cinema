package com.tje.cinema.services;

import com.tje.cinema.domain.AdminUser;
import com.tje.cinema.domain.User;
import com.tje.cinema.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        setInitialUsers();
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    private void setInitialUsers() {
        if (userRepository.findByEmail("admin@cinematricity.com").isEmpty()) {
            AdminUser admin = new AdminUser("admin@cinematricity.com","admin_Karol","itakniezgadniesz125");
            userRepository.save(admin);

        }
        if(userRepository.findByEmail("user1@example.com").isEmpty()){
            User user = new User("user1@example.com","k-krawczykiewicz","ABC!@#abc123");
            user.setUserType(User.UserType.REGISTERED);
            userRepository.save(user);
        }
    }
}
