package com.tje.cinema.domain;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "reservations")
public class Reservation {

    public static double TICKET_COST = 20.99;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;
    @Transient
    private Long screeningId;
    @ManyToOne
    @JoinColumn(name = "screening_id")
    private Screening screening;
    @ElementCollection
    @CollectionTable(name = "reserved_seats", joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "seat")
    private List<String> reservedSeats;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "reservation_cost")
    private double reservationCost;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


    public Reservation(){}
    public Reservation(Long screeningId, List<String> reservedSeats, User user){
        this.screeningId = screeningId;
        this.reservedSeats = reservedSeats;
        this.user = user;
        this.reservationCost = reservedSeats.size() * TICKET_COST;
    }
    public Reservation(Screening screening, List<String> reservedSeats, User user){
        this.screeningId = screening.getScreeningId();
        this.screening = screening;
        this.reservedSeats = reservedSeats;
        this.user = user;
        this.reservationCost = reservedSeats.size() * TICKET_COST;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
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
        this.reservationCost = reservedSeats.size() * TICKET_COST;
    }
    public void addReservedSeats(String seat) {
        this.reservedSeats.add(seat);
        this.reservationCost = reservedSeats.size() * TICKET_COST;
    }
    public void removeReservedSeat(String seat){
        this.reservationCost = reservedSeats.size() * TICKET_COST;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
