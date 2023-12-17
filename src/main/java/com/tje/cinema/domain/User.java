package com.tje.cinema.domain;

import com.tje.cinema.interfaces.UserInterface;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "userType", discriminatorType = DiscriminatorType.STRING)
public class User implements UserInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,  insertable = false, updatable = false)
    private UserType userType;
    @Column(name = "date_of_registration", nullable = false)
    private LocalDate dateOfRegistration;

    public User() {
        this.dateOfRegistration = LocalDate.now();
        this.userType = UserType.REGISTERED;
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
                ", userType: "+ getUserType() +
                ", DOR: "+ getDateOfRegistration() +
                "}";
    }

    public enum UserType {
        REGISTERED, ADMIN, User
    }


}
