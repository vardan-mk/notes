package am.vardanmk.etl.controller;


import am.vardanmk.etl.service.StorageService;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
public class BatchController {

    @Value("${output.directoryPath}")
    private String outputDirectoryPath;

    @Value("${output.jsonFileName}")
    private String outputJsonFileName;

    @Value("${output.csvFileName}")
    private String outputCsvFileName;

    private final JobLauncher jobLauncher;

    private final Job job;

    private StorageService service;

    @Autowired
    public BatchController(JobLauncher jobLauncher, Job job, StorageService service) {
        this.jobLauncher = jobLauncher;
        this.job = job;
        this.service =service;
    }

    @GetMapping("/load")
    public @ResponseBody BatchStatus load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
                                         JobRestartException, JobInstanceAlreadyCompleteException {

        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(job, parameters);

        // after batch process finished upload result files to AWS S3
        service.uploadFile(new File(outputDirectoryPath + "/" + outputJsonFileName));
        service.uploadFile(new File(outputDirectoryPath + "/" + outputCsvFileName));

        return jobExecution.getStatus();
    }
}
