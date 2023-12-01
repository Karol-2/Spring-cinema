package com.tje.cinema.services;

import com.tje.cinema.domain.Movie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    private final List<Movie> movieDatabase = new ArrayList<>();

    public void addMovie(Movie movie){
        this.movieDatabase.add(movie);
    }//TODO: dodaj sprawdzenie czy film z takim id już nie istnieje
    public void removeMovieById(int id) {
        movieDatabase.removeIf(movie -> movie.getId() == id);
    }

    public  ArrayList<Movie> getMoviesByID(int id){
        ArrayList<Movie> result = new ArrayList<Movie>();
        for (Movie movie : this.movieDatabase) {
            if (id == movie.getId()) {
                result.add(movie);
            }
        }
        return result;
    }

}
