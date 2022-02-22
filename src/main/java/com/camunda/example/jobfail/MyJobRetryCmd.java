package com.camunda.example.jobfail;

import lombok.extern.java.Log;
import org.camunda.bpm.engine.impl.cmd.DefaultJobRetryCmd;
import org.camunda.bpm.engine.impl.cmd.JobRetryCmd;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.JobEntity;

@Log
public class MyJobRetryCmd extends DefaultJobRetryCmd {

    public static final String NO_RETRY_MSG = "no_retry";

    public MyJobRetryCmd(String jobId, Throwable exception) {
        super(jobId, exception);
    }

    public Object execute(CommandContext commandContext) {
        log.info("Executing MyJobRetryCmd for job" + jobId + "and exception message" + exception.getMessage());
        if (super.exception.getMessage().equals(NO_RETRY_MSG)) {
            return new NoRetriesJobRetryCmd(jobId, exception).execute(commandContext);
        } else {
            return super.execute(commandContext);
        }
    }

    public static class NoRetriesJobRetryCmd extends JobRetryCmd {

        public NoRetriesJobRetryCmd(String jobId, Throwable exception) {
            super(jobId, exception);
        }

        public Object execute(CommandContext cmdContext) {
            JobEntity job = getJob();
            job.unlock();
            logException(job);
            job.setRetries(0);
            notifyAcquisition(cmdContext);
            return null;
        }
    }
}
