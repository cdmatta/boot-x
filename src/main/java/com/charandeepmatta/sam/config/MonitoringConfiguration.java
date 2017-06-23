package com.charandeepmatta.sam.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.charandeepmatta.sam.monitoring.HttpService;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties
@Getter
@Setter
public class MonitoringConfiguration {

  private MonitoringSettings monitoringSettings;

  private List<HttpService> webpages;
}
