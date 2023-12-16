package com.tje.cinema.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Seans {
    private Long seansId;
    private long movieId;
    private Movie movie;
    private LocalDateTime dateAndTime;
    private HashMap<Long, List<String>> takenSeats;
    private List<List<String>> allSeats;
    public Seans(){
        this.takenSeats = new HashMap<>();
        this.allSeats = this.generateSeatList(10,'A',2);
    };
    public Seans(int movieId, LocalDateTime dateAndTime) {
        this.movieId = movieId;
        this.dateAndTime = dateAndTime;
        this.takenSeats = new HashMap<>();
        this.allSeats = new ArrayList<>();
        this.allSeats = this.generateSeatList(10,'A',2);
    }
    public Seans(long id,Movie movie,long movieId, LocalDateTime dateAndTime) {
        this.seansId = id;
        this.movie = movie;
        this.movieId = movieId;
        this.dateAndTime = dateAndTime;
        this.takenSeats = new HashMap<>();
        this.allSeats = this.generateSeatList(10,'A',2);
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
        return "Seans:{" +
                " id: " + getSeansId()+
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
