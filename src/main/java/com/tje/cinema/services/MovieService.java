package com.tje.cinema.services;

import com.tje.cinema.domain.Movie;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    public MovieService() {
        this.setInitialMovies();
    }
    private final List<Movie> movieDatabase = new ArrayList<>();

    private long movieIdCounter = 1;

    public void addMovie(Movie movie){
        movie.setId(movieIdCounter++);
        this.movieDatabase.add(movie);
        System.out.println("Dodano film: "+movie.getTitle());
    }
    public void removeMovieById(long id) {
        movieDatabase.removeIf(movie -> movie.getId() == id);
    }
    public List<Movie> getAllMovies(){
        return this.movieDatabase;
    }
    public Movie getMovieById(long id) throws RuntimeException {
        return movieDatabase.stream()
                .filter(movie -> movie.getId()==(id))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Movie not found with id: " + id));
    }

    public void editMovie(long id, Movie updatedMovie) {
        Movie existingMovie = getMovieById(id);

        if (existingMovie != null) {
            existingMovie.setTitle(updatedMovie.getTitle());
            existingMovie.setGenre(updatedMovie.getGenre());
            existingMovie.setYear(updatedMovie.getYear());
            existingMovie.setDirector(updatedMovie.getDirector());
            existingMovie.setActors(updatedMovie.getActors());
            existingMovie.setTrailerLink(updatedMovie.getTrailerLink());
            existingMovie.setPhotos(updatedMovie.getPhotos());

            System.out.println("Zaktualizowano film: " + existingMovie.getTitle());
        } else {

            throw new RuntimeException("Movie not found with id: " + id);
        }
    }





    public void setInitialMovies() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        // Dodanie Filmów z beans
        Map<String, Movie> movieBeans = context.getBeansOfType(Movie.class);
        movieBeans.values().forEach(movie -> this.addMovie(movie));
    }

}
