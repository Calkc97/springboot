package com.softtek.msacademy.hospital.appointmentsapi.service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.Consultory;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.Laboratory;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.Qx;
import org.springframework.beans.factory.annotation.Autowired;
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
public class WorkAreasServiceImpl implements WorkAreasService{

    private final RestTemplate restTemplate;

    public WorkAreasServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "getLaboratoryFallback",
            threadPoolKey = "getThreadPool")
    public Laboratory getLaboratory(int laboratoryId) {
        String url = "http://work-areas.herokuapp.com/laboratories/" + laboratoryId;
        return this.restTemplate.getForObject(url, Laboratory.class);
    }

    public Laboratory getLaboratoryFallback(int laboratoryId) {
        Laboratory laboratory = new Laboratory();
        laboratory.setId(0);
        laboratory.setName("An error has occured while getting the laboratory.");
        return laboratory;
    }

    @HystrixCommand(
            fallbackMethod = "getConsultoryFallback",
            threadPoolKey = "getThreadPool")
    public Consultory getConsultory(int consultoryID) {
        String url = "https://work-areas.herokuapp.com/consultories/" + consultoryID;
        return this.restTemplate.getForObject(url, Consultory.class);
    }

    public Consultory getConsultoryFallback(int consultoryID) {
        Consultory consultory = new Consultory();
        consultory.setId(0);
        consultory.setName("An error has occured while getting the consultory.");
        return consultory;
    }

    @HystrixCommand(
            fallbackMethod = "getQxFallback",
            threadPoolKey = "getThreadPool")
    public Qx getQx(int qxID) {
        String url = "https://work-areas.herokuapp.com/operating-rooms/" + qxID;
        return this.restTemplate.getForObject(url, Qx.class);
    }

    public Qx getQxFallback(int qxID) {
        Qx qx = new Qx();
        qx.setId(0);
        qx.setName("An error has occured while getting the qx.");
        return qx;
    }
}
