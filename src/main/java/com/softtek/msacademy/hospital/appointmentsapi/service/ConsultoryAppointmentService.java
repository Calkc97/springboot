package com.softtek.msacademy.hospital.appointmentsapi.service;

import com.softtek.msacademy.hospital.appointmentsapi.model.entity.ConsultoryAppointment;

import java.util.List;

public interface ConsultoryAppointmentService {
    List<ConsultoryAppointment> searchConsultoryAppointmentsByIdAndDate(int idConsultory, String date);

    List<ConsultoryAppointment> searchConsultoryAppointmentsById(int idConsultory);

    ConsultoryAppointment verifyConsultoryAvailability(String startDateNewAppointment,
                                                       String startHourNewAppointment,
                                                       String endDateNewAppointment,
                                                       String endHourNewAppointment,
                                                       int idConsultories);

    ConsultoryAppointment verifyConsultoryAvailabilityExcludingCurrentAppointment(String startDateNewAppointment,
                                                                                  String startHourNewAppointment,
                                                                                  String endDateNewAppointment,
                                                                                  String endHourNewAppointment,
                                                                                  int idConsultories,
                                                                                  int idAppointments);

    ConsultoryAppointment save(ConsultoryAppointment consultoryAppointment);

    boolean existsById(int idConsultory);

    ConsultoryAppointment findById(int idConsultory);

    List<ConsultoryAppointment> searchPatientAppointmentsByNss(String patientNss);
}
