package com.softtek.msacademy.hospital.appointmentsapi.model.entity;

public class QxCatalogue {
    private int idSurgery;
    private String surgicalName;
    private  String surgicalCategory;
    private int estimatedDuration;

    public int getIdSurgery() {
        return idSurgery;
    }

    public void setIdSurgery(int idSurgery) {
        this.idSurgery = idSurgery;
    }

    public String getSurgicalName() {
        return surgicalName;
    }

    public void setSurgicalName(String surgicalName) {
        this.surgicalName = surgicalName;
    }

    public String getSurgicalCategory() {
        return surgicalCategory;
    }

    public void setSurgicalCategory(String surgicalCategory) {
        this.surgicalCategory = surgicalCategory;
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

}
