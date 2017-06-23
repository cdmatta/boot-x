package com.charandeepmatta.sam.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.charandeepmatta.sam.config.MonitoringConfiguration;
import com.charandeepmatta.sam.monitoring.HttpService;

@RestController
public class StatusResource {

  private List<HttpService> webpages;

  @Autowired
  public StatusResource(MonitoringConfiguration monitoringConfiguration) {
    webpages = monitoringConfiguration.getWebpages();
  }

  @GetMapping(value = "/status", produces = APPLICATION_JSON_VALUE)
  public List<HttpService> getStatus() {
    return webpages;
  }
}
