package com.softtek.msacademy.hospital.appointmentsapi.service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.Patient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
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
public class PatientsServiceImpl implements PatientsService {
    private final RestTemplate restTemplate;

    public PatientsServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "getPatientFallback",
            threadPoolKey = "getThreadPool")
    public Patient getPatient(String idPatient) {
        System.out.println("bien");
        String url = "http://softtekmsacademypatients.herokuapp.com/administration/patient/" + idPatient;
        return this.restTemplate.getForObject(url, Patient.class);
    }

    public Patient getPatientFallback(String idPatient) {
        System.out.println("mal");
        Patient patient = new Patient();
        patient.setNss("0");
        patient.setFirst_name("An error has occured while getting the patient.");
        return patient;
    }
}
