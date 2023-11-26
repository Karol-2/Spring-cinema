package com.tje.cinema;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
public class AppController {

    private Repertuar repertuar;
    private MovieDatabase movieDatabase;

    public AppController() {
        this.repertuar = new Repertuar();
        this.movieDatabase = new MovieDatabase();
        this.movieDatabase.addMovie(new Movie(1,"Ballada ptaków i węży"));

        this.repertuar.addSeans(new Seans("poniedzialek-14",1, LocalDateTime.of(2024, 1, 29,14,0)));
        this.repertuar.addSeans(new Seans("poniedzialek-18",1, LocalDateTime.of(2024, 1, 29,18,0)));
        this.repertuar.addSeans(new Seans("wtorek-20",1, LocalDateTime.of(2024, 1, 30,20,0)));
    }


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
    public String showMovies(@RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        if (date == null) {
            date = LocalDate.now();
        }
        ArrayList<Seans> movies = repertuar.getSeansesByDate(date);
        model.addAttribute("movies", movies);
        model.addAttribute("selectedDate", date);
        return "moviesPage";
    }

    @GetMapping("/movies/details/{movieId}")
    public String movieDetails(@PathVariable Long movieId, Model model) throws ParseException {

        ArrayList<String> photos = new ArrayList<>();
        photos.add("https://assets.teenvogue.com/photos/65561517b83d1658d07a687f/16:9/w_2560%2Cc_limit/boss-unit-220920-01273-r.jpeg");
        photos.add("https://i.abcnewsfe.com/a/3ba96479-7825-47f4-9280-17e92c5aac27/the-hunger-games-the-ballad-of-songbirds-snakes2-ht-ml-231102_1698933416892_hpMain_16x9.jpg?w=992");
        Movie movie = new Movie(1,"The Hunger Games: The Ballad of Songbirds & Snakes","Action",
                2023,"Francis Lawrence","http://youtu.be/RDE6Uz73A7g?si=mIf9HyfFDeHXWHxL",photos);

        model.addAttribute("movie", movie);

        return "movieDetailsPage";
    }
}
