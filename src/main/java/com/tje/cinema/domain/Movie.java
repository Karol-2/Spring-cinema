package com.tje.cinema.domain;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private long id;
    private String title;
    private String genre;
    private int year;
    private String director;
    private String trailerLink;
    private List<String> photos;
    public Movie(){}

    public Movie(String title, String genre, int year, String director, String trailerLink, ArrayList<String> photos) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.director = director;
        this.trailerLink = trailerLink;
        this.photos = photos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
