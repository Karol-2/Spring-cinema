package com.tje.cinema;

import com.tje.cinema.domain.Movie;

import java.util.ArrayList;

public class MovieDatabase {
    private ArrayList<Movie> movieDatabase = new ArrayList<Movie>();

    public void addMovie(Movie movie){
        this.movieDatabase.add(movie);
    }
    public void removeMovieById(int id){
        for (int i = 0; i < this.movieDatabase.size(); i++) {
            if ( id == this.movieDatabase.get(i).getId()){
                this.movieDatabase.remove(i);
                return;
            }
        }
    }

    public  ArrayList<Movie> getMovieByID(int id){
        ArrayList<Movie> result = new ArrayList<Movie>();
        for (Movie movie : this.movieDatabase) {
            if (id == movie.getId()) {
                result.add(movie);
            }
        }
        return result;
    }
}
