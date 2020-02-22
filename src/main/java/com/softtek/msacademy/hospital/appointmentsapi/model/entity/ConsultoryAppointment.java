package com.softtek.msacademy.hospital.appointmentsapi.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "ConsultoryAppointment")
@Table(name = "consultory_appointments")
public class ConsultoryAppointment extends Appointment {

    @Column(name="id_consultories")
    private int idConsultories;

    public ConsultoryAppointment() {
    }

    public int getIdConsultories() {
        return idConsultories;
    }

    public void setIdConsultories(int idConsultories) {
        this.idConsultories = idConsultories;
    }
}
