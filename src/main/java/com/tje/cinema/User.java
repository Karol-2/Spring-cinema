package com.tje.cinema;

public class User {
    private int id;
    private String email;
    private UserType userType;


    public User(int id, String email, UserType userType) {
        this.id = id;
        this.email = email;
        this.userType = userType;
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


    public static enum UserType {
        REGISTERED, ADMIN
    }
}
