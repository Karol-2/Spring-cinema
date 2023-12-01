package com.tje.cinema.interfaces;

public interface UserInterface {
    Long getId();

    void setId(Long id);

    String getEmail();

    void setEmail(String email);

    String getName();

    void setName(String name);

    String getPassword();

    void setPassword(String password);

    @Override
    String toString();
}
