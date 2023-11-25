package com.tje.cinema;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.ParseException;

@Controller
public class AppController {

    @GetMapping("/")
    public String home(Model model) throws ParseException {
        return "homePage";
    }

    @GetMapping("/login")
    public String login(Model model) throws ParseException {
        return "loginPage";
    }

    @GetMapping("/register")
    public String register(Model model) throws ParseException {
        return "registerPage";
    }

    @GetMapping("/movies")
    public String movies(Model model) throws ParseException {
        return "moviesPage";
    }

    @GetMapping("/movies/details/{movieId}")
    public String movieDetails(@PathVariable Long movieId, Model model) throws ParseException {

//        Movie movie = movieService.getMovieById(movieId);
//
//        model.addAttribute("movie", movie);
//
        return "movieDetailsPage";
    }
}
