package com.tje.cinema.domain;

import java.util.List;

public class Reservation {

    private final double TICKET_COST = 20.99;
    private Long seansId;
    private Seans seans;
    private List<String> reservedSeats;
    private User user;
    private double reservationCost;


    public Reservation(){}
    public Reservation(Long seansId, List<String> reservedSeats, User user){
        this.seansId = seansId;
        this.reservedSeats = reservedSeats;
        this.user = user;
        this.reservationCost = reservedSeats.size() * this.TICKET_COST;
    }
    public Reservation(Seans seans, List<String> reservedSeats, User user){
        this.seansId = seans.getSeansId();
        this.seans = seans;
        this.reservedSeats = reservedSeats;
        this.user = user;
        this.reservationCost = reservedSeats.size() * this.TICKET_COST;
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
        this.reservationCost = reservedSeats.size() * this.TICKET_COST;
    }
    public void addReservedSeats(String seat) {
        this.reservedSeats.add(seat);
        this.reservationCost = reservedSeats.size() * this.TICKET_COST;
    }
    public void removeReservedSeat(String seat){
        this.reservationCost = reservedSeats.size() * this.TICKET_COST;
        this.reservedSeats.remove(seat);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getTICKET_COST() {
        return TICKET_COST;
    }

    public double getReservationCost() {
        return reservationCost;
    }

    public void setReservationCost(double reservationCost) {
        this.reservationCost = reservationCost;
    }

    @Override
    public String toString(){
        return "Reservation:{" +
                ", userId: "+ getUser().getId() +
                ", seats: "+ getReservedSeats() +
                ", seans: "+ getSeans() +
                "}";
    }
}
