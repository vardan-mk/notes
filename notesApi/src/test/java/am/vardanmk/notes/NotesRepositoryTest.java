package am.vardanmk.notes;

import am.vardanmk.notes.domain.Notes;
import am.vardanmk.notes.repository.NotesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
public class NotesRepositoryTest {


    private NotesRepository notesRepo;

    @Autowired
    public NotesRepositoryTest(NotesRepository notesRepo) {
        this.notesRepo = notesRepo;
    }

    private Notes getNotes(String userEmail, String title, String note) {
        Notes note1 = new Notes();
        note1.setUserEmail(userEmail);
        note1.setTitle(title);
        note1.setNote(note);
        return  note1;
    }

    @Test
    public void testFindByUserEmailAndNoteId() {

        Notes note1 = getNotes("testUser1@mail.com", "test title 1", "test note 1");
        Notes note2 = getNotes("testUser2@mail.com", "test title 2", "test note 2");
        Notes note3 = getNotes("testUser3@mail.com", "test title 3", "test note 3");
        Notes note4 = getNotes("testUser1@mail.com", "test title 4", "test note 4");

        Flux<Notes> notesFlux = Flux.just(note1, note2, note3, note4)
                .flatMap(notesRepo::save)
                .thenMany(notesRepo.findNotesByUserEmail("testUser1@gmail.com"));

        StepVerifier.create(notesFlux).assertNext(next -> {
            assertThat(next.getUserEmail()).isEqualTo("testUser1@gmail.com");
            assertThat(next.getTitle()).isEqualTo("test title 1");
            assertThat(next.getNote()).isEqualTo("test note 1");
        })
        .assertNext(next -> {
            assertThat(next.getUserEmail()).isEqualTo("testUser1@mail.com");
            assertThat(next.getTitle()).isEqualTo("test title 4");
            assertThat(next.getNote()).isEqualTo("test note 4");
        })
        .verifyComplete();
    }
}
