package com.softtek.msacademy.hospital.appointmentsapi.service;

import com.softtek.msacademy.hospital.appointmentsapi.model.entity.QxAppointment;

import java.util.List;

public interface QxAppointmentService {
    List<QxAppointment> searchQxAppointmentsByIdAndDate(int idQx, String date);

    List<QxAppointment> searchQxAppointmentsById(int idQx);

    QxAppointment verifyQxAvailability(String startDateNewAppointment,
                                                       String startHourNewAppointment,
                                                       String endDateNewAppointment,
                                                       String endHourNewAppointment,
                                                       int idQx);

    QxAppointment verifyQxAvailabilityExcludingCurrentAppointment(String startDateNewAppointment,
                                                                                  String startHourNewAppointment,
                                                                                  String endDateNewAppointment,
                                                                                  String endHourNewAppointment,
                                                                                  int idQx,
                                                                                  int idAppointments);

    QxAppointment save(QxAppointment qxAppointment);

    boolean existsById(int idQx);

    QxAppointment findById(int idQx);

    List<QxAppointment> searchPatientAppointmentsByNss(String patientNss);
}
