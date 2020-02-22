package com.softtek.msacademy.hospital.appointmentsapi.service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.QxAppointment;
import com.softtek.msacademy.hospital.appointmentsapi.model.repository.QxAppointmentRepository;
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
public class QxAppointmentServiceImpl implements QxAppointmentService{


    @Autowired
    private QxAppointmentRepository qxAppointmentRepository;

    @Override
    @HystrixCommand(
            fallbackMethod = "searchQxAppointmentsByIdAndDateFallback",
            threadPoolKey = "getThreadPool")
    public List<QxAppointment> searchQxAppointmentsByIdAndDate(int idQx, String date){
        return qxAppointmentRepository.searchQxAppointmentsByIdAndDate(idQx, date);
    }

    public List<QxAppointment> searchQxAppointmentsByIdAndDateFallback(int idQx, String date){
        return new ArrayList<>();
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "searchQxAppointmentsByIdFallback",
            threadPoolKey = "getThreadPool")
    public List<QxAppointment> searchQxAppointmentsById(int idQx){
        return qxAppointmentRepository.searchQxAppointmentsById(idQx);
    }

    public List<QxAppointment> searchQxAppointmentsByIdFallback(int idQx){
        return new ArrayList<>();
    }

    @Override
    @HystrixCommand(threadPoolKey = "getThreadPool")
    public QxAppointment verifyQxAvailability(String startDateNewAppointment,
                                                              String startHourNewAppointment,
                                                              String endDateNewAppointment,
                                                              String endHourNewAppointment,
                                                              int idQx){
        return qxAppointmentRepository.verifyQxAvailability(startDateNewAppointment,
                startHourNewAppointment,
                endDateNewAppointment,
                endHourNewAppointment,
                idQx);
    }

    @Override
    @HystrixCommand(threadPoolKey = "getThreadPool")
    public QxAppointment verifyQxAvailabilityExcludingCurrentAppointment(String startDateNewAppointment,
                                                                                         String startHourNewAppointment,
                                                                                         String endDateNewAppointment,
                                                                                         String endHourNewAppointment,
                                                                                         int idQx,
                                                                                         int idAppointments){
        return qxAppointmentRepository.verifyQxAvailabilityExcludingCurrentAppointment(startDateNewAppointment,
                startHourNewAppointment,
                endDateNewAppointment,
                endHourNewAppointment,
                idQx,
                idAppointments);
    }

    @Override
    @HystrixCommand(threadPoolKey = "writeThreadPool")
    public QxAppointment save(QxAppointment qxAppointment){
        return qxAppointmentRepository.save(qxAppointment);
    }

    @Override
    public boolean existsById(int idQx){
        return qxAppointmentRepository.existsById(idQx);
    }

    @Override
    @HystrixCommand(threadPoolKey = "getThreadPool")
    public QxAppointment findById(int idQx){
        return qxAppointmentRepository.findById(idQx).orElse(null);
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "searchPatientAppointmentsByNssFallback",
            threadPoolKey = "getThreadPool")
    public List<QxAppointment> searchPatientAppointmentsByNss(String patientNss){
        return qxAppointmentRepository.searchPatientAppointmentsByNss(patientNss);
    }

    public List<QxAppointment> searchPatientAppointmentsByNssFallback(String patientNss){
        return new ArrayList<>();
    }
}
