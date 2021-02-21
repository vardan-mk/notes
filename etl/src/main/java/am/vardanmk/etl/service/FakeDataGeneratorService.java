package am.vardanmk.etl.service;

import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class FakeDataGeneratorService {

    private final JdbcTemplate jdbcTemplate;

    DataFactory dataFactory = new DataFactory();

    @Autowired
    public FakeDataGeneratorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void generateAndLoadFakeNotesDataToDB() {

        List<String> userEmailsList = getFakeUsersAndLoadToDB();
        int lstSize = userEmailsList.size();

        for (int i = 0; i < 1500; i++) {
            int randomIndex = new Random().nextInt(lstSize);
            String userEmail = userEmailsList.get(randomIndex);
            String title = dataFactory.getRandomText(50);
            String note = dataFactory.getRandomText(2, 1000);
            String INSERT_QUERY = "INSERT INTO notes (user_email, title, note) VALUES (?, ?, ?)";
            jdbcTemplate.update(INSERT_QUERY, userEmail, title, note);
        }
    }

    private List<String> getFakeUsersAndLoadToDB() {

        List<String> userEmailsList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            String userEmail = dataFactory.getEmailAddress();
            String userPassword = dataFactory.getRandomChars(8,15);
            userEmailsList.add(userEmail);
            String INSERT_QUERY = "INSERT INTO usr (email, password, role) VALUES (?, ?, ?)";
            jdbcTemplate.update(INSERT_QUERY, userEmail, userPassword, "ROLE_USER");
        }

        return userEmailsList;
    }
}