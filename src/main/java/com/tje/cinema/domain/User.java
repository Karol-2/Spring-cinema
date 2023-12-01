package com.tje.cinema.domain;

public class User {
    private int id;
    private String email;
    private String password;
    private UserType userType;


    public User(int id, String email,String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userType = UserType.REGISTERED;
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


    public static enum UserType {
        REGISTERED, ADMIN
    }

}
