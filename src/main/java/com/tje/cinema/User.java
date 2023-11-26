package com.tje.cinema;

import java.util.ArrayList;

public class User {
    private int id;
    private String email;
    private String password;
    private UserType userType;
    private ArrayList<Order> orders;


    public User(int id, String email,String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userType = UserType.REGISTERED;
        this.orders = new ArrayList<Order>();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public static enum UserType {
        REGISTERED, ADMIN
    }

}
