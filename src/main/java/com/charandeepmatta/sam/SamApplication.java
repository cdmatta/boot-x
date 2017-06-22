package com.charandeepmatta.sam;

import static org.springframework.boot.Banner.Mode.OFF;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SamApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(SamApplication.class);
    app.setBannerMode(OFF);
    app.run(args);
  }
}
