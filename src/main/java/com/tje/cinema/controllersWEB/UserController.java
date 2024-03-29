package com.tje.cinema.controllersWEB;

import com.tje.cinema.domain.AdminUser;
import com.tje.cinema.domain.User;
import com.tje.cinema.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/web-login")
    public String showLoginPage(@RequestParam(name = "error", required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "loginPage";
    }

    @PostMapping("/web-login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        try { // sprawdzanie czy istnieje taki user
            User user = userService.getUserByEmail(email);

            try { // czy jest moze adminem
                AdminUser adminUser = (AdminUser) user;
                System.out.println(adminUser);
                if(adminUser.getUserType().equals(User.UserType.ADMIN)){
                    session.setAttribute("user", adminUser);
                    return "redirect:/admin";
                }
            } catch (RuntimeException ignored){}

            if (password.equals(user.getPassword())) { // czy hasła pasują
                session.setAttribute("user", user);
                return "redirect:/movies";
            } else { // hasła nie pasują
                redirectAttributes.addAttribute("error", "Password doesn't match");
                return "redirect:/web-login";
            }

        } catch (RuntimeException e) { //nie ma takiego loginu
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/web-login";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(@RequestParam(name = "error", required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "registerPage";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("email") String email,
                               @RequestParam("name") String name,
                               @RequestParam("password") String password,
                               @RequestParam("passwordConf") String passwordConf,
                               RedirectAttributes redirectAttributes) {

        if (!passwordConf.equals(password)) {
            redirectAttributes.addAttribute("error", "Passwords do not match");
            return "redirect:/register";
        }

        try {
            userService.getUserByEmail(email);
            redirectAttributes.addAttribute("error", "User with this email already exists!");
            return "redirect:/register";
        } catch (RuntimeException ignored) {
        }

        User newUser = new User(email, name, password);
        userService.saveUser(newUser);

        return "redirect:/web-login";
    }

    @GetMapping("/web-logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();

        return "redirect:/";
    }

}
