package com.tje.cinema.repositories;

import com.tje.cinema.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.screening.screeningId = :screeningId")
    List<Reservation> getReservationsByScreeningId(@Param("screeningId") Long screeningId);

    @Query("SELECT r FROM Reservation r WHERE r.order.orderId = :orderId")
    List<Reservation> getReservationsByOrderId(@Param("orderId") long orderId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Reservation r WHERE r.screening.screeningId = :screeningId")
    void deleteByScreeningId(Long screeningId);

    @Query("SELECT r FROM Reservation r JOIN r.order o WHERE o.date BETWEEN :startTime AND :endTime")
    List<Reservation> findReservationsByOrderDateBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
