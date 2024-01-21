package com.tje.cinema.services;

import com.tje.cinema.domain.Movie;
import com.tje.cinema.repositories.MovieRepository;
import com.tje.cinema.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.List;

@Service
public class StatsService {


    private final OrderService orderService;
    private final ScreeningService screeningService;
    private final UserService userService;
    private final ReservationRepository reservationRepository;
    private final MovieRepository movieRepository;
    @Autowired
    public StatsService(MovieRepository movieRepository,ReservationRepository reservationRepository,OrderService orderService, ScreeningService screeningService, UserService userService) {
        this.orderService = orderService;
        this.screeningService = screeningService;
        this.userService = userService;
        this.reservationRepository = reservationRepository;
        this.movieRepository = movieRepository;
    }

    public long getNumberOfOrders(LocalDate dateFrom, LocalDate dateTo){
        return this.orderService.getOrders().stream()
                .filter(order -> (order.getDate().isEqual(dateFrom.atStartOfDay()) || order.getDate().isAfter(dateFrom.atStartOfDay()))
                        && order.getDate().isBefore(dateTo.plusDays(1).atStartOfDay()))
                .count();
    }

    public long getNumberOfScreenings(LocalDate dateFrom, LocalDate dateTo){
        return this.screeningService.getAllscreening().stream()
                .filter(showing -> (showing.getDateAndTime().isEqual(dateFrom.atStartOfDay()) || showing.getDateAndTime().isAfter(dateFrom.atStartOfDay()))
                        && showing.getDateAndTime().isBefore(dateTo.plusDays(1).atStartOfDay()))
                .count();
    }

    public long getNumberofMoviesShown(LocalDate dateFrom, LocalDate dateTo){
        return this.screeningService.getAllscreening().stream()
                .filter(showing -> (showing.getDateAndTime().isEqual(dateFrom.atStartOfDay()) || showing.getDateAndTime().isAfter(dateFrom.atStartOfDay()))
                        && showing.getDateAndTime().isBefore(dateTo.plusDays(1).atStartOfDay()))
                .map(showing -> showing.getMovie())
                .distinct()
                .count();
    }

    public String getMostPopularMovie(LocalDate dateFrom, LocalDate dateTo) {

      List<Movie> result = this.movieRepository.findMostPopularMovieInDateRange(dateFrom.atStartOfDay(), dateTo.atTime(23,59,59));
      if(result.size() > 0){
          return result.get(0).getTitle();
      }

      return "-";
    }

    public long getSoldSeats(LocalDate dateFrom, LocalDate dateTo) {
        return this.reservationRepository.findReservationsByOrderDateBetween(dateFrom.atStartOfDay(), dateTo.atTime(23,59,59))
                .stream()
                .map(reservation -> reservation.getReservedSeats())
                .mapToLong(list -> list.size())
                .sum();
    }

    public double getMoneyEarned(LocalDate dateFrom, LocalDate dateTo) {
        return this.orderService.getOrders().stream()
                .filter(order -> (order.getDate().isEqual(dateFrom.atStartOfDay()) || order.getDate().isAfter(dateFrom.atStartOfDay()))
                        && order.getDate().isBefore(dateTo.plusDays(1).atStartOfDay()))
                .mapToDouble(order -> order.getPrice())
                .reduce(0.0, (acc, price) -> acc + price);
    }

    public double getEarningsPerOrder(LocalDate dateFrom, LocalDate dateTo){
        if (this.getNumberOfOrders(dateFrom,dateTo) > 0){
            return this.getMoneyEarned(dateFrom,dateTo) / this.getNumberOfOrders(dateFrom,dateTo);
        }
        return 0;
    }


    public long getNumberOfUsersReg(LocalDate dateFrom, LocalDate dateTo){
        System.out.println(dateFrom+" to "+dateTo);
        return this.userService.getUserList().stream()
                .filter(usr -> (usr.getDateOfRegistration().isAfter(ChronoLocalDate.from(dateFrom.atStartOfDay()))
                        || usr.getDateOfRegistration().isEqual(ChronoLocalDate.from(dateFrom.atStartOfDay())))
                        && usr.getDateOfRegistration().isBefore(ChronoLocalDate.from(dateTo.plusDays(1).atStartOfDay())))

                .count();
    }
}
