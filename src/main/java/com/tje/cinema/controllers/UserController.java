package com.tje.cinema.controllers;

import com.tje.cinema.domain.User;
import com.tje.cinema.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestParam("email") String email,
                               @RequestParam("name") String name,
                               @RequestParam("password") String password,
                               @RequestParam("passwordConf") String passwordConf) {

        if (!passwordConf.equals(password)){
            return "redirect:/registerPage";
        }

        User newUser = new User(email, name, password);
        userService.saveUser(newUser);

        return "redirect:/login";
    }
}
