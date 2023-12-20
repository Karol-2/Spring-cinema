package com.tje.cinema.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@DiscriminatorValue("ADMIN")
public class AdminUser extends User implements com.tje.cinema.interfaces.AdminUser {
    private LocalDateTime dateAdminGranted;

    public AdminUser() {}

    public AdminUser(String email, String name, String password) {
        super(email, name, password);
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
    public String toString() {
        return "User:{" +
                " id: " + getId() +
                ", email: " + getEmail() +
                ", name: " + getName() +
                ", password: " + getPassword() +
                ", userType: " + getUserType() +
                ", DOR: " + getDateOfRegistration() +
                "}";
    }

}
