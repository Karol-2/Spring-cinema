package com.tje.cinema;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Reservation {
    private int id;
    private LocalDateTime date;
    private double price;
    private ArrayList<String> seats;

    public Reservation(int id, LocalDateTime date, double price, ArrayList<String> seats) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.seats = seats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<String> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<String> seats) {
        this.seats = seats;
    }
}
