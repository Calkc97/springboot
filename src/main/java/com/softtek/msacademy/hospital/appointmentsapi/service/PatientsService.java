package com.softtek.msacademy.hospital.appointmentsapi.service;

import com.softtek.msacademy.hospital.appointmentsapi.model.entity.Patient;

public interface PatientsService {
    Patient getPatient(String idPatient);
}
