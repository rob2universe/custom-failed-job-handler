package com.camunda.example.jobfail;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class FailingDelegate implements JavaDelegate {

  @Override
  public void execute(DelegateExecution execution) throws Exception {
    
    if (execution.hasVariable("fail")) {
      throw new RuntimeException((String)execution.getVariable("fail"));
    }
  }
}
