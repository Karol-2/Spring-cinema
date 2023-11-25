package com.tje.cinema;

import java.util.ArrayList;

public class User {
    private int id;
    private String email;
    private UserType userType;
    private ArrayList<Reservation> reservations;


    public User(int id, String email, UserType userType) {
        this.id = id;
        this.email = email;
        this.userType = userType;
        this.reservations = new ArrayList<Reservation>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public static enum UserType {
        REGISTERED, ADMIN
    }

}
