package com.softtek.msacademy.hospital.appointmentsapi.service;

import com.softtek.msacademy.hospital.appointmentsapi.model.entity.LaboratoryAppointment;

import java.util.List;

public interface LaboratoryAppointmentService {
    List<LaboratoryAppointment> searchLaboratoryAppointmentsByIdAndDate(int idLaboratory, String date);

    List<LaboratoryAppointment> searchLaboratoryAppointmentsById(int idLaboratory);

    LaboratoryAppointment verifyLaboratoryAvailability(String startDateNewAppointment,
                                                       String startHourNewAppointment,
                                                       String endDateNewAppointment,
                                                       String endHourNewAppointment,
                                                       int idLaboratories);

    LaboratoryAppointment verifyLaboratoryAvailabilityExcludingCurrentAppointment(String startDateNewAppointment,
                                                                                  String startHourNewAppointment,
                                                                                  String endDateNewAppointment,
                                                                                  String endHourNewAppointment,
                                                                                  int idLaboratories,
                                                                                  int idAppointments);

    LaboratoryAppointment save(LaboratoryAppointment laboratoryAppointment);

    boolean existsById(int idLaboratory);

    LaboratoryAppointment findById(int idLaboratory);

    List<LaboratoryAppointment> searchPatientAppointmentsByNss(String patientNss);
}
