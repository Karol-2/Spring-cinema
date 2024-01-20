package com.tje.cinema.domain;

import com.tje.cinema.interfaces.UserInterface;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "email is mandatory")
    @NotBlank(message = "email is mandatory")
    @Size(min = 5, max = 100, message = "email length should be between 5 and 100")
    @Email(message = "enter valid email address")
    private String email;
    @Column(nullable = false)
    @NotNull(message = "name is mandatory")
    @Size(min = 2, max = 50, message = "name length should be between 2 and 50")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "password is mandatory")
    @Size(min = 6, max = 50, message = "password length should be between 6 and 50")
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
