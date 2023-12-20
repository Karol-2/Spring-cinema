package com.tje.cinema.interfaces;

import java.time.LocalDateTime;

public interface AdminUser extends UserInterface {
    LocalDateTime getDateAdminGranted();

    void setDateAdminGranted(LocalDateTime dateAdminGranted);

    @Override
    String toString();
}
