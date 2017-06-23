package com.charandeepmatta.sam.async;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.charandeepmatta.sam.config.MonitoringConfiguration;
import com.charandeepmatta.sam.config.MonitoringSettings;
import com.charandeepmatta.sam.monitoring.HttpService;

@Component
@EnableScheduling
public class SiteAvailabilityJob {

  private MonitoringSettings monitoringSettings;

  private List<HttpService> webpages;

  @Autowired
  public SiteAvailabilityJob(MonitoringConfiguration monitoringConfiguration) {
    monitoringSettings = monitoringConfiguration.getMonitoringSettings();
    webpages = monitoringConfiguration.getWebpages();
  }

  @Scheduled(cron = "${monitoringSettings.pollingFrequencyCronExpression}")
  public void pollServices() {
    webpages.stream().forEach(System.out::println);
    System.out.println(monitoringSettings);
  }
}
