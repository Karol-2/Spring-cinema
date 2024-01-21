package com.tje.cinema.controllersREST;

import com.tje.cinema.services.StatsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/stats")
public class RestStatsController {
    private final StatsService statsService;

    public RestStatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/number_of_orders")
    public ResponseEntity<?> getNumberOfOrders(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                         @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.badRequest().body("Invalid date range. 'from' date must be on or before 'to' date.");
        }
        return ResponseEntity.ok(this.statsService.getNumberOfOrders(fromDate,toDate));
    }
    @GetMapping("/number_of_screenings")
    public ResponseEntity<?> getNumberOfScreenings(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                         @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.badRequest().body("Invalid date range. 'from' date must be on or before 'to' date.");
        }
        return ResponseEntity.ok(this.statsService.getNumberOfScreenings(fromDate,toDate));
    }
    @GetMapping("/number_of_movies")
    public ResponseEntity<?> getNumberMoviesShown(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                  @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.badRequest().body("Invalid date range. 'from' date must be on or before 'to' date.");
        }
        return ResponseEntity.ok(this.statsService.getNumberofMoviesShown(fromDate,toDate));
    }
    @GetMapping("/most_popular_movie")
    public ResponseEntity<?> getMostPopularMovie(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                  @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.badRequest().body("Invalid date range. 'from' date must be on or before 'to' date.");
        }
        return ResponseEntity.ok(this.statsService.getMostPopularMovie(fromDate,toDate));
    }
    @GetMapping("/sold_seats")
    public ResponseEntity<?> getSoldSeats(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                  @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.badRequest().body("Invalid date range. 'from' date must be on or before 'to' date.");
        }
        return ResponseEntity.ok(this.statsService.getSoldSeats(fromDate,toDate));
    }
    @GetMapping("/money_earned")
    public ResponseEntity<?> getMoneyEarned(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                  @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.badRequest().body("Invalid date range. 'from' date must be on or before 'to' date.");
        }
        return ResponseEntity.ok(this.statsService.getMoneyEarned(fromDate,toDate));
    }
    @GetMapping("/earning_per_order")
    public ResponseEntity<?> getEarningsPerOrder(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                  @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.badRequest().body("Invalid date range. 'from' date must be on or before 'to' date.");
        }
        return ResponseEntity.ok(this.statsService.getEarningsPerOrder(fromDate,toDate));
    }
    @GetMapping("/number_of_users")
    public ResponseEntity<?> getNumberOfUsers(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                  @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            return ResponseEntity.badRequest().body("Invalid date range. 'from' date must be on or before 'to' date.");
        }
        return ResponseEntity.ok(this.statsService.getNumberOfUsersReg(fromDate,toDate));
    }

//TODO: handler gdy brakuje paramsów albo zła forma

}
