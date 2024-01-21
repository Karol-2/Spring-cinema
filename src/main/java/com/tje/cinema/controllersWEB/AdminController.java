package com.tje.cinema.controllersWEB;

import com.tje.cinema.domain.*;
import com.tje.cinema.services.MovieService;
import com.tje.cinema.services.ScreeningService;
import com.tje.cinema.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Controller
public class AdminController {
    private final MovieService movieService;
    private final ScreeningService screeningService;
    private final StatsService statsService;

    @Autowired
    public AdminController(MovieService movieService, ScreeningService screeningService, StatsService statsService){
        this.movieService = movieService;
        this.screeningService = screeningService;
        this.statsService = statsService;
    }

    @GetMapping("/admin")
    public String admin(@RequestParam(name = "message", required = false) String message,
                        HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)){
            redirectAttributes.addAttribute("error","You don't have access to this page.");
            return "redirect:/";
        }
        LocalDate testFrom = LocalDate.of(2000, 1, 1);
        LocalDate testTo =  LocalDate.of(3000, 1, 1);
        String title = "Statistics of all time";
        model.addAttribute("message",message);
        return processAdminRequest(testFrom,testTo, model, title);
    }

    @GetMapping("/admin/period")
    public String adminByPeriod(
            @RequestParam(name = "dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(name = "dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            HttpSession session, Model model) {
        if (!isAdmin(session)){
            model.addAttribute("error","You don't have access to this page.");
            return "redirect:/";
        }
        String title = "Statistics from " + dateFrom + " to " + dateTo;
        return processAdminRequest(dateFrom, dateTo, model, title );
    }

    @GetMapping("/admin/day")
    public String adminByDay(
            @RequestParam(name = "selectedDay") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDay,
            HttpSession session, Model model) {
        if (!isAdmin(session)){
            model.addAttribute("error","You don't have access to this page.");
            return "redirect:/";
        }
        String title = "Statistics for " + selectedDay;
        return processAdminRequest(selectedDay, selectedDay, model, title);
    }

    @GetMapping("/admin/month")
    public String adminByMonth(
            @RequestParam(name = "selectedMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth selectedMonth,
            HttpSession session, Model model) {
        if (!isAdmin(session)){
            model.addAttribute("error","You don't have access to this page.");
            return "redirect:/";
        }
        LocalDate startDate = selectedMonth.atDay(1);
        LocalDate endDate = selectedMonth.atEndOfMonth();
        String title = "Statistics from " + startDate + " to " + endDate;
        return processAdminRequest(startDate, endDate, model, title );
    }

    private String processAdminRequest(LocalDate startDate, LocalDate endDate, Model model, String statsType) {
        List<Movie> moviesList = this.movieService.getAllMovies();
        model.addAttribute("movies", moviesList);

        List<Screening> screenings = this.screeningService.getAllscreening();
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
        model.addAttribute("numOfUsers", statsService.getNumberOfUsersReg(startDate, endDate));

        return "adminPanelPage";
    }

    private boolean isAdmin(HttpSession session){
        if (session.getAttribute("user") != null){
            AdminUser adminUser = (AdminUser) session.getAttribute("user");
            return adminUser.getUserType().equals(User.UserType.ADMIN);
        }
        return false;
    }
}
