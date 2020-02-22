package com.softtek.msacademy.hospital.appointmentsapi.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "LaboratoryAppointment")
@Table(name = "laboratory_appointments")
public class LaboratoryAppointment extends Appointment{

    @Column(name="id_medical_studies")
    private int idMedicalStudies;

    @Column(name="id_laboratories")
    private int idLaboratories;

    public LaboratoryAppointment() {
    }

    public int getIdMedicalStudies() {
        return idMedicalStudies;
    }

    public void setIdMedicalStudies(int idMedicalStudies) {
        this.idMedicalStudies = idMedicalStudies;
    }

    public int getIdLaboratories() {
        return idLaboratories;
    }

    public void setIdLaboratories(int idLaboratories) {
        this.idLaboratories = idLaboratories;
    }
}
