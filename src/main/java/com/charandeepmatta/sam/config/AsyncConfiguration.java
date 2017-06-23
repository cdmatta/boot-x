package com.charandeepmatta.sam.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.charandeepmatta.sam.async.ExceptionHandlingAsyncTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAsync
@EnableScheduling
@Slf4j
public class AsyncConfiguration implements AsyncConfigurer, EnvironmentAware {

  private RelaxedPropertyResolver propertyResolver;

  @Override
  public void setEnvironment(Environment environment) {
    this.propertyResolver = new RelaxedPropertyResolver(environment, "async.");
  }

  @Override
  @Bean
  public Executor getAsyncExecutor() {
    log.debug("Creating Async Task Executor");
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(getIntegerConfigProperty("corePoolSize", 200));
    executor.setMaxPoolSize(getIntegerConfigProperty("maxPoolSize", 500));
    executor.setQueueCapacity(getIntegerConfigProperty("queueCapacity", 10000));
    executor.setThreadNamePrefix("cmatta-Executor-");
    return new ExceptionHandlingAsyncTaskExecutor(executor);
  }

  private Integer getIntegerConfigProperty(String property, int defaultValue) {
    Integer value = propertyResolver.getProperty(property, Integer.class, defaultValue);
    log.info("Setting {}={}", property, value);
    return value;
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new SimpleAsyncUncaughtExceptionHandler();
  }
}
