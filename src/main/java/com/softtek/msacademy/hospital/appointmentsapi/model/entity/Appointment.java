package com.softtek.msacademy.hospital.appointmentsapi.model.entity;

import javax.persistence.*;

@MappedSuperclass
public class Appointment extends Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_appointments")
    private int idAppointments;

    @Column(name = "assistance")
    private Boolean assistance;

    @Column(name = "nss")
    private String nss;

    public Appointment() {
    }

    public int getIdAppointments() {
        return idAppointments;
    }

    public void setIdAppointments(int idAppointments) {
        this.idAppointments = idAppointments;
    }

    public Boolean getAssistance() {
        return assistance;
    }

    public void setAssistance(Boolean assistance) {
        this.assistance = assistance;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }
}
