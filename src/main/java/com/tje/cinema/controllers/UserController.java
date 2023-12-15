package com.tje.cinema.controllers;

import com.tje.cinema.domain.AdminUser;
import com.tje.cinema.domain.Movie;
import com.tje.cinema.domain.Seans;
import com.tje.cinema.domain.User;
import com.tje.cinema.services.MovieService;
import com.tje.cinema.services.RepertuarService;
import com.tje.cinema.services.StatsService;
import com.tje.cinema.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private RepertuarService repertuarService;

    @Autowired
    private StatsService statsService;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(name = "error", required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "loginPage";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        try { // sprawdzanie czy istnieje taki user
            User user = userService.getUserByEmail(email);

            try { // czy jest moze adminem
                AdminUser adminUser = (AdminUser) user;
                System.out.println(adminUser);
                if(adminUser.isHasAdminAccess()){
                    session.setAttribute("adminuser", adminUser);
                    return "redirect:/admin";
                }
            } catch (RuntimeException ignored){}

            if (password.equals(user.getPassword())) { // czy hasła pasują
                session.setAttribute("user", user);
                return "redirect:/movies";
            } else { // hasła nie pasują
                redirectAttributes.addAttribute("error", "Password doesn't match");
                return "redirect:/login";
            }

        } catch (RuntimeException e) { //nie ma takiego loginu
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/login";
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

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();

        return "redirect:/";
    }
    @GetMapping("/admin")
    public String admin(HttpSession session, Model model) {
        List<Movie> moviesList = this.movieService.getAllMovies();
        model.addAttribute("movies", moviesList);

        LocalDate OD = LocalDate.of(2022,1,1);
        LocalDate DO = LocalDate.of(2025,1,1);

        List<Seans> screenings = this.repertuarService.getAllSeans();
        model.addAttribute("screenings", screenings);
        model.addAttribute("numOfOrders",statsService.getNumberOfOrders(OD, DO));
        model.addAttribute("numOfScreenings",statsService.getNumberOfScreenings(OD, DO));
        model.addAttribute("numOfMovies",statsService.getNumberofMoviesShown(OD, DO));
        model.addAttribute("mostPopular",statsService.getMostPopularMovie(OD, DO));
        model.addAttribute("numOfSeats",statsService.getSoldSeats(OD, DO));
        model.addAttribute("moneyEarned",statsService.getMoneyEarned(OD, DO));
        model.addAttribute("earnings",statsService.getEarningsPerCustomer(OD, DO));
        model.addAttribute("percentOfSeats",statsService.getPercentOfTakenSeats(OD, DO));
        model.addAttribute("numOfUsers",statsService.getNumberOfUsersReg(OD, DO));
        return "adminPanelPage";
    }


}
