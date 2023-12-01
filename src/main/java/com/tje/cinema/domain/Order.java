package com.tje.cinema.domain;

import java.util.ArrayList;

public class Order {
    private int orderId;
    private String seansId;
    private double price;
    private ArrayList<String> seats;

    public Order(int orderId, String seansId, double price, ArrayList<String> seats) {
        this.orderId = orderId;
        this.seansId = seansId;
        this.price = price;
        this.seats = seats;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getSeansId() {
        return seansId;
    }

    public void setSeansId(String seansId) {
        this.seansId = seansId;
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
