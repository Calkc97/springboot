package com.softtek.msacademy.hospital.appointmentsapi.service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.softtek.msacademy.hospital.appointmentsapi.model.entity.QxCatalogue;
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
public class QxCatalogueServiceImpl implements QxCatalogueService{
    private final RestTemplate restTemplate;

    public QxCatalogueServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    @HystrixCommand(
            fallbackMethod = "getQxCatalogueFallback",
            threadPoolKey = "getThreadPool")
    public QxCatalogue getQxCatalogue(int idQxCatalogue) {
        String url = "https://qx-catalogue.herokuapp.com/surgeries/" + idQxCatalogue;
        return this.restTemplate.getForObject(url, QxCatalogue.class);
    }

    public QxCatalogue getQxCatalogueFallback(int idQxCatalogue) {
        QxCatalogue qxCatalogue = new QxCatalogue();
        qxCatalogue.setIdSurgery(0);
        qxCatalogue.setSurgicalName("An error has occured while getting the qx catalogue.");
        return qxCatalogue;
    }
}
