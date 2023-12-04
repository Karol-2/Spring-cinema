package com.tje.cinema.domain;

import java.util.List;

public class Reservation {

    private Long id;
    private Long seansId;
    private Seans seans;
    private List<String> reservedSeats;
    private User user;

    public Reservation(){};
    public Reservation(Long seansId, List<String> reservedSeats, User user){
        this.seansId = seansId;
        this.reservedSeats = reservedSeats;
        this.user = user;
    };
    public Reservation(Seans seans, List<String> reservedSeats, User user){
        this.seansId = seans.getSeansId();
        this.seans = seans;
        this.reservedSeats = reservedSeats;
        this.user = user;
    };


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSeansId() {
        return seansId;
    }

    public void setSeansId(Long seansId) {
        this.seansId = seansId;
    }

    public Seans getSeans() {
        return seans;
    }

    public void setSeans(Seans seans) {
        this.seans = seans;
    }

    public List<String> getReservedSeats() {
        return reservedSeats;
    }

    public void setReservedSeats(List<String> reservedSeats) {
        this.reservedSeats = reservedSeats;
    }
    public void addReservedSeats(String seat) {
        this.reservedSeats.add(seat);
    }
    public void removeReservedSeat(String seat){
        this.reservedSeats.remove(seat);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
