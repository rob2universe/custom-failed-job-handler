package com.camunda.example;

import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class Application {

  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }

  private static final String PROCESS_DEFINITION_KEY = "failed-job-handler";

  @PostDeploy
  public void onDeploymentFinished(ProcessEngine processEngine) {

    processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY, Map.of("fail","somethingNotnoretry"));
    processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
  }

}