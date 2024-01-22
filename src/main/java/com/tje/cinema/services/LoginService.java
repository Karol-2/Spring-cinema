package com.tje.cinema.services;

import com.tje.cinema.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.tje.cinema.domain.User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if(userEntity.getUserType()== com.tje.cinema.domain.User.UserType.ADMIN){
            return User.builder()
                    .username(userEntity.getEmail())
                    .password(userEntity.getPassword())
                    .roles("ADMIN")
                    .build();
        }

        return User.builder()
                .username(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles("USER")
                .build();
    }
}
