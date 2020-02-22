package com.softtek.msacademy.hospital.appointmentsapi.model.entity;

public class Consultory {
    private int id;
    private String name;
    private String estimated_duration;

    public int getId() {
        return id;
    }

    public String getEstimated_duration() {
        return estimated_duration;
    }

    public void setEstimated_duration(String estimated_duration) {
        this.estimated_duration = estimated_duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
