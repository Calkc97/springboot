package com.softtek.msacademy.hospital.appointmentsapi.controller;

import com.softtek.msacademy.hospital.appointmentsapi.model.entity.*;

import com.softtek.msacademy.hospital.appointmentsapi.model.entity.Error;
import com.softtek.msacademy.hospital.appointmentsapi.service.LaboratoryAppointmentServiceImpl;
import com.softtek.msacademy.hospital.appointmentsapi.service.PatientsServiceImpl;
import com.softtek.msacademy.hospital.appointmentsapi.service.WorkAreasService;
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
public class LaboratoryAppointmentController {

    @Autowired
    private LaboratoryAppointmentServiceImpl laboratoryAppointmentService;

    @Autowired
    private WorkAreasServiceImpl workAreasService;

    @Autowired
    private PatientsServiceImpl getPatientService;

    @GetMapping(
            value = "/laboratories/{laboratoryId}/appointments",
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity searchLaboratoryAppointmentsByIdAndDate(@PathVariable("laboratoryId") int laboratoryId, @RequestParam(required = false) String date) {
        Laboratory laboratory = workAreasService.getLaboratory(laboratoryId);
        if (laboratory.getId() != 0) {
            List<LaboratoryAppointment> laboratoryAppointments = null;
            try {
                if(date != null){
                    laboratoryAppointments = laboratoryAppointmentService.searchLaboratoryAppointmentsByIdAndDate(laboratoryId, date);
                }
                else {
                    laboratoryAppointments = laboratoryAppointmentService.searchLaboratoryAppointmentsById(laboratoryId);
                }
                return new ResponseEntity<>(laboratoryAppointments.toArray(), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(
                        new Error(
                                LocalDateTime.now(),
                                400,
                                "Bad Request",
                                "The request could not be understood by the server due to malformed syntax.",
                                "/laboratories/" + laboratoryId + "/appointments"),
                        HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            404,
                            "Not Found",
                            "The laboratory id does not exist.",
                            "/laboratories/" + laboratoryId + "/appointments"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(
            value = "/laboratories/appointments",
            consumes = {APPLICATION_JSON_VALUE},
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity addLaboratoryAppointment(@Valid @RequestBody LaboratoryAppointment laboratoryAppointment) {
        Laboratory laboratory = workAreasService.getLaboratory(laboratoryAppointment.getIdLaboratories());
        Patient patient = getPatientService.getPatient(laboratoryAppointment.getNss());

        if (laboratory.getId() != 0 && patient.getNss() != "0") {
            boolean isBeforeDate = laboratoryAppointment.getStartDate().isBefore(laboratoryAppointment.getEndDate());
            boolean isEqualDate = laboratoryAppointment.getStartDate().isEqual(laboratoryAppointment.getEndDate());
            boolean isBeforeHour = laboratoryAppointment.getStartHour().isBefore(laboratoryAppointment.getEndHour());

            if(isBeforeDate || (isEqualDate && isBeforeHour)){
                LaboratoryAppointment existentQxAppointment = laboratoryAppointmentService.verifyLaboratoryAvailability(
                        laboratoryAppointment.getStartDate().toString(),
                        laboratoryAppointment.getStartHour().toString(),
                        laboratoryAppointment.getEndDate().toString(),
                        laboratoryAppointment.getEndHour().toString(),
                        laboratoryAppointment.getIdLaboratories());
                if(existentQxAppointment == null){
                    laboratoryAppointment.setAssistance(false);
                    laboratoryAppointment.setCancelled(false);
                    laboratoryAppointmentService.save(laboratoryAppointment);
                    return new ResponseEntity<>(laboratoryAppointment, HttpStatus.CREATED);
                }
                else {
                    return new ResponseEntity<>(
                            new Error(
                                    LocalDateTime.now(),
                                    409,
                                    "Conflict",
                                    "The appointment is overlapped",
                                    "/laboratories/appointments"),
                            HttpStatus.CONFLICT);
                }
            }
            else{
                return new ResponseEntity<>(
                        new Error(
                                LocalDateTime.now(),
                                400,
                                "Bad Request",
                                "The end date is before of the start date.",
                                "/laboratories/appointments"),
                        HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            404,
                            "Not Found",
                            "The laboratory id does not exist.",
                            "/laboratories/appointments"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(
            value = "/laboratories/appointments/{appointmentId}",
            produces = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity searchLaboratoryAppointment(@PathVariable("appointmentId") int appointmentId) {
        if(laboratoryAppointmentService.existsById(appointmentId)){
            return new ResponseEntity<>(laboratoryAppointmentService.findById(appointmentId), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            404,
                            "Not Found",
                            "This laboratory appointment id does not exist.",
                            "/laboratories/appointments/" + appointmentId),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(
            value = "/laboratories/appointments/{appointmentId}",
            consumes = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity updateLaboratoryAppointment(@PathVariable("appointmentId") int appointmentId, @Valid @RequestBody LaboratoryAppointment laboratoryAppointmentUpdated) {
        if(laboratoryAppointmentService.existsById(appointmentId)){
            Laboratory laboratory = workAreasService.getLaboratory(laboratoryAppointmentUpdated.getIdLaboratories());
            Patient patient = getPatientService.getPatient(laboratoryAppointmentUpdated.getNss());
            if(laboratory.getId() != 0 && patient.getNss() != "0"){
                boolean isBeforeDate = laboratoryAppointmentUpdated.getStartDate().isBefore(laboratoryAppointmentUpdated.getEndDate());
                boolean isEqualDate = laboratoryAppointmentUpdated.getStartDate().isEqual(laboratoryAppointmentUpdated.getEndDate());
                boolean isBeforeHour = laboratoryAppointmentUpdated.getStartHour().isBefore(laboratoryAppointmentUpdated.getEndHour());
                if(isBeforeDate || (isEqualDate && isBeforeHour)){
                    LaboratoryAppointment existentLaboratoryAppointment = laboratoryAppointmentService.verifyLaboratoryAvailabilityExcludingCurrentAppointment(
                            laboratoryAppointmentUpdated.getStartDate().toString(),
                            laboratoryAppointmentUpdated.getStartHour().toString(),
                            laboratoryAppointmentUpdated.getEndDate().toString(),
                            laboratoryAppointmentUpdated.getEndHour().toString(),
                            laboratoryAppointmentUpdated.getIdLaboratories(),
                            appointmentId);
                    if(existentLaboratoryAppointment == null){
                        LaboratoryAppointment laboratoryAppointment = laboratoryAppointmentService.findById(appointmentId);
                        laboratoryAppointment.setStartDate(laboratoryAppointmentUpdated.getStartDate());
                        laboratoryAppointment.setEndDate(laboratoryAppointmentUpdated.getEndDate());
                        laboratoryAppointment.setStartHour(laboratoryAppointmentUpdated.getStartHour());
                        laboratoryAppointment.setEndHour(laboratoryAppointmentUpdated.getEndHour());
                        laboratoryAppointment.setNss(laboratoryAppointmentUpdated.getNss());
                        laboratoryAppointment.setIdMedicalStudies(laboratoryAppointmentUpdated.getIdMedicalStudies());
                        laboratoryAppointment.setIdLaboratories(laboratoryAppointmentUpdated.getIdLaboratories());
                        laboratoryAppointmentService.save(laboratoryAppointment);
                        return new ResponseEntity(HttpStatus.OK);
                    }
                    else {
                        return new ResponseEntity<>(
                                new Error(
                                        LocalDateTime.now(),
                                        409,
                                        "Conflict",
                                        "The appointment is overlapped.",
                                        "/laboratories/appointments/" + appointmentId),
                                HttpStatus.CONFLICT);
                    }
                }
                else {
                    return new ResponseEntity<>(
                            new Error(
                                    LocalDateTime.now(),
                                    400,
                                    "Bad Request",
                                    "The appointment is overlapped",
                                    "/laboratories/appointments/" + appointmentId),
                            HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return new ResponseEntity<>(
                        new Error(
                                LocalDateTime.now(),
                                404,
                                "Not Found",
                                "The laboratory id does not exist.",
                                "/laboratories/appointments/" + appointmentId),
                        HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            404,
                            "Not Found",
                            "This laboratory appointment id does not exist.",
                            "/laboratories/appointments/" + appointmentId),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(
            value = "/laboratories/appointments/{appointmentId}",
            consumes = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity cancelOrAssistanceOfLaboratoryAppointment(@PathVariable("appointmentId") int appointmentId, @Valid @RequestBody AppointmentStatus appointmentStatus) {
        if((appointmentStatus.getAssistance() && appointmentStatus.getCancelled()) || (!appointmentStatus.getAssistance() && !appointmentStatus.getCancelled())){
            return new ResponseEntity<>(
                    new Error(
                            LocalDateTime.now(),
                            400,
                            "Bad Request",
                            "One and only one value of the cancelled and assistance attributes must be true.",
                            "/laboratories/appointments/" + appointmentId),
                    HttpStatus.BAD_REQUEST);
        }else{
            if (laboratoryAppointmentService.existsById(appointmentId)){
                LaboratoryAppointment laboratoryAppointment = laboratoryAppointmentService.findById(appointmentId);
                if(appointmentStatus.getAssistance()){
                    laboratoryAppointment.setAssistance(true);
                }else{
                    laboratoryAppointment.setCancelled(true);
                }
                laboratoryAppointmentService.save(laboratoryAppointment);
                return new ResponseEntity(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(
                        new Error(
                                LocalDateTime.now(),
                                404,
                                "Not Found",
                                "This laboratory appointment id does not exist.",
                                "/laboratories/appointments/" + appointmentId),
                        HttpStatus.NOT_FOUND);
            }
        }
    }
}
