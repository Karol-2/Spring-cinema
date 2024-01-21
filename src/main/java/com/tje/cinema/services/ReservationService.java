package com.tje.cinema.services;

import com.tje.cinema.domain.Reservation;
import com.tje.cinema.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAll(){
        return this.reservationRepository.findAll();
    }
    public List<Reservation> getReservationsByOrderId(Long orderId){
        return this.reservationRepository.getReservationsByOrderId(orderId);
    }

}
