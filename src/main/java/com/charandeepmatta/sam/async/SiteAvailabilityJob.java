package com.charandeepmatta.sam.async;

import static java.lang.Thread.sleep;
import static java.time.LocalDateTime.now;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.charandeepmatta.sam.config.MonitoringConfiguration;
import com.charandeepmatta.sam.config.MonitoringSettings;
import com.charandeepmatta.sam.monitoring.HttpService;
import com.charandeepmatta.sam.monitoring.VerificationService;

import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@Slf4j
public class SiteAvailabilityJob {

  private MonitoringSettings monitoringSettings;

  private List<HttpService> webpages;

  private VerificationService verificationService;

  @Autowired
  public SiteAvailabilityJob(MonitoringConfiguration monitoringConfiguration, VerificationService verificationService) {
    this.verificationService = verificationService;
    monitoringSettings = monitoringConfiguration.getMonitoringSettings();
    webpages = monitoringConfiguration.getWebpages();
  }

  @Scheduled(cron = "${monitoringSettings.pollingFrequencyCronExpression}")
  public void pollServices() {
    try {
      LocalDateTime monitoringStartTime = now();
      Map<HttpService, Future<Boolean>> serviceStatusMap = new HashMap<>();
      startParallelVerification(serviceStatusMap);
      sleepToAllowMaxResponseTimeOut();
      boolean allServicesAreUp = aggregateResultsAndReturnOverallStatus(serviceStatusMap, monitoringStartTime);
      if (allServicesAreUp) {
        log.info("---- Overall Result . Services are UP ----");
      } else {
        log.error("---- Overall Result . Some services are DOWN ----");
      }
    } catch (Exception e) {
      log.error("Failure in polling services", e);
    }
  }

  private void startParallelVerification(Map<HttpService, Future<Boolean>> serviceStatusMap) {
    int timeout = monitoringSettings.getTimeOutForPollRequestSeconds();
    for (HttpService service : webpages) {
      serviceStatusMap.put(service, verificationService.isServiceRunning(service, timeout));
    }
  }

  private void sleepToAllowMaxResponseTimeOut() {
    try {
      int secondsToSleep = monitoringSettings.getTimeOutForPollRequestSeconds();
      log.debug("Sleeping {} seconds to allow verifications to finish", secondsToSleep);
      sleep(secondsToSleep * 1000);
    } catch (InterruptedException ignored) {
    }
  }

  private boolean aggregateResultsAndReturnOverallStatus(Map<HttpService, Future<Boolean>> serviceStatusMap, LocalDateTime monitoringStartTime) {
    serviceStatusMap.forEach((service, futureResult) -> {
      try {
        boolean serviceIsRunning = futureResult.get(0, SECONDS);
        if (serviceIsRunning) {
          log.info("UP   " + service);
          service.resetFailureCountersOnSuccess();
          return;
        }
        log.error("DOWN " + service);
      } catch (Exception e) {
        log.error("DOWN (timeout exceeded) " + service);
        futureResult.cancel(true);
      }
      service.updateFailureCounters(monitoringStartTime);
    });
    return serviceStatusMap.keySet().parallelStream().map(HttpService::serviceIsUp).allMatch(e -> e);
  }
}
