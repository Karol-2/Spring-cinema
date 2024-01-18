package com.tje.cinema.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "genre")
    private String genre;
    @Column(name = "year")
    private int year;
    @Column(name = "director")
    private String director;
    @Column(name = "actors")
    private String actors;
    @Column(name = "trailer_link")
    private String trailerLink;
    @ElementCollection
    @CollectionTable(name = "movie_photos", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "photo")
    private List<String> photos;

    public Movie() {}

    public Movie(String title, String genre, int year, String director, String actors, String trailerLink, ArrayList<String> photos) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.director = director;
        this.actors = actors;
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

    public String getActors() { return actors;}

    public void setActors(String actors) { this.actors = actors;}

}
