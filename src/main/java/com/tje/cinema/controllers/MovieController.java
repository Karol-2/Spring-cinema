package com.tje.cinema.controllers;

import com.tje.cinema.domain.*;
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

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
//        System.out.println(seances);
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

    @GetMapping("/seats/{seansId}")
    public String seats(@PathVariable Long seansId, Model model) throws ParseException {
        try {
            Seans seans = this.repertuarService.getSeansById(seansId);
            model.addAttribute("seans", seans);
            model.addAttribute("seansId", seans.getSeansId());
            model.addAttribute("rows", Arrays.asList("A", "B")); //TODO: deledte?
            System.out.println(seans);
        } catch (RuntimeException e) {
            return "redirect:/movies";
        }
        return "seatPage";
    }


    @PostMapping("/seats")
    public String handleSeatSelection(@RequestParam(name = "seat", required = false) List<String> selectedSeats,
                                      @RequestParam(name = "seansId", required = true) Long seansId,
                                      HttpSession session,
                                      Model model) {
//        System.out.println(seansId);
        if (selectedSeats != null) {
            // create reservation
            Seans seans = repertuarService.getSeansById(seansId);
            Reservation reservation = new Reservation(seans,selectedSeats,(User)session.getAttribute("user"));

            Object userOrder = session.getAttribute("order");
            if (userOrder != null){ // order already exists - add to it
                Order existingOrder = (Order)userOrder;
                existingOrder.addReservation(reservation);
                session.setAttribute("order", existingOrder);

            } else { // order doesn't exist - create new
                List<Reservation> resArr = new ArrayList<>();
                resArr.add(reservation);
                Order order = new Order(1,resArr, LocalDateTime.now(),(User)session.getAttribute("user"));
                session.setAttribute("order", order);
            }
            return "redirect:/cart";

        } else {
//            System.out.println("No seats selected");
            model.addAttribute("error","Choose at least one seat!");
        }
        return "seatPage";
    }

    @GetMapping("/seats/{seansId}/edit")
    public String seatsEdit(@PathVariable String seansId,
                            @RequestParam("previousSeats") String previousSeatsParam,
                            Model model) throws ParseException {
        Long seansIdLong = Long.parseLong(seansId);
        String[] previousSeats = previousSeatsParam
                .replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .split(", ");
        System.out.println("seansId: " + seansIdLong);
        System.out.println("prev seats: " + Arrays.toString(previousSeats));

        try {
            Seans seans = this.repertuarService.getSeansById(seansIdLong);
            model.addAttribute("previousSeats", previousSeats);
            model.addAttribute("seans", seans);
            System.out.println(seans);
        } catch (RuntimeException e) {
            return "redirect:/movies";
        }
        return "seatEditPage";
    }

    @PostMapping("/editReservation")
            public String editReservation(@RequestParam(name = "seat", required = false) List<String> selectedSeats,
                    @RequestParam(name = "seansId", required = true) Long seansId,
                    HttpSession session,
                    Model model) throws ParseException {

                if (selectedSeats != null) {
                    // find reservation
                    Order userOrder = (Order)session.getAttribute("order");
                    userOrder.getReservations().stream()
                            .filter((res) -> res.getSeans().getSeansId().equals(seansId))
                            .findFirst()
                            .ifPresent((res) -> res.setReservedSeats(selectedSeats));

                    session.setAttribute("order", userOrder);

                    return "redirect:/cart";

        } else {
            model.addAttribute("error","Choose at least one seat!");
        }
        return "seatPage"; //TODO: this
    }





}
