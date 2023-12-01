package com.tje.cinema.interfaces;

import java.time.LocalDateTime;

public interface AdminUserInterface {
    public void setHasAdminAccess(boolean hasAdminAccess);

    public LocalDateTime getDateAdminGranted();

    public void setDateAdminGranted(LocalDateTime dateAdminGranted);
}
