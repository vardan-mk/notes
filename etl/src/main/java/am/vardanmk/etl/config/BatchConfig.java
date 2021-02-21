package am.vardanmk.etl.config;

import am.vardanmk.etl.batch.NoteItemProcessor;
import am.vardanmk.etl.batch.NotesRowMapper;
import am.vardanmk.etl.model.Notes;
import am.vardanmk.etl.batch.JobCompletionNotificationListener;

import am.vardanmk.etl.service.StorageService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;


@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

    private final StorageService storageService;

    @Value("${output.directoryPath}")
    private String outputDirectoryPath;

    @Value("${output.jsonFileName}")
    private String outputJsonFileName;

    @Value("${output.csvFileName}")
    private String outputCsvFileName;

    @Autowired
    public BatchConfig(JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory,
                       DataSource dataSource,
                       StorageService storageService) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
        this.storageService = storageService;
    }


    @Bean
    public JdbcCursorItemReader<Notes> notesItemReader(){
        JdbcCursorItemReader<Notes> cursorItemReader = new JdbcCursorItemReader<>();
        cursorItemReader.setDataSource(dataSource);
        cursorItemReader.setSql("SELECT * FROM notes");
        cursorItemReader.setRowMapper(new NotesRowMapper());
        return cursorItemReader;
    }

    @Bean
    public NoteItemProcessor noteItemProcessor(){
        return new NoteItemProcessor();
    }

    @Bean
    public JsonFileItemWriter<Notes> jsonFileItemWriter() {
        return new JsonFileItemWriterBuilder<Notes>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(new FileSystemResource(outputDirectoryPath +"/" + outputJsonFileName))
                .append(true)
                .shouldDeleteIfEmpty(true)
                .shouldDeleteIfExists(true)
                .name("notesJsonFileItemWriter")
                .build();
    }

    @Bean
    public FlatFileItemWriter<Notes> csvFileItemWriter(){

        FlatFileItemWriter<Notes> writer = new FlatFileItemWriter<Notes>();
        writer.setResource(new FileSystemResource(outputDirectoryPath +"/" + outputCsvFileName));
        writer.setAppendAllowed(true);
        writer.setShouldDeleteIfEmpty(true);
        writer.setShouldDeleteIfExists(true);

        DelimitedLineAggregator<Notes> lineAggregator = new DelimitedLineAggregator<Notes>();
        lineAggregator.setDelimiter(",");

        BeanWrapperFieldExtractor<Notes>  fieldExtractor = new BeanWrapperFieldExtractor<Notes>();
        fieldExtractor.setNames(new String[]{"noteId","userEmail","title","note","createTime","LastUpdateTime"});
        lineAggregator.setFieldExtractor(fieldExtractor);

        writer.setLineAggregator(lineAggregator);
        return writer;
    }

    @Bean
    public Step exportToJson() {
        return stepBuilderFactory.get("exportToJson")
                .<Notes,Notes> chunk(100)
                .reader(notesItemReader())
                .writer(jsonFileItemWriter())
                .build();
    }

    @Bean
    public Step exportToCsv() {
        return stepBuilderFactory.get("exportToCsv")
                .<Notes,Notes> chunk(100)
                .reader(notesItemReader())
                .processor(noteItemProcessor())
                .writer(csvFileItemWriter())
                .build();
    }

    @Bean
    public Job importEtlJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importEtlJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(exportToCsv())
                .next(exportToJson())
                .end()
                .build();
    }
}