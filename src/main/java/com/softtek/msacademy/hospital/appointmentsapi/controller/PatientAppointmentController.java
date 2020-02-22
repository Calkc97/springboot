package com.softtek.msacademy.hospital.appointmentsapi.controller;

import com.softtek.msacademy.hospital.appointmentsapi.model.entity.Appointment;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.ConsultoryAppointment;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.Error;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.LaboratoryAppointment;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.QxAppointment;
import com.softtek.msacademy.hospital.appointmentsapi.service.ConsultoryAppointmentServiceImpl;
import com.softtek.msacademy.hospital.appointmentsapi.service.LaboratoryAppointmentServiceImpl;
import com.softtek.msacademy.hospital.appointmentsapi.service.QxAppointmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class PatientAppointmentController {

    @Autowired
    private QxAppointmentServiceImpl qxAppointmentService;

    @Autowired
    private LaboratoryAppointmentServiceImpl laboratoryAppointmentService;

    @Autowired
    private ConsultoryAppointmentServiceImpl consultoryAppointmentService;

    @GetMapping(
            value = "/patients/{patientNss}/appointments",
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity searchPatientAppointments(@PathVariable("patientNss") String patientNss) {
        List<QxAppointment> qxAppointments = null;
        List<LaboratoryAppointment> laboratoryAppointments = null;
        List<ConsultoryAppointment> consultoryAppointments = null;
        List<Appointment> appointments = new ArrayList<Appointment>();
        try {
            qxAppointments = this.qxAppointmentService.searchPatientAppointmentsByNss(patientNss);
            laboratoryAppointments = this.laboratoryAppointmentService.searchPatientAppointmentsByNss(patientNss);
            consultoryAppointments = this.consultoryAppointmentService.searchPatientAppointmentsByNss(patientNss);
            appointments.addAll(qxAppointments);
            appointments.addAll(laboratoryAppointments);
            appointments.addAll(consultoryAppointments);
            return new ResponseEntity<>(appointments.toArray(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            400,
                            "Bad Request",
                            "The request could not be understood by the server due to malformed syntax.",
                            "/patients/" + patientNss + "/appointments"),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
