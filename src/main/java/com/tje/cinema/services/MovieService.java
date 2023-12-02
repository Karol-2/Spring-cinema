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
    public void removeMovieById(int id) {
        movieDatabase.removeIf(movie -> movie.getId() == id);
    }
    public List<Movie> getAllMovies(){
        return this.movieDatabase;
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


    public void setInitialMovies() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        // Dodanie Filmów z beans
        Map<String, Movie> movieBeans = context.getBeansOfType(Movie.class);
        movieBeans.values().forEach(movie -> this.addMovie(movie));
    }

}
