package com.softtek.msacademy.hospital.appointmentsapi.service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.ConsultoryAppointment;
import com.softtek.msacademy.hospital.appointmentsapi.model.repository.ConsultoryAppointmentRepository;
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
public class ConsultoryAppointmentServiceImpl implements ConsultoryAppointmentService{

    @Autowired
    private ConsultoryAppointmentRepository consultoryAppointmentRepository;

    @Override
    @HystrixCommand(
            fallbackMethod = "searchConsultoryAppointmentsByIdAndDateFallback",
            threadPoolKey = "getThreadPool")
    public List<ConsultoryAppointment> searchConsultoryAppointmentsByIdAndDate(int idConsultory, String date){
        return consultoryAppointmentRepository.searchConsultoryAppointmentsByIdAndDate(idConsultory, date);
    }

    public List<ConsultoryAppointment> searchConsultoryAppointmentsByIdAndDateFallback(int idConsultory, String date){
        return new ArrayList<>();
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "searchConsultoryAppointmentsByIdFallback",
            threadPoolKey = "getThreadPool"
    )
    public List<ConsultoryAppointment> searchConsultoryAppointmentsById(int idConsultory){
        return consultoryAppointmentRepository.searchConsultoryAppointmentsById(idConsultory);
    }

    public List<ConsultoryAppointment> searchConsultoryAppointmentsByIdFallback(int idConsultory){
        return new ArrayList<>();
    }

    @Override
    @HystrixCommand(threadPoolKey = "getThreadPool")
    public ConsultoryAppointment verifyConsultoryAvailability(String startDateNewAppointment,
                                                              String startHourNewAppointment,
                                                              String endDateNewAppointment,
                                                              String endHourNewAppointment,
                                                              int idConsultories){
        return consultoryAppointmentRepository.verifyConsultoryAvailability(startDateNewAppointment,
                startHourNewAppointment,
                endDateNewAppointment,
                endHourNewAppointment,
                idConsultories);
    }

    @Override
    @HystrixCommand(threadPoolKey = "getThreadPool")
    public ConsultoryAppointment verifyConsultoryAvailabilityExcludingCurrentAppointment(String startDateNewAppointment,
                                                                                         String startHourNewAppointment,
                                                                                         String endDateNewAppointment,
                                                                                         String endHourNewAppointment,
                                                                                         int idConsultories,
                                                                                         int idAppointments){
        return consultoryAppointmentRepository.verifyConsultoryAvailabilityExcludingCurrentAppointment(startDateNewAppointment,
                startHourNewAppointment,
                endDateNewAppointment,
                endHourNewAppointment,
                idConsultories,
                idAppointments);
    }

    @Override
    @HystrixCommand(threadPoolKey = "writeThreadPool")
    public ConsultoryAppointment save(ConsultoryAppointment consultoryAppointment){
        return consultoryAppointmentRepository.save(consultoryAppointment);
    }

    @Override
    public boolean existsById(int idLaboratory){
        return consultoryAppointmentRepository.existsById(idLaboratory);
    }

    @Override
    @HystrixCommand(threadPoolKey = "getThreadPool")
    public ConsultoryAppointment findById(int idConsultory){
        return consultoryAppointmentRepository.findById(idConsultory).orElse(null);
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "searchPatientAppointmentsByNssFallback",
            threadPoolKey = "getThreadPool")
    public List<ConsultoryAppointment> searchPatientAppointmentsByNss(String patientNss){
        return consultoryAppointmentRepository.searchPatientAppointmentsByNss(patientNss);
    }

    public List<ConsultoryAppointment> searchPatientAppointmentsByNssFallback(String patientNss){
        return new ArrayList<>();
    }
}
