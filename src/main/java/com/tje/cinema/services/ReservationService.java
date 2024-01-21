package com.tje.cinema.services;

import com.tje.cinema.domain.Reservation;
import com.tje.cinema.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
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
    public Reservation getReservation(Long id){
        return this.reservationRepository.getOne(id);
    }
    public Reservation addReservation(Reservation newReservation){
        return this.reservationRepository.save(newReservation); //TODO: sprawdź czy order id istnieje, user, screening, czy miejsca nie zajte
    }
    public Reservation editReservation(Reservation editReservation){
        return editReservation; //TODO: implement
    }
    public void removeReservationById(long id) {
        reservationRepository.deleteById(id);
    }

    public void deleteByScreeningId(Long screening_id){
        this.reservationRepository.deleteByScreeningId(screening_id);
    }

}
