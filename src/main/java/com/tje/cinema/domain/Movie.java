package com.tje.cinema.domain;

import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title")
    @NotNull(message = "title is mandatory")
    @NotBlank(message = "title is mandatory")
    @Size(min = 2, max = 100, message = "title length should be between 2 and 100")
    private String title;
    @Column(name = "genre")
    @NotNull(message = "genre field is mandatory")
    @NotBlank(message = "genre field is mandatory")
    @Size(min = 2, max = 25, message = "genre field length should be between 2 and 25")
    private String genre;
    @Column(name = "year")
    @NotNull(message = "year field is mandatory")
    @Min(value = 1900, message = "Year must be greater than or equal to 1900")
    @Max(value = 2024, message = "Year must be less than or equal to 2024")
    private int year;
    @Column(name = "director")
    @NotNull(message = "director field is mandatory")
    @NotBlank(message = "director field is mandatory")
    @Size(min = 2, max = 50, message = "director field length should be between 2 and 50")
    private String director;
    @Column(name = "actors")
    @NotNull(message = "actors field is mandatory")
    @NotBlank(message = "actors field is mandatory")
    @Size(min = 5, max = 150, message = "actors field length should be between 5 and 150")
    private String actors;
    @Column(name = "trailer_link")
    @NotNull(message = "trailerLink field is mandatory")
    @NotBlank(message = "trailerLink field is mandatory")
    @URL(message = "trailerLink should be valid URL")
    private String trailerLink;
    @ElementCollection
    @CollectionTable(name = "movie_photos", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "photo")
    @NotNull(message = "photos field is mandatory")
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
