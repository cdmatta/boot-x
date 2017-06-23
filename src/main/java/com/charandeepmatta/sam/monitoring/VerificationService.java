package com.charandeepmatta.sam.monitoring;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VerificationService {

  @Async
  public Future<Boolean> isServiceRunning(final HttpService service, final int timeout) {
    try {
      log.info("Check " + service);
      return new AsyncResult<>(service.checkStatus(timeout));
    } catch (Exception e) {
      log.error(service + " check failed.", e);
      return new AsyncResult<>(false);
    }
  }
}
