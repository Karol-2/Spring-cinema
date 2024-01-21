package com.tje.cinema.services;

import com.tje.cinema.domain.AdminUser;
import com.tje.cinema.domain.User;
import com.tje.cinema.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
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

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        return  userRepository.save(user);
    }

    @Transactional
    public User editUser(User user) {
        User userEdited = userRepository.findById(user.getId())
                .orElseThrow();

        userEdited.setEmail(user.getEmail());
        userEdited.setName(user.getName());
        userEdited.setPassword(user.getPassword());

        return userRepository.save(userEdited);
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
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
