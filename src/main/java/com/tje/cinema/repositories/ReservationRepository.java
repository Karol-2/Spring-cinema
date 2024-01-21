package com.tje.cinema.repositories;

import com.tje.cinema.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.screening.screeningId = :screeningId")
    List<Reservation> getReservationsByScreeningId(@Param("screeningId") Long screeningId);

    @Query("SELECT r FROM Reservation r WHERE r.order.orderId = :orderId")
    List<Reservation> getReservationsByOrderId(@Param("orderId") long orderId);
}
