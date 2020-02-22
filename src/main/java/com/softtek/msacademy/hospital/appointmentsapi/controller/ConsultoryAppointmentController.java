package com.softtek.msacademy.hospital.appointmentsapi.controller;

import com.softtek.msacademy.hospital.appointmentsapi.model.entity.*;

import com.softtek.msacademy.hospital.appointmentsapi.model.entity.Error;
import com.softtek.msacademy.hospital.appointmentsapi.service.ConsultoryAppointmentServiceImpl;
import com.softtek.msacademy.hospital.appointmentsapi.service.PatientsServiceImpl;
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
public class ConsultoryAppointmentController {

    @Autowired
    private ConsultoryAppointmentServiceImpl consultoryAppointmentService;

    @Autowired
    private WorkAreasServiceImpl workAreasService;

    @Autowired
    private PatientsServiceImpl getPatientService;

    @GetMapping(
            value = "/consultories/{consultoryId}/appointments",
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity searchConsultoryAppointmentsByIdAndDate(@PathVariable("consultoryId") int consultoryId, @RequestParam(required = false) String date) {
        List<ConsultoryAppointment> consultoryAppointment = null;
        try {
            if(date != null){
                consultoryAppointment = consultoryAppointmentService.searchConsultoryAppointmentsByIdAndDate(consultoryId, date);
            }
            else {
                consultoryAppointment = consultoryAppointmentService.searchConsultoryAppointmentsById(consultoryId);
            }
            return new ResponseEntity<>(consultoryAppointment.toArray(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            400,
                            "Bad Request",
                            "The request could not be understood by the server due to malformed syntax.",
                            "/consultories/" + consultoryId + "/appointments"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(
            value = "/consultories/appointments",
            consumes = {APPLICATION_JSON_VALUE},
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity addConsultoryAppointment(@Valid @RequestBody ConsultoryAppointment consultoryAppointment) {
        boolean isBeforeDate = consultoryAppointment.getStartDate().isBefore(consultoryAppointment.getEndDate());
        boolean isEqualDate = consultoryAppointment.getStartDate().isEqual(consultoryAppointment.getEndDate());
        boolean isBeforeHour = consultoryAppointment.getStartHour().isBefore(consultoryAppointment.getEndHour());

        Consultory consultory = workAreasService.getConsultory(consultoryAppointment.getIdConsultories());
        Patient patient = getPatientService.getPatient(consultoryAppointment.getNss());

        if(consultory.getId() != 0 && patient.getNss() != "0") {
            if (isBeforeDate || (isEqualDate && isBeforeHour)) {
                ConsultoryAppointment existentQxAppointment = consultoryAppointmentService.verifyConsultoryAvailability(
                        consultoryAppointment.getStartDate().toString(),
                        consultoryAppointment.getStartHour().toString(),
                        consultoryAppointment.getEndDate().toString(),
                        consultoryAppointment.getEndHour().toString(),
                        consultoryAppointment.getIdConsultories());
                if (existentQxAppointment == null) {
                    consultoryAppointment.setAssistance(false);
                    consultoryAppointment.setCancelled(false);
                    consultoryAppointmentService.save(consultoryAppointment);
                    return new ResponseEntity<>(consultoryAppointment, HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>(
                            new Error(
                                    LocalDateTime.now(),
                                    409,
                                    "Conflict",
                                    "The appointment is overlapped",
                                    "/consultories/appointments"),
                            HttpStatus.CONFLICT);
                }
            } else {
                return new ResponseEntity<>(
                        new Error(
                                LocalDateTime.now(),
                                400,
                                "Bad Request",
                                "The end date is before of the start date.",
                                "/consultories/appointments"),
                        HttpStatus.BAD_REQUEST);
            }
        }else{
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            400,
                            "Bad Request",
                            "The consultory id does not exist.",
                            "/consultories/appointments"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(
            value = "/consultories/appointments/{appointmentId}",
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity searchConsultoryAppointment(@PathVariable("appointmentId") int appointmentId) {
        if(consultoryAppointmentService.existsById(appointmentId)){
            return new ResponseEntity<>(consultoryAppointmentService.findById(appointmentId), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            404,
                            "Not Found",
                            "This consultory appointment id does not exist.",
                            "/consultories/appointments/" + appointmentId),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(
            value = "/consultories/appointments/{appointmentId}",
            consumes = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity updateConsultoryAppointment(@PathVariable("appointmentId") int appointmentId, @Valid @RequestBody ConsultoryAppointment consultoryAppointmentUpdated) {
        if (consultoryAppointmentService.existsById(appointmentId)) {
            boolean isBeforeDate = consultoryAppointmentUpdated.getStartDate().isBefore(consultoryAppointmentUpdated.getEndDate());
            boolean isEqualDate = consultoryAppointmentUpdated.getStartDate().isEqual(consultoryAppointmentUpdated.getEndDate());
            boolean isBeforeHour = consultoryAppointmentUpdated.getStartHour().isBefore(consultoryAppointmentUpdated.getEndHour());
            Consultory consultory = workAreasService.getConsultory(consultoryAppointmentUpdated.getIdConsultories());
            Patient patient = getPatientService.getPatient(consultoryAppointmentUpdated.getNss());

            if(consultory.getId() != 0 && patient.getNss() != "0") {
                if (isBeforeDate || (isEqualDate && isBeforeHour)) {
                    ConsultoryAppointment existentConsultoryAppointment = consultoryAppointmentService.verifyConsultoryAvailabilityExcludingCurrentAppointment(
                            consultoryAppointmentUpdated.getStartDate().toString(),
                            consultoryAppointmentUpdated.getStartHour().toString(),
                            consultoryAppointmentUpdated.getEndDate().toString(),
                            consultoryAppointmentUpdated.getEndHour().toString(),
                            consultoryAppointmentUpdated.getIdConsultories(),
                            appointmentId);
                    if (existentConsultoryAppointment == null) {
                        ConsultoryAppointment consultoryAppointment = consultoryAppointmentService.findById(appointmentId);
                        consultoryAppointment.setStartDate(consultoryAppointmentUpdated.getStartDate());
                        consultoryAppointment.setEndDate(consultoryAppointmentUpdated.getEndDate());
                        consultoryAppointment.setStartHour(consultoryAppointmentUpdated.getStartHour());
                        consultoryAppointment.setEndHour(consultoryAppointmentUpdated.getEndHour());
                        consultoryAppointment.setNss(consultoryAppointmentUpdated.getNss());
                        consultoryAppointment.setIdConsultories(consultoryAppointmentUpdated.getIdConsultories());
                        consultoryAppointmentService.save(consultoryAppointment);
                        return new ResponseEntity(HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(
                                new Error(
                                        LocalDateTime.now(),
                                        409,
                                        "Conflict",
                                        "The appointment is overlapped.",
                                        "/consultories/appointments/" + appointmentId),
                                HttpStatus.CONFLICT);
                    }
                } else {
                    return new ResponseEntity<>(
                            new Error(LocalDateTime.now(),
                                    400,
                                    "Bad Request",
                                    "The end date is before of the start date.",
                                    "/consultories/appointments/" + appointmentId),
                            HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>(
                        new Error(
                                LocalDateTime.now(),
                                400,
                                "Bad Request",
                                "The consultory id does not exist.",
                                "/consultories/appointments"),
                        HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            404,
                            "Not Found",
                            "This consultory appointment id does not exist.",
                            "/consultories/appointments/" + appointmentId),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(
            value = "/consultories/appointments/{appointmentId}",
            consumes = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity cancelOrAssistanceOfConsultoryAppointment(@PathVariable("appointmentId") int appointmentId, @Valid @RequestBody AppointmentStatus appointmentStatus) {
        if((appointmentStatus.getAssistance() && appointmentStatus.getCancelled()) || (!appointmentStatus.getAssistance() && !appointmentStatus.getCancelled())){
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            400,
                            "Bad Request",
                            "One and only one value of the cancelled and assistance attributes must be true.",
                            "/consultories/appointments/" + appointmentId),
                    HttpStatus.BAD_REQUEST);
        }else{
            if (consultoryAppointmentService.existsById(appointmentId)){
                ConsultoryAppointment consultoryAppointment = consultoryAppointmentService.findById(appointmentId);
                if(appointmentStatus.getAssistance()){
                    consultoryAppointment.setAssistance(true);
                }else{
                    consultoryAppointment.setCancelled(true);
                }
                consultoryAppointmentService.save(consultoryAppointment);
                return new ResponseEntity(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(
                        new Error(
                                LocalDateTime.now(),
                                404,
                                "Not Found",
                                "This consultory appointment id does not exist.",
                                "/consultories/appointments/" + appointmentId),
                        HttpStatus.NOT_FOUND);
            }
        }
    }


    @GetMapping(
            value = "/consultories/{consultoryId}",
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity checkConsultories(@PathVariable("consultoryId") int consultoryId) {
        Consultory consultory = workAreasService.getConsultory(consultoryId);
        return new ResponseEntity(consultory, HttpStatus.OK);
    }

}
