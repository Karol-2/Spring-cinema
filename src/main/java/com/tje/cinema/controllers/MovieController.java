package com.tje.cinema.controllers;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.domain.Seans;
import com.tje.cinema.services.MovieService;
import com.tje.cinema.services.RepertuarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class MovieController {

    private final MovieService movieService;
    private final RepertuarService repertuarService;

    @Autowired
    public MovieController(MovieService movieService, RepertuarService repertuarService) {
        this.movieService = movieService;
        this.repertuarService = repertuarService;
    }

    @GetMapping("/movies")
    public String showMovies(@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<Seans> seances = this.repertuarService.getSeansesByDate(date);

        model.addAttribute("seances", seances);
        model.addAttribute("selectedDate", date);
        return "moviesPage";
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

    @PostMapping("/movieForm")
    public String addMovie(Model model) throws ParseException {
        model.addAttribute("newMovie", new Movie());
        model.addAttribute("endpoint", "/add-movie");

        return "movieForm";
    }

    @GetMapping("/movieForm")
    public String movieForm(Model model) throws ParseException {
        model.addAttribute("movie", new Movie());
        model.addAttribute("endpoint", "/add-movie");

        return "movieForm";
    }

    @GetMapping("/movieForm/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        Movie movie = this.movieService.getMovieById(id);

        if (movie != null) {
            model.addAttribute("movie", movie);
            model.addAttribute("endpoint", "/edit-movie");
            return "movieForm";
        } else {
            // Obsługa sytuacji, gdy film o podanym id nie istnieje
            return "redirect:/movieList"; // Przekierowanie na listę filmów lub inną stronę
        }
    }
    @PostMapping("/add-movie")
    public String addMovie(@ModelAttribute("newMovie") Movie newMovie) {
        System.out.println("Dodano film: " + newMovie.getTitle());
        this.movieService.addMovie(newMovie);
        return "redirect:/";
    }

    @GetMapping("/remove-movie/{id}")
    public String removeMovie(@PathVariable Long id, Model model) {
        Movie movie = this.movieService.getMovieById(id);

        if (movie != null) {
            this.movieService.removeMovieById(id);
            //TODO: Add removing all screenigs of this movie
            return "redirect:/admin";
        } else {

            return "redirect:/admin";
        }
    }

    @PostMapping("/edit-movie")
    public String editMovie(@ModelAttribute("newMovie") Movie newMovie) {
        System.out.println("Zedytowano film: " + newMovie.getTitle());
        this.movieService.editMovie(newMovie.getId(),newMovie);
        return "redirect:/admin";
    }

}
