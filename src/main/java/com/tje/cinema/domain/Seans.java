package com.tje.cinema.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Seans {
    private Long seansId;
    private int movieId;
    private Movie movie;
    private LocalDateTime dateAndTime;
    public Seans(){};
    public Seans(Movie movie, LocalDateTime dateAndTime) {
        this.movie = movie;
        this.dateAndTime = dateAndTime;
    }
    public Seans(int movieId, LocalDateTime dateAndTime) {
        this.movieId = movieId;
        this.dateAndTime = dateAndTime;
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

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
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
}
