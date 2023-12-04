package com.tje.cinema.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Seans {
    private Long seansId;
    private long movieId;
    private Movie movie;
    private LocalDateTime dateAndTime;
    private List<String> takenSeats;
    public Seans(){};
    public Seans(Movie movie, LocalDateTime dateAndTime) {
        this.movie = movie;
        this.dateAndTime = dateAndTime;
        this.takenSeats = new ArrayList<>();
    }
    public Seans(int movieId, LocalDateTime dateAndTime) {
        this.movieId = movieId;
        this.dateAndTime = dateAndTime;
        this.takenSeats = new ArrayList<>();
    }
    public Seans(long id,Movie movie,long movieId, LocalDateTime dateAndTime) {
        this.seansId = id;
        this.movie = movie;
        this.movieId = movieId;
        this.dateAndTime = dateAndTime;
        this.takenSeats = new ArrayList<>();
    }
    public String getMovieTitle(){
        return this.movie.getTitle();
    }

    public static Seans createSeansFromString(int movieId, String dateAndTimeString) {
        return new Seans(movieId, LocalDateTime.parse(dateAndTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }

    public Long getSeansId() {
        return seansId;
    }

    public void setSeansId(Long seansId) {
        this.seansId = seansId;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public List<String> getTakenSeats() {
        return takenSeats;
    }

    public void addTakenSeats(String seat) {
       this.takenSeats.add(seat);
    }
    public void removeTakenSeats(String seat) {
        this.takenSeats.remove(seat);
    }

    public void setTakenSeats(List<String> takenSeats) {
        this.takenSeats = takenSeats;
    }

    @Override
    public String toString(){
        return "Seans:{" +
                " id: " + getSeansId()+
                ", movie: "+ getMovie() +
                ", dateTime: "+ getDateAndTime() +
                ", movieId: "+ getMovieId() +
                ", takenSeats: "+ getTakenSeats() +
                "}";
    }
}
