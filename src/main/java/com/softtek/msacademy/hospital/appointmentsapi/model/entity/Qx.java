package com.softtek.msacademy.hospital.appointmentsapi.model.entity;

public class Qx {
    private int id;
    private String name;

    public int getId() {
        return id;
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

    public int getQx_number_room() {
        return qx_number_room;
    }

    public void setQx_number_room(int qx_number_room) {
        this.qx_number_room = qx_number_room;
    }

    public String getQx_clasification() {
        return qx_clasification;
    }

    public void setQx_clasification(String qx_clasification) {
        this.qx_clasification = qx_clasification;
    }

    private int qx_number_room;
    private  String qx_clasification;

}
