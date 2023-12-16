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
        LocalDate testFrom = LocalDate.of(2000, 1, 1);
        LocalDate testTo =  LocalDate.of(3000, 1, 1);
        String title = "Statistics of all time";
        return processAdminRequest(testFrom,testTo, model, title);
    }

    @GetMapping("/admin/period")
    public String adminByPeriod(
            @RequestParam(name = "dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(name = "dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            HttpSession session, Model model) {
        String title = "Statistics from " + dateFrom + " to " + dateTo;
        return processAdminRequest(dateFrom, dateTo, model, title );
    }

    @GetMapping("/admin/day")
    public String adminByDay(
            @RequestParam(name = "selectedDay") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDay,
            HttpSession session, Model model) {
        String title = "Statistics for " + selectedDay;
        return processAdminRequest(selectedDay, selectedDay.plusDays(1), model, title);
        //TODO: check dates range
    }

    @GetMapping("/admin/month")
    public String adminByMonth(
            @RequestParam(name = "selectedMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth selectedMonth,
            HttpSession session, Model model) {
        LocalDate startDate = selectedMonth.atDay(1);
        LocalDate endDate = selectedMonth.atEndOfMonth();
        String title = "Statistics from " + startDate + " to " + endDate;
        return processAdminRequest(startDate, endDate, model, title );
    }

    private String processAdminRequest(LocalDate startDate, LocalDate endDate, Model model, String statsType) {
        List<Movie> moviesList = this.movieService.getAllMovies();
        model.addAttribute("movies", moviesList);

        List<Seans> screenings = this.repertuarService.getAllSeans();
        model.addAttribute("screenings", screenings);

        model.addAttribute("statsType", statsType);

        model.addAttribute("ticketPrice", Reservation.TICKET_COST);
        model.addAttribute("numOfOrders", statsService.getNumberOfOrders(startDate, endDate));
        model.addAttribute("numOfScreenings", statsService.getNumberOfScreenings(startDate, endDate));
        model.addAttribute("numOfMovies", statsService.getNumberofMoviesShown(startDate, endDate));
        model.addAttribute("mostPopular", statsService.getMostPopularMovie(startDate, endDate));
        model.addAttribute("numOfSeats", statsService.getSoldSeats(startDate, endDate));
        model.addAttribute("moneyEarned", statsService.getMoneyEarned(startDate, endDate));
        model.addAttribute("earnings", statsService.getEarningsPerOrder(startDate, endDate));
        model.addAttribute("percentOfSeats", statsService.getPercentOfTakenSeats(startDate, endDate));
        model.addAttribute("numOfUsers", statsService.getNumberOfUsersReg(startDate, endDate));

        return "adminPanelPage";
    }
}
