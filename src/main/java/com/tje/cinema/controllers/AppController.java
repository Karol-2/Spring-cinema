package com.tje.cinema.controllers;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.ParseException;
import java.util.List;

@Controller
public class AppController {

    private final MovieService movieService;

    @Autowired
    public AppController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String home(Model model) throws ParseException {
        List<Movie> movies = this.movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "homePage";
    }

}