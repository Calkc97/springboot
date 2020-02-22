package com.softtek.msacademy.hospital.appointmentsapi.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "QxAppointment")
@Table(name = "qx_appointments")
public class QxAppointment extends Appointment{

    @Column(name="id_surgical_intervention_cat")
    private int idSurgicalInterventionCat;

    @Column(name="id_qx")
    private int idQx;

    public QxAppointment() {
    }

    public int getIdSurgicalInterventionCat() {
        return idSurgicalInterventionCat;
    }

    public void setIdSurgicalInterventionCat(int idSurgicalInterventionCat) {
        this.idSurgicalInterventionCat = idSurgicalInterventionCat;
    }

    public int getIdQx() {
        return idQx;
    }

    public void setIdQx(int idQx) {
        this.idQx = idQx;
    }
}
