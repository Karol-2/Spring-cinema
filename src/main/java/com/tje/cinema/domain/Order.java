package com.tje.cinema.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private long orderId;
    private double price;
    private List<Reservation> reservations;
    private LocalDateTime date;
    private User user;
    private OrderStatus status;

    public Order() {}
    public Order(double price, List<Reservation> reservations,LocalDateTime date,User user ){
        this.price = price;
        this.reservations = reservations;
        this.date = date;
        this.user = user;
        this.status = OrderStatus.NEW;
    }
    public void addReservation(Reservation reservation){
        this.reservations.add(reservation);
    }
    public enum OrderStatus{
        NEW, CANCELLED, COMPLETED
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return "Order:{" +
                " id: " + getOrderId()+
                ", userId: "+ getUser().getId() +
                ", reservations: "+ getReservations() +
                ", status: "+ getStatus() +
                ", date: "+ getDate() +
                ", price: "+ getPrice() +
                "}";
    }
}
