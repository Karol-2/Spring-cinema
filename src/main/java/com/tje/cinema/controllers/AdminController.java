package com.tje.cinema.controllers;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.domain.Reservation;
import com.tje.cinema.domain.Seans;
import com.tje.cinema.services.MovieService;
import com.tje.cinema.services.RepertuarService;
import com.tje.cinema.services.StatsService;
import com.tje.cinema.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private RepertuarService repertuarService;

    @Autowired
    private StatsService statsService;

    @GetMapping("/admin")
    public String admin(HttpSession session, Model model) {

        LocalDate OD = LocalDate.of(2000,1,1);
        LocalDate DO = LocalDate.of(3000,1,1);

        List<Movie> moviesList = this.movieService.getAllMovies();
        model.addAttribute("movies", moviesList);
        List<Seans> screenings = this.repertuarService.getAllSeans();
        model.addAttribute("screenings", screenings);
        model.addAttribute("ticketPrice", Reservation.TICKET_COST);
        model.addAttribute("numOfOrders",statsService.getNumberOfOrders(OD, DO));
        model.addAttribute("numOfScreenings",statsService.getNumberOfScreenings(OD, DO));
        model.addAttribute("numOfMovies",statsService.getNumberofMoviesShown(OD, DO));
        model.addAttribute("mostPopular",statsService.getMostPopularMovie(OD, DO));
        model.addAttribute("numOfSeats",statsService.getSoldSeats(OD, DO));
        model.addAttribute("moneyEarned",statsService.getMoneyEarned(OD, DO));
        model.addAttribute("earnings",statsService.getEarningsPerOrder(OD, DO));
        model.addAttribute("percentOfSeats",statsService.getPercentOfTakenSeats(OD, DO));
        model.addAttribute("numOfUsers",statsService.getNumberOfUsersReg(OD, DO));
        return "adminPanelPage";
    }

    @GetMapping("/admin/period")
    public String adminByPeriod(
            @RequestParam(name = "dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(name = "dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            HttpSession session, Model model) {

        LocalDate OD = dateFrom;
        LocalDate DO = dateTo;


        List<Movie> moviesList = this.movieService.getAllMovies();
        model.addAttribute("movies", moviesList);
        List<Seans> screenings = this.repertuarService.getAllSeans();
        model.addAttribute("screenings", screenings);

        model.addAttribute("statsType","Statistics from" + OD +" to "+ DO);

        model.addAttribute("ticketPrice", Reservation.TICKET_COST);
        model.addAttribute("numOfOrders",statsService.getNumberOfOrders(OD, DO));
        model.addAttribute("numOfScreenings",statsService.getNumberOfScreenings(OD, DO));
        model.addAttribute("numOfMovies",statsService.getNumberofMoviesShown(OD, DO));
        model.addAttribute("mostPopular",statsService.getMostPopularMovie(OD, DO));
        model.addAttribute("numOfSeats",statsService.getSoldSeats(OD, DO));
        model.addAttribute("moneyEarned",statsService.getMoneyEarned(OD, DO));
        model.addAttribute("earnings",statsService.getEarningsPerOrder(OD, DO));
        model.addAttribute("percentOfSeats",statsService.getPercentOfTakenSeats(OD, DO));
        model.addAttribute("numOfUsers",statsService.getNumberOfUsersReg(OD, DO));

        return "adminPanelPage";
    }

    @GetMapping("/admin/day")
    public String adminByDay(
            @RequestParam(name = "selectedDay")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDay,
            HttpSession session, Model model) {

        LocalDate OD = selectedDay;
        LocalDate DO = selectedDay;

        List<Movie> moviesList = this.movieService.getAllMovies();
        model.addAttribute("movies", moviesList);
        List<Seans> screenings = this.repertuarService.getAllSeans();
        model.addAttribute("screenings", screenings);

        model.addAttribute("statsType","Statistics for " + OD);

        model.addAttribute("ticketPrice", Reservation.TICKET_COST);
        model.addAttribute("numOfOrders",statsService.getNumberOfOrders(OD, DO));
        model.addAttribute("numOfScreenings",statsService.getNumberOfScreenings(OD, DO));
        model.addAttribute("numOfMovies",statsService.getNumberofMoviesShown(OD, DO));
        model.addAttribute("mostPopular",statsService.getMostPopularMovie(OD, DO));
        model.addAttribute("numOfSeats",statsService.getSoldSeats(OD, DO));
        model.addAttribute("moneyEarned",statsService.getMoneyEarned(OD, DO));
        model.addAttribute("earnings",statsService.getEarningsPerOrder(OD, DO));
        model.addAttribute("percentOfSeats",statsService.getPercentOfTakenSeats(OD, DO));
        model.addAttribute("numOfUsers",statsService.getNumberOfUsersReg(OD, DO));

        return "adminPanelPage";
    }

    @GetMapping("/admin/month")
    public String adminByMonth(
            @RequestParam(name = "selectedMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth selectedMonth,
            HttpSession session, Model model) {

        LocalDate OD = selectedMonth.atDay(1);
        LocalDate DO = selectedMonth.atEndOfMonth();

        List<Movie> moviesList = this.movieService.getAllMovies();
        model.addAttribute("movies", moviesList);
        List<Seans> screenings = this.repertuarService.getAllSeans();
        model.addAttribute("screenings", screenings);

        model.addAttribute("statsType","Statistics from " + OD +" to "+ DO);

        model.addAttribute("ticketPrice", Reservation.TICKET_COST);
        model.addAttribute("numOfOrders",statsService.getNumberOfOrders(OD, DO));
        model.addAttribute("numOfScreenings",statsService.getNumberOfScreenings(OD, DO));
        model.addAttribute("numOfMovies",statsService.getNumberofMoviesShown(OD, DO));
        model.addAttribute("mostPopular",statsService.getMostPopularMovie(OD, DO));
        model.addAttribute("numOfSeats",statsService.getSoldSeats(OD, DO));
        model.addAttribute("moneyEarned",statsService.getMoneyEarned(OD, DO));
        model.addAttribute("earnings",statsService.getEarningsPerOrder(OD, DO));
        model.addAttribute("percentOfSeats",statsService.getPercentOfTakenSeats(OD, DO));
        model.addAttribute("numOfUsers",statsService.getNumberOfUsersReg(OD, DO));

        return "adminPanelPage";
    }
}
