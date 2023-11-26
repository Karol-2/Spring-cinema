package com.tje.cinema;

import java.time.LocalDateTime;

public class Seans {
    private String seansId;
    private int movieId;
    private LocalDateTime dateAndTime;

    public Seans(String seansId, int movieId, LocalDateTime dateAndTime) {
        this.seansId = seansId;
        this.movieId = movieId;
        this.dateAndTime = dateAndTime;
    }

    public String getSeansId() {
        return seansId;
    }

    public void setSeansId(String seansId) {
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
}
