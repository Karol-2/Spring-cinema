package com.tje.cinema.domain;

import java.util.List;

public class Reservation {

    public static double TICKET_COST = 20.99;
    private Long screeningId;
    private Screening screening;
    private List<String> reservedSeats;
    private User user;
    private double reservationCost;


    public Reservation(){}
    public Reservation(Long screeningId, List<String> reservedSeats, User user){
        this.screeningId = screeningId;
        this.reservedSeats = reservedSeats;
        this.user = user;
        this.reservationCost = reservedSeats.size() * this.TICKET_COST;
    }
    public Reservation(Screening screening, List<String> reservedSeats, User user){
        this.screeningId = screening.getScreeningId();
        this.screening = screening;
        this.reservedSeats = reservedSeats;
        this.user = user;
        this.reservationCost = reservedSeats.size() * this.TICKET_COST;
    }

    public Long getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(Long screeningId) {
        this.screeningId = screeningId;
    }

    public Screening getScreening() {
        return screening;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
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
                ", screening: "+ getScreening() +
                "}";
    }
}
