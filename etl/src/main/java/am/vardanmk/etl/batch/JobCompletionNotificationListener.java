package am.vardanmk.etl.batch;

import org.slf4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = getLogger(JobCompletionNotificationListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("######## Job Started ##########");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
        } else if(jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("!!! DB to file failed with following exceptions");

            List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
            for(Throwable th : exceptionList){
                log.error("exception :" +th.getLocalizedMessage());
            }
        }
    }
}