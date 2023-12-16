package com.tje.cinema.domain;

import com.tje.cinema.interfaces.AdminUserInterface;

import java.time.LocalDateTime;

public class AdminUser extends User implements AdminUserInterface {
    private LocalDateTime dateAdminGranted;
    public AdminUser(){}

    public AdminUser(String email,String name, String password) {
        super(email,name,password);
        this.dateAdminGranted = LocalDateTime.now();
        this.setUserType(UserType.ADMIN);
    }


    public LocalDateTime getDateAdminGranted() {
        return dateAdminGranted;
    }

    public void setDateAdminGranted(LocalDateTime dateAdminGranted) {
        this.dateAdminGranted = dateAdminGranted;
    }

    @Override
    public String toString(){
        return "User:{" +
                " id: " + getId()+
                ", email: "+ getEmail() +
                ", name: "+ getName() +
                ", password: "+ getPassword() +
                ", userType: "+ getUserType() +
                ", DOR: "+ getDateOfRegistration() +
                "}";
    }

}
