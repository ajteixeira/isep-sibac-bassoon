package org.sibac.bassoon.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Spring Boot entry point for the Bassoon REST API. */
@SpringBootApplication
public class BassoonApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(BassoonApiApplication.class, args);
  }
}
