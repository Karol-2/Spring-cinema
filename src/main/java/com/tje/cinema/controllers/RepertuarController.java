package com.tje.cinema.controllers;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.domain.Seans;
import com.tje.cinema.services.MovieService;
import com.tje.cinema.services.RepertuarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RepertuarController {
    @Autowired
    private RepertuarService repertuarService ;
    @Autowired
    private MovieService movieService ;

    @PostMapping("/movies")
    public String showMovies(@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<Seans> seances = repertuarService.getSeansesByDate(date);

        model.addAttribute("seances", seances);
        model.addAttribute("selectedDate", date);
        return "moviesPage";
    }

    @PostMapping("/screeningsForm")
    public String addScreening(Model model) throws ParseException {
        List<Movie> moviesList = this.movieService.getAllMovies();
        model.addAttribute("movies", moviesList);
        model.addAttribute("screening", new Seans());
        model.addAttribute("endpoint", "/add-screening");

        return "screeningForm";
    }

    @GetMapping("/screeningsForm/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        Seans screening = this.repertuarService.getSeansById(id);

        if (screening != null) {
            List<Movie> moviesList = this.movieService.getAllMovies().stream()
                    .filter(movie -> movie.getId() != screening.getMovieId())
                    .collect(Collectors.toList());

            model.addAttribute("movies", moviesList);

            System.out.println(screening);
            LocalDate date = screening.getDateAndTime().toLocalDate();
            LocalTime time = screening.getDateAndTime().toLocalTime();

            model.addAttribute("screening", screening);
            model.addAttribute("date", date);
            model.addAttribute("time", time);
            model.addAttribute("endpoint", "/edit-screening");
            return "screeningForm";
        } else {
            // Obsługa sytuacji, gdy film o podanym id nie istnieje
            return "redirect:/admin";
        }
    }

    @GetMapping("/edit-screening")
    public String editScreening(
            @RequestParam("seansId") Long seansId,
            @RequestParam("movie") Long movieId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
            Model model) {
        // TODO: how it affects orders
        LocalDateTime dateAndTime = LocalDateTime.of(date,time);
        Movie movie = this.movieService.getMovieById(movieId);
        Seans newSeans = new Seans(seansId,movie,movie.getId(),dateAndTime);
        this.repertuarService.editSeans(seansId,newSeans);

        return "redirect:/admin";
    }



    @GetMapping("/add-screening")
    public String addScreening(
            @RequestParam("movie") Long movieId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
            Model model) {

        LocalDateTime dateAndTime = LocalDateTime.of(date,time);
        Movie movie = this.movieService.getMovieById(movieId);
        Seans newSeans = new Seans(movieId,movie,movie.getId(),dateAndTime);
        this.repertuarService.addSeans(newSeans);

        return "redirect:/admin";
    }

    @GetMapping("/remove-screening/{id}")
    public String removeMovie(@PathVariable Long id, Model model) {
        Seans screening = this.repertuarService.getSeansById(id);

        if (screening != null) {
            this.repertuarService.removeById(screening.getSeansId());
                //TODO: Add change status of orders to cancelled
            return "redirect:/admin";
        } else {

            return "redirect:/admin";
        }
    }

}
