package com.tje.cinema.services;

import com.tje.cinema.domain.Reservation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    // tu trzymam wszystkie rezerwacje: mogę dodać rezerwację, usunąć,
    // pobrać wszystkie rezerwacje danego użytkownika, stowrzyć z rezerwacji Order
    private final List<Reservation> reservationDatabase = new ArrayList<>();
    private long orderIdCounter = 1;

    public void addReservation(Reservation reservation){
        reservation.setId(orderIdCounter++);
        reservationDatabase.add(reservation);
        System.out.println("Dodano Rezerwacje z id: "+reservation.getId());
    }
    public void removeReservation(Reservation reservation){
        reservationDatabase.remove(reservation);
        System.out.println("Usunieto Rezerwacje z id: "+reservation.getId());
    }

    public List<Reservation> getReservationsByUserId(long userId){
        return reservationDatabase.stream()
                .filter(reservation -> reservation.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }








}
