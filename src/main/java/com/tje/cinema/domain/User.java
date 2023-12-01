package com.tje.cinema.domain;

import com.tje.cinema.interfaces.UserInterface;

public class User implements UserInterface {

    private Long id;
    private String email;
    private String name;

    private String password;

    public User() {}

    public User(String email, String name,String password) {
        this.email = email;
        this.name = name;
        this.password = password;
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

    @Override
    public String toString(){
        return "User:{" +
                " id: " + getId()+
                ", email: "+ getEmail() +
                ", name: "+ getName() +
                ", password: "+ getPassword() +
                "}";
    }

}
