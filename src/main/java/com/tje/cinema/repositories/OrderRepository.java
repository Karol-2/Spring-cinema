package com.tje.cinema.repositories;

import com.tje.cinema.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.date = :targetDate")
    List<Order> findOrderByDate(@Param("targetDate") LocalDateTime targetDate);

    @Modifying
    @Query("UPDATE Order o SET o.status = 'COMPLETED' WHERE o.orderId = :orderFinalizedId")
    void finalizeOrder(@Param("orderFinalizedId") Long orderFinalizedId);

    @Modifying
    @Query("UPDATE Order o SET o.status = 'CANCELLED' WHERE o.orderId IN " +
            "(SELECT r FROM Reservation r WHERE r.screening.movie.id = :movieId)")
    void cancelEveryOrderOfMovie(@Param("movieId") Long movieId);

    @Modifying
    @Query("UPDATE Order o SET o.status = 'CANCELLED' WHERE o.orderId IN " +
            "(SELECT r FROM Reservation r WHERE r.screening.screeningId = :screeningId)")
    void cancelEveryOrderOfscreening(@Param("screeningId") Long screeningId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> getOrdersByUserId(@Param("userId") Long userId);


}
