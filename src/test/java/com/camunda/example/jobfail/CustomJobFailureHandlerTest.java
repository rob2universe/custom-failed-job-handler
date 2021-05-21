package com.camunda.example.jobfail;

import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.Before;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.junit.Assert.assertEquals;

@Deployment(resources = "process.bpmn")
public class CustomJobFailureHandlerTest extends AbstractProcessEngineRuleTest {

    private static final String PD_KEY = "failed-job-handler";

    @Before
    public void setUp() throws RuntimeException {
        var engineConfig = processEngine.getProcessEngineConfiguration();
        engineConfig.getProcessEnginePlugins().add(new JobFailureHandlerProcessEnginePlugin());
        engineConfig.setJobExecutorActivate(true);
        engineConfig.setHistory(ProcessEngineConfiguration.HISTORY_FULL);
    }

//    @Test
    public void testRegularFailure() throws InterruptedException {
        var pi = processEngine().getRuntimeService().startProcessInstanceByKey(
                PD_KEY, withVariables("fail", "somethingElse"));
        assertThat(pi).job();
        Thread.sleep(5000);
        assertEquals(3, historyService().createHistoricJobLogQuery().processInstanceId(pi.getId()).failureLog().count());
    }

//    @Test
    public void testNoRetryFailure() throws InterruptedException {
        var pi = processEngine().getRuntimeService().startProcessInstanceByKey(
                PD_KEY, withVariables("fail", MyJobRetryCmd.NORETRY_MSG));
        assertThat(pi).job();
        Thread.sleep(1000);
        assertEquals(0, historyService().createHistoricJobLogQuery().processInstanceId(pi.getId()).failureLog().count());
    }

}
