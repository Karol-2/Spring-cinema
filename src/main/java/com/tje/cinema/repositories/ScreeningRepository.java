package com.tje.cinema.repositories;

import com.tje.cinema.domain.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    @Modifying
    @Query("DELETE FROM Screening s WHERE s.movie.id = :movieId")
    void deleteByMovieId(@Param("movieId") long movieId);
}
