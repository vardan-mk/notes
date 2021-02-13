package am.vardanmk.etl.config;

import am.vardanmk.etl.model.Notes;
import am.vardanmk.etl.batch.JobCompletionNotificationListener;
import am.vardanmk.etl.batch.NotesItemReader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Value("${output.directoryPath}")
    private String outputDirectoryPath;

    @Value("${output.jsonFileName}")
    private String outputJsonFileName;

    @Autowired
    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    ItemReader<Notes> notesItemReader() {
        return new NotesItemReader();
    }

    @Bean
    public JsonFileItemWriter<Notes> jsonFileItemWriter() {
        return new JsonFileItemWriterBuilder<Notes>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(new FileSystemResource(outputDirectoryPath +"/" + outputJsonFileName))
                .name("notesJsonFileItemWriter")
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(exportToJSON())
                .end()
                .build();
    }

    @Bean
    public Step exportToJSON() {
        return stepBuilderFactory.get("exportToJSON")
                .<Notes,Notes> chunk(100)
                .reader(notesItemReader())
                .writer(jsonFileItemWriter())
                .build();
    }
}