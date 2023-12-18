package com.tje.cinema.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsService {


    private final OrderService orderService;
    private final RepertuarService repertuarService;
    private final UserService userService;
    @Autowired
    public StatsService(OrderService orderService, RepertuarService repertuarService, UserService userService) {
        this.orderService = orderService;
        this.repertuarService = repertuarService;
        this.userService = userService;
    }

    public long getNumberOfOrders(LocalDate dateFrom, LocalDate dateTo){
        return this.orderService.getOrders().stream()
                .filter(order -> order.getDate().isAfter(dateFrom.atStartOfDay())
                        && order.getDate().isBefore(dateTo.atStartOfDay()))
                .count();
    }

    public long getNumberOfScreenings(LocalDate dateFrom, LocalDate dateTo){
        return this.repertuarService.getAllscreening().stream()
                .filter(showing -> showing.getDateAndTime().isAfter(dateFrom.atStartOfDay())
                        && showing.getDateAndTime().isBefore(dateTo.atStartOfDay()))
                .count();
    }

    public long getNumberofMoviesShown(LocalDate dateFrom, LocalDate dateTo){
        return this.repertuarService.getAllscreening().stream()
                .filter(showing -> showing.getDateAndTime().isAfter(dateFrom.atStartOfDay())
                        && showing.getDateAndTime().isBefore(dateTo.atStartOfDay()))
                .map( showing -> showing.getMovie())
                .distinct()
                .count();
    }

    public String getMostPopularMovie(LocalDate dateFrom, LocalDate dateTo) {
        Map<String, Long> movieFrequencyMap = this.repertuarService.getAllscreening().stream()
                .filter(show -> show.getDateAndTime().isAfter(dateFrom.atStartOfDay())
                        && show.getDateAndTime().isBefore(dateTo.atStartOfDay()))
                .collect(Collectors.groupingBy(
                        show -> show.getMovie().getTitle(),
                        Collectors.summingLong(show -> show.getTakenSeats().size())
                ));

        if (movieFrequencyMap.values().stream().allMatch(count -> count == 0)) {
            return "-";
        }

        return movieFrequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("-");
    }

    public long getSoldSeats(LocalDate dateFrom, LocalDate dateTo) {
        return this.orderService.getOrders().stream()
                .filter(order -> order.getDate().isAfter(dateFrom.atStartOfDay())
                        && order.getDate().isBefore(dateTo.atStartOfDay()))
                .flatMap(order -> order.getReservations().stream()
                        .map(reservation -> reservation.getReservedSeats()))
                .mapToLong(list -> list.size())
                .sum();
    }

    public double getMoneyEarned(LocalDate dateFrom, LocalDate dateTo) {
        return this.orderService.getOrders().stream()
                .filter(order -> order.getDate().isAfter(dateFrom.atStartOfDay())
                        && order.getDate().isBefore(dateTo.atStartOfDay()))
                .mapToDouble(order -> order.getPrice())
                .reduce(0.0, (acc, price) -> acc + price);
    }

    public double getEarningsPerOrder(LocalDate dateFrom, LocalDate dateTo){
        if (this.getNumberOfOrders(dateFrom,dateTo) > 0){
            return this.getMoneyEarned(dateFrom,dateTo) / this.getNumberOfOrders(dateFrom,dateTo);
        }
        return 0;
    }

    public double getPercentOfTakenSeats(LocalDate dateFrom, LocalDate dateTo){
        return this.orderService.getOrders().stream()
                .filter(order -> order.getDate().isAfter(dateFrom.atStartOfDay())
                        && order.getDate().isBefore(dateTo.atStartOfDay()))
                .flatMap(order -> order.getReservations().stream()
                        .map(reservation -> reservation.getReservedSeats()))
                .mapToLong(list -> list.size())
                .average()
                .orElse(0.0);
    }

    public long getNumberOfUsersReg(LocalDate dateFrom, LocalDate dateTo){
        return this.userService.getUserList().stream()
            .filter(usr -> usr.getDateOfRegistration().isEqual(dateFrom)
                    || (usr.getDateOfRegistration().isAfter(ChronoLocalDate.from(dateFrom.atStartOfDay()))
                    && usr.getDateOfRegistration().isBefore(ChronoLocalDate.from(dateTo.atStartOfDay()))
                    ))
            .count();
    }
}
