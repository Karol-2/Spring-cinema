package com.tje.cinema.domain;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Transactional
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    @Column(name = "price")
    private double price;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
    @Column(name = "date")
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
        this.reservations = new ArrayList<>();
    }

    public Order(List<Reservation> reservations, LocalDateTime date, User user) {
        this.price = calculateCost(reservations);
        this.reservations = reservations;
        this.date = date;
        this.user = user;
        this.status = OrderStatus.NEW;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        this.setPrice(calculateCost(this.getReservations()));
        reservation.setOrder(this);
    }

    public enum OrderStatus {
        NEW, CANCELLED, COMPLETED
    }

    public long getOrderId() { return orderId;}

    public void setOrderId(long orderId) { this.orderId = orderId;}

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price;}

    public List<Reservation> getReservations() {return reservations;}

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
        this.setPrice(calculateCost(this.getReservations()));
    }

    public LocalDateTime getDate() {return date;}

    public void setDate(LocalDateTime date) {this.date = date;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public OrderStatus getStatus() {return status;}

    public void setStatus(OrderStatus status) {this.status = status;}

    @Override
    public String toString() {
        return "Order:{" +
                " id: " + getOrderId() +
                ", userId: " + getUser().getId() +
                ", reservations: " + getReservations() +
                ", status: " + getStatus() +
                ", date: " + getDate() +
                ", price: " + getPrice() +
                "}";
    }

    public double calculateCost(List<Reservation> reservations) {
        return (reservations.stream().map(Reservation::getReservationCost).reduce(0.0, Double::sum));
    }
}
