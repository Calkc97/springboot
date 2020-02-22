package com.softtek.msacademy.hospital.appointmentsapi.service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.LaboratoryAppointment;
import com.softtek.msacademy.hospital.appointmentsapi.model.repository.LaboratoryAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@DefaultProperties(
        threadPoolProperties = {
                @HystrixProperty( name = "coreSize", value = "20" ),
                @HystrixProperty( name = "maxQueueSize", value = "10") },
        commandProperties = {
                @HystrixProperty( name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
                @HystrixProperty( name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                @HystrixProperty( name = "circuitBreaker.errorThresholdPercentage", value = "30"),
                @HystrixProperty( name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
                @HystrixProperty( name = "metrics.rollingStats.timeInMilliseconds", value = "5000"),
                @HystrixProperty( name = "metrics.rollingStats.numBuckets", value = "10") } )
public class LaboratoryAppointmentServiceImpl implements LaboratoryAppointmentService {

    @Autowired
    private LaboratoryAppointmentRepository laboratoryAppointmentRepository;

    @Override
    @HystrixCommand(
            fallbackMethod = "searchLaboratoryAppointmentsByIdAndDateFallback",
            threadPoolKey = "getThreadPool")
    public List<LaboratoryAppointment> searchLaboratoryAppointmentsByIdAndDate(int idLaboratory, String date){
        return laboratoryAppointmentRepository.searchLaboratoryAppointmentsByIdAndDate(idLaboratory, date);
    }

    public List<LaboratoryAppointment> searchLaboratoryAppointmentsByIdAndDateFallback(int idLaboratory, String date){
        return new ArrayList<>();
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "searchLaboratoryAppointmentsByIdFallback",
            threadPoolKey = "getThreadPool")
    public List<LaboratoryAppointment> searchLaboratoryAppointmentsById(int idLaboratory){
        return laboratoryAppointmentRepository.searchLaboratoryAppointmentsById(idLaboratory);
    }

    public List<LaboratoryAppointment> searchLaboratoryAppointmentsByIdFallback(int idLaboratory){
        return new ArrayList<>();
    }

    @Override
    @HystrixCommand(threadPoolKey = "getThreadPool")
    public LaboratoryAppointment verifyLaboratoryAvailability(String startDateNewAppointment,
                                                              String startHourNewAppointment,
                                                              String endDateNewAppointment,
                                                              String endHourNewAppointment,
                                                              int idLaboratories){
        return laboratoryAppointmentRepository.verifyLaboratoryAvailability(startDateNewAppointment,
                startHourNewAppointment,
                endDateNewAppointment,
                endHourNewAppointment,
                idLaboratories);
    }

    @Override
    @HystrixCommand(threadPoolKey = "getThreadPool")
    public LaboratoryAppointment verifyLaboratoryAvailabilityExcludingCurrentAppointment(String startDateNewAppointment,
                                                                                         String startHourNewAppointment,
                                                                                         String endDateNewAppointment,
                                                                                         String endHourNewAppointment,
                                                                                         int idLaboratories,
                                                                                         int idAppointments){
        return laboratoryAppointmentRepository.verifyLaboratoryAvailabilityExcludingCurrentAppointment(startDateNewAppointment,
                startHourNewAppointment,
                endDateNewAppointment,
                endHourNewAppointment,
                idLaboratories,
                idAppointments);
    }

    @Override
    @HystrixCommand(threadPoolKey = "writeThreadPool")
    public LaboratoryAppointment save(LaboratoryAppointment laboratoryAppointment){
        return laboratoryAppointmentRepository.save(laboratoryAppointment);
    }

    @Override
    public boolean existsById(int idLaboratory){
        return laboratoryAppointmentRepository.existsById(idLaboratory);
    }

    @Override
    @HystrixCommand(threadPoolKey = "getThreadPool")
    public LaboratoryAppointment findById(int idLaboratory){
        return laboratoryAppointmentRepository.findById(idLaboratory).orElse(null);
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "searchPatientAppointmentsByNssFallback",
            threadPoolKey = "getThreadPool")
    public List<LaboratoryAppointment> searchPatientAppointmentsByNss(String patientNss){
        return laboratoryAppointmentRepository.searchPatientAppointmentsByNss(patientNss);
    }

    public List<LaboratoryAppointment> searchPatientAppointmentsByNssFallback(String patientNss){
        return new ArrayList<>();
    }
}
