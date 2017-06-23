package com.charandeepmatta.sam.monitoring;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class MonitorableService {

  private int recentFailureCount = 0;

  private Optional<LocalDateTime> downtimeStart = empty();

  public void resetOnSuccess() {
    recentFailureCount = 0;
    downtimeStart = empty();
  }

  public void updateFailureCounters(final LocalDateTime monitoringStartTime) {
    ++recentFailureCount;
    if (downtimeStart.isPresent()) {
      return;
    }
    downtimeStart = of(monitoringStartTime);
  }

  /**
   * Check the service availability.
   * 
   * @param timeoutSeconds Max seconds the thread will wait before assuming service is down
   * @return True if service is up. False otherwise
   */
  public abstract boolean checkStatus(int timeoutSeconds);

  @Override
  public boolean equals(final Object obj) {
    return reflectionEquals(this, obj, "recentFailureCount", "downtimeStart");
  }

  @Override
  public int hashCode() {
    return reflectionHashCode(this, "recentFailureCount", "downtimeStart");
  }
}

