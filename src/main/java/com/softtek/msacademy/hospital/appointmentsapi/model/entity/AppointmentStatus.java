package com.softtek.msacademy.hospital.appointmentsapi.model.entity;

public class AppointmentStatus {
    private Boolean cancelled;
    private Boolean assistance;

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Boolean getAssistance() {
        return assistance;
    }

    public void setAssistance(Boolean assistance) {
        this.assistance = assistance;
    }
}
