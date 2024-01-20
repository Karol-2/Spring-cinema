package com.tje.cinema.controllersWEB;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.domain.Screening;
import com.tje.cinema.services.MovieService;
import com.tje.cinema.services.OrderService;
import com.tje.cinema.services.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

@Controller
public class MovieController {

    private final MovieService movieService;
    private final ScreeningService screeningService;
    private final OrderService orderService;

    @Autowired
    public MovieController(MovieService movieService, ScreeningService screeningService, OrderService orderService) {
        this.movieService = movieService;
        this.screeningService = screeningService;
        this.orderService = orderService;
    }

    @GetMapping("/movies")
    public String showMovies(@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<Screening> seances = this.screeningService.getscreeningesByDate(date);

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

    @GetMapping("/movieForm")
    public String movieForm(Model model) throws ParseException {
        model.addAttribute("action", "Add Movie");
        model.addAttribute("movie", new Movie());
        model.addAttribute("endpoint", "/add-movie");
        model.addAttribute("currentYear", Year.now().getValue());

        return "movieForm";
    }

    @GetMapping("/movieForm/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        Movie movie = this.movieService.getMovieById(id);

        if (movie != null) {
            model.addAttribute("movie", movie);
            model.addAttribute("action", "Edit Movie");
            model.addAttribute("endpoint", "/edit-movie");
            model.addAttribute("currentYear", Year.now().getValue());
            return "movieForm";
        }
        return "redirect:/movieList";

    }
    @PostMapping("/add-movie")
    public String addMovie(@ModelAttribute("newMovie") Movie newMovie) {
        System.out.println("Dodano film: " + newMovie.getTitle());
        this.movieService.addMovie(newMovie);
        return "redirect:/admin";
    }

    @GetMapping("/remove-movie/{id}")
    public String removeMovie(@PathVariable Long id, Model model) {
        Movie movie = this.movieService.getMovieById(id);

        if (movie != null) {
            //Removing all screenigs of this movie
            this.screeningService.removeAllscreeningOfMovie(id);

            //Cancel all orders with this movie
            this.orderService.cancelEveryOrderOfMovie(id);

            this.movieService.removeMovieById(id);

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
