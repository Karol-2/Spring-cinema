package com.tje.cinema.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "screenings")
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screening_id")
    private Long screeningId;
    @Transient
    private long movieId;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @Column(name = "date_and_time")
    private LocalDateTime dateAndTime;
    @Transient
    private HashMap<Long, List<String>> takenSeats;
    @Transient
    private List<List<String>> allSeats; //TODO: fix seats objects
    public Screening(){
        this.takenSeats = new HashMap<>();
        this.allSeats = this.generateSeatList(10,'A',2);
    };
    public Screening(int movieId, LocalDateTime dateAndTime) {
        this.movieId = movieId;
        this.dateAndTime = dateAndTime;
        this.takenSeats = new HashMap<>();
        this.allSeats = new ArrayList<>();
        this.allSeats = this.generateSeatList(10,'A',2);
    }
    public Screening(long id, Movie movie, long movieId, LocalDateTime dateAndTime) {
        this.screeningId = id;
        this.movie = movie;
        this.movieId = movieId;
        this.dateAndTime = dateAndTime;
        this.takenSeats = new HashMap<>();
        this.allSeats = this.generateSeatList(10,'A',2);
    }
    public String getMovieTitle(){
        return this.movie.getTitle();
    }

    public static Screening createScreeningFromString(int movieId, String dateAndTimeString) {
        return new Screening(movieId, LocalDateTime.parse(dateAndTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    }

    public Long getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(Long screeningId) {
        this.screeningId = screeningId;
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

    public HashMap<Long, List<String>> getTakenSeats() {
        return takenSeats;
    }

    public List<String> getTakenSeatsWithoutId(){
        HashMap<Long, List<String>> takenSeats = this.getTakenSeats();
        List<String> allSeats = new ArrayList<>();

        for (List<String> seats : takenSeats.values()) {
            allSeats.addAll(seats);
        }

        return allSeats;
    }

    public void setTakenSeats(HashMap<Long, List<String>> takenSeats) {
        this.takenSeats = takenSeats;
    }

    public void addTakenSeats(List<String> seats, long orderId) {
        this.takenSeats.put(orderId,seats);
    }

    public void removeTakenSeats(List<String> seats, long orderId) {
        this.takenSeats.remove(orderId,seats);
    }

    public List<List<String>> getAllSeats() {
        return allSeats;
    }

    public void setAllSeats(List<List<String>> allSeats) {
        this.allSeats = allSeats;
    }

    @Override
    public String toString(){
        return "Screening:{" +
                " id: " + getScreeningId()+
                ", movie: "+ getMovie() +
                ", dateTime: "+ getDateAndTime() +
                ", movieId: "+ getMovieId() +
                ", takenSeats: "+ getTakenSeats() +
                ", free: "+ getAllSeats() +

                "}";
    }

    private List<List<String>> generateSeatList(int seatCount, char startRow, int rowCount) {
        List<List<String>> seatList = new ArrayList<>();

        for (int row = 0; row < rowCount; row++) {
            char currentRow = (char) (startRow + row);
            List<String> seatsInRow = new ArrayList<>();

            for (int seatNumber = 1; seatNumber <= seatCount; seatNumber++) {
                String seatLabel = String.format("%s-%d", currentRow, seatNumber);
                seatsInRow.add(seatLabel);
            }

            seatList.add(seatsInRow);
        }

        return seatList;
    }

}
