package com.softtek.msacademy.hospital.appointmentsapi.controller;

import com.softtek.msacademy.hospital.appointmentsapi.model.entity.*;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.Error;
import com.softtek.msacademy.hospital.appointmentsapi.service.PatientsServiceImpl;
import com.softtek.msacademy.hospital.appointmentsapi.service.QxAppointmentServiceImpl;
import com.softtek.msacademy.hospital.appointmentsapi.service.QxCatalogueServiceImpl;
import com.softtek.msacademy.hospital.appointmentsapi.service.WorkAreasServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class QxAppointmentController {
    @Autowired
    private QxAppointmentServiceImpl qxAppointmentService;

    @Autowired
    private WorkAreasServiceImpl workAreasService;

    @Autowired
    private QxCatalogueServiceImpl qxCatalogueService;

    @Autowired
    private PatientsServiceImpl getPatientService;

    @GetMapping(
            value = "/qx/{qxId}/appointments",
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity searchQxAppointmentsByIdAndDate(@PathVariable("qxId") int qxId, @RequestParam(required = false) String date) {
        List<QxAppointment> qxAppointments = null;
        try {
            if(date != null){
                qxAppointments = qxAppointmentService.searchQxAppointmentsByIdAndDate(qxId, date);
            }
            else {
                qxAppointments = qxAppointmentService.searchQxAppointmentsById(qxId);
            }
            return new ResponseEntity<>(qxAppointments.toArray(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            400,
                            "Bad Request",
                            "The request could not be understood by the server due to malformed syntax.",
                            "/qx/" + qxId + "/appointments"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(
            value = "/qx/appointments",
            consumes = {APPLICATION_JSON_VALUE},
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity addQxAppointment(@Valid @RequestBody QxAppointment qxAppointment) {
        boolean isBeforeDate = qxAppointment.getStartDate().isBefore(qxAppointment.getEndDate());
        boolean isEqualDate = qxAppointment.getStartDate().isEqual(qxAppointment.getEndDate());
        boolean isBeforeHour = qxAppointment.getStartHour().isBefore(qxAppointment.getEndHour());

        Qx qx = workAreasService.getQx(qxAppointment.getIdQx());
        QxCatalogue qxCatalogue = qxCatalogueService.getQxCatalogue(qxAppointment.getIdSurgicalInterventionCat());
        Patient patient = getPatientService.getPatient(qxAppointment.getNss());

        if (qx.getId() != 0 && qxCatalogue.getIdSurgery() != 0 && patient.getNss() != "0") {

            if (isBeforeDate || (isEqualDate && isBeforeHour)) {
                QxAppointment existentQxAppointment = qxAppointmentService.verifyQxAvailability(
                        qxAppointment.getStartDate().toString(),
                        qxAppointment.getStartHour().toString(),
                        qxAppointment.getEndDate().toString(),
                        qxAppointment.getEndHour().toString(),
                        qxAppointment.getIdQx());
                if (existentQxAppointment == null) {
                    qxAppointment.setAssistance(false);
                    qxAppointment.setCancelled(false);
                    qxAppointmentService.save(qxAppointment);
                    return new ResponseEntity<>(qxAppointment, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(
                            new Error(
                                    LocalDateTime.now(),
                                    409,
                                    "Conflict",
                                    "The appointment is overlapped",
                                    "/qx/appointments"),
                            HttpStatus.CONFLICT);
                }
            } else {
                return new ResponseEntity<>(
                        new Error(
                                LocalDateTime.now(),
                                400,
                                "Bad Request",
                                "The end date is before of the start date.",
                                "/qx/appointments"),
                        HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            400,
                            "Bad Request",
                            "The qx id does not exist.",
                            "/qx/appointments"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(
            value = "/qx/appointments/{appointmentId}",
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity searchQxAppointment(@PathVariable("appointmentId") int appointmentId) {
        if(qxAppointmentService.existsById(appointmentId)){
            return new ResponseEntity<>(qxAppointmentService.findById(appointmentId), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            404,
                            "Not Found",
                            "This qx appointment id does not exist.",
                            "/qx/appointments/" + appointmentId),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(
            value = "/qx/appointments/{appointmentId}",
            consumes = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity updateQxAppointment(@PathVariable("appointmentId") int appointmentId, @Valid @RequestBody QxAppointment qxAppointmentUpdated) {
        if (qxAppointmentService.existsById(appointmentId)) {
            boolean isBeforeDate = qxAppointmentUpdated.getStartDate().isBefore(qxAppointmentUpdated.getEndDate());
            boolean isEqualDate = qxAppointmentUpdated.getStartDate().isEqual(qxAppointmentUpdated.getEndDate());
            boolean isBeforeHour = qxAppointmentUpdated.getStartHour().isBefore(qxAppointmentUpdated.getEndHour());
            Qx qx = workAreasService.getQx(qxAppointmentUpdated.getIdQx());
            QxCatalogue qxCatalogue = qxCatalogueService.getQxCatalogue(qxAppointmentUpdated.getIdSurgicalInterventionCat());
            Patient patient = getPatientService.getPatient(qxAppointmentUpdated.getNss());

            if(qx.getId() != 0 && qxCatalogue.getIdSurgery() != 0 && patient.getNss() != "0") {
                if (isBeforeDate || (isEqualDate && isBeforeHour)) {
                    QxAppointment existentQxAppointment = qxAppointmentService.verifyQxAvailabilityExcludingCurrentAppointment(
                            qxAppointmentUpdated.getStartDate().toString(),
                            qxAppointmentUpdated.getStartHour().toString(),
                            qxAppointmentUpdated.getEndDate().toString(),
                            qxAppointmentUpdated.getEndHour().toString(),
                            qxAppointmentUpdated.getIdQx(),
                            appointmentId);
                    if (existentQxAppointment == null) {
                        QxAppointment qxAppointment = qxAppointmentService.findById(appointmentId);
                        qxAppointment.setStartDate(qxAppointmentUpdated.getStartDate());
                        qxAppointment.setEndDate(qxAppointmentUpdated.getEndDate());
                        qxAppointment.setStartHour(qxAppointmentUpdated.getStartHour());
                        qxAppointment.setEndHour(qxAppointmentUpdated.getEndHour());
                        qxAppointment.setNss(qxAppointmentUpdated.getNss());
                        qxAppointment.setIdSurgicalInterventionCat(qxAppointmentUpdated.getIdSurgicalInterventionCat());
                        qxAppointment.setIdQx(qxAppointmentUpdated.getIdQx());
                        qxAppointmentService.save(qxAppointment);
                        return new ResponseEntity(HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(
                                new Error(
                                        LocalDateTime.now(),
                                        409,
                                        "Conflict",
                                        "The appointment is overlapped.",
                                        "/qx/appointments/" + appointmentId),
                                HttpStatus.CONFLICT);
                    }
                } else {
                    return new ResponseEntity<>(
                            new Error(
                                    LocalDateTime.now(),
                                    400,
                                    "Bad Request",
                                    "The end date is before of the start date.",
                                    "/qx/appointments/" + appointmentId),
                            HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>(
                        new Error(
                                LocalDateTime.now(),
                                400,
                                "Bad Request",
                                "The qx id does not exist.",
                                "/qx/appointments"),
                        HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            404,
                            "Not Found",
                            "This qx appointment id does not exist.",
                            "/qx/appointments/" + appointmentId),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(
            value = "/qx/appointments/{appointmentId}",
            consumes = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity cancelOrAssistanceOfQxAppointment(@PathVariable("appointmentId") int appointmentId, @Valid @RequestBody AppointmentStatus appointmentStatus) {
        if((appointmentStatus.getAssistance() && appointmentStatus.getCancelled()) || (!appointmentStatus.getAssistance() && !appointmentStatus.getCancelled())){
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            400,
                            "Bad Request",
                            "One and only one value of the cancelled and assistance attributes must be true.",
                            "/qx/appointments/" + appointmentId),
                    HttpStatus.BAD_REQUEST);
        }else{
            if (qxAppointmentService.existsById(appointmentId)){
                QxAppointment qxAppointment = qxAppointmentService.findById(appointmentId);
                if(appointmentStatus.getAssistance()){
                    qxAppointment.setAssistance(true);
                }else{
                    qxAppointment.setCancelled(true);
                }
                qxAppointmentService.save(qxAppointment);
                return new ResponseEntity(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(
                        new Error(
                                LocalDateTime.now(),
                                404,
                                "Not Found",
                                "This qx appointment id does not exist.",
                                "/qx/appointments/" + appointmentId),
                        HttpStatus.NOT_FOUND);
            }
        }
    }


    @GetMapping(
            value = "/paciente/{patientID}",
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity checkPatient(@PathVariable("patientID") String patientID) {
        Patient patient = getPatientService.getPatient(patientID);
        System.out.println("Entro paciente " + patientID);
        return new ResponseEntity(patient, HttpStatus.OK);
    }
}