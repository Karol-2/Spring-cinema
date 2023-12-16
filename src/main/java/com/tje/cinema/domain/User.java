package com.tje.cinema.domain;

import com.tje.cinema.interfaces.UserInterface;

import java.time.LocalDate;

public class User implements UserInterface {

    private Long id;
    private String email;
    private String name;

    private String password;
    private UserType userType;
    private LocalDate dateOfRegistration;

    public User() {
        this.dateOfRegistration = LocalDate.now();
    }

    public User(String email, String name,String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.userType = UserType.REGISTERED;
        this.dateOfRegistration = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDate getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDate dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    @Override
    public String toString(){
        return "User:{" +
                " id: " + getId()+
                ", email: "+ getEmail() +
                ", name: "+ getName() +
                ", password: "+ getPassword() +
                ", DOR: "+ getDateOfRegistration() +
                "}";
    }

    public static enum UserType {
        REGISTERED, ADMIN
    }


}
