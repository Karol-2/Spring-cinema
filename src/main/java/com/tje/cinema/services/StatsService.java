package com.tje.cinema.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class StatsService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private RepertuarService repertuarService;
    @Autowired
    private UserService userService;

    public long getNumberOfOrders(LocalDate dateFrom, LocalDate dateTo){
        return this.orderService.getOrders().stream()
                .filter(order -> order.getDate().isAfter(dateFrom.minusDays(1).atStartOfDay())
                        && order.getDate().isBefore(dateTo.plusDays(1).atStartOfDay()))
                .count();
    }

    public long getNumberOfScreenings(LocalDate dateFrom, LocalDate dateTo){
        return this.repertuarService.getAllSeans().stream()
                .filter(showing -> showing.getDateAndTime().isAfter(dateFrom.minusDays(1).atStartOfDay())
                        && showing.getDateAndTime().isBefore(dateTo.plusDays(1).atStartOfDay()))
                .count();
    }

    public long getNumberofMoviesShown(LocalDate dateFrom, LocalDate dateTo){
        return this.repertuarService.getAllSeans().stream()
                .filter(showing -> showing.getDateAndTime().isAfter(dateFrom.minusDays(1).atStartOfDay())
                        && showing.getDateAndTime().isBefore(dateTo.plusDays(1).atStartOfDay()))
                .map( showing -> showing.getMovie())
                .distinct()
                .count();
    }

    public String getMostPopularMovie(LocalDate dateFrom, LocalDate dateTo){
        return "TEST";
        //TODO: implement
    }

    public long getSoldSeats(LocalDate dateFrom, LocalDate dateTo) {
        return this.orderService.getOrders().stream()
                .filter(order -> order.getDate().isAfter(dateFrom.minusDays(1).atStartOfDay())
                        && order.getDate().isBefore(dateTo.plusDays(1).atStartOfDay()))
                .map(order -> order.getReservations().stream()
                        .map(res -> res.getReservedSeats())
                        .collect(Collectors.toList()))
                .mapToLong(arr -> arr.size())
                .sum();

    }

    public double getMoneyEarned(LocalDate dateFrom, LocalDate dateTo) {
        return this.orderService.getOrders().stream()
                .filter(order -> order.getDate().isAfter(dateFrom.minusDays(1).atStartOfDay())
                        && order.getDate().isBefore(dateTo.plusDays(1).atStartOfDay()))
                .mapToDouble(order -> order.getPrice())
                .reduce(0.0, (acc, price) -> acc + price);
    }

    public double getEarningsPerCustomer(LocalDate dateFrom, LocalDate dateTo){
        if (this.getNumberOfOrders(dateFrom,dateTo) > 0){
            return this.getMoneyEarned(dateFrom,dateTo) / this.getNumberOfOrders(dateFrom,dateTo);
        }
        return 0;
    }

    public double getPercentOfTakenSeats(LocalDate dateFrom, LocalDate dateTo){
        return 404.13;
        //TODO: implement
    }

    public long getNumberOfUsersReg(LocalDate dateFrom, LocalDate dateTo){
//        return this.userService.getUserList().stream()
//            .filter(usr -> usr.getDateOfRegistration().isAfter(ChronoLocalDate.from(dateFrom.minusDays(1).atStartOfDay()))
//                    && usr.getDateOfRegistration().isBefore(ChronoLocalDate.from(dateTo.plusDays(1).atStartOfDay())))
//            .count();
        //TODO: Check poprawność dodawania pola date
        return 404;
    }


}
