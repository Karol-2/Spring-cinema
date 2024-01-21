package com.tje.cinema.repositories;

import com.tje.cinema.domain.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE m.title = :title")
    Optional<Movie> findByTitle(String title);

    @Query("SELECT m, COUNT(r) AS reservationCount " +
            "FROM Movie m " +
            "JOIN Screening s ON m.id = s.movie.id " +
            "JOIN Reservation r ON s.screeningId = r.screening.screeningId " +
            "JOIN Order o ON r.order.orderId = o.orderId " +
            "WHERE o.date BETWEEN :startDate AND :endDate " +
            "GROUP BY m.id " +
            "ORDER BY reservationCount DESC")
    List<Movie> findMostPopularMovieInDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


}
