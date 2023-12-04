package com.tje.cinema.controllers;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.domain.Seans;
import com.tje.cinema.services.MovieService;
import com.tje.cinema.services.RepertuarService;
import com.tje.cinema.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class AppController {

    private final MovieService movieService;
    private final RepertuarService repertuarService;
    private final UserService userService;

    @Autowired
    public AppController(MovieService movieService, RepertuarService repertuarService, UserService userService) {
        this.movieService = movieService;
        this.repertuarService = repertuarService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) throws ParseException {
        List<Movie> movies = this.movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "homePage";
    }



    @GetMapping("/movies")
    public String showMovies(@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<Seans> seances = this.repertuarService.getSeansesByDate(date);

        model.addAttribute("seances", seances);
//        System.out.println(seances);
        model.addAttribute("selectedDate", date);
        return "moviesPage";
    }

    @GetMapping("/seats/{seansId}")
    public String seats(@PathVariable Long seansId,Model model) throws ParseException {
        try {
            Seans seans = this.repertuarService.getSeansById(seansId);
            model.addAttribute("seans", seans);
            model.addAttribute("seansId", seans.getSeansId());
        } catch (RuntimeException e) {
            return "redirect:/movies";
        }
        return "seatPage";
    }


    @GetMapping("/movies/details/{movieId}")
    public String movieDetails(@PathVariable Long movieId, Model model) throws ParseException {

        try {
            Movie movie = this.movieService.getMovieById(movieId);
            model.addAttribute("movie", movie);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "movieDetailsPage";
    }



}