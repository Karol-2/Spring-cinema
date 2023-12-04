package com.tje.cinema.domain;

import com.tje.cinema.interfaces.AdminUserInterface;

import java.time.LocalDateTime;

public class AdminUser extends User implements AdminUserInterface {
    private boolean hasAdminAccess;
    private LocalDateTime dateAdminGranted;
    public AdminUser(){}

    public AdminUser(String email,String name, String password,boolean hasAdminAccess) {
        super(email,name,password);
        this.hasAdminAccess = hasAdminAccess;
        this.dateAdminGranted = LocalDateTime.now();
        this.setUserType(UserType.ADMIN);
    }

    public boolean isHasAdminAccess() {
        return hasAdminAccess;
    }

    public void setHasAdminAccess(boolean hasAdminAccess) {
        this.hasAdminAccess = hasAdminAccess;
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
                ", isAdmin: "+ isHasAdminAccess() +
                "}";
    }

}
