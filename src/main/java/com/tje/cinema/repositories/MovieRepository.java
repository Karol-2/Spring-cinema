package com.tje.cinema.repositories;

import com.tje.cinema.domain.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE m.title = :title")
    Optional<Movie> findByTitle(String title);


}
