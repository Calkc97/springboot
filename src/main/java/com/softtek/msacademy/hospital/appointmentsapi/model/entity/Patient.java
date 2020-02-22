package com.softtek.msacademy.hospital.appointmentsapi.model.entity;

public class Patient {
    private String nss;

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    private String first_name;
}
