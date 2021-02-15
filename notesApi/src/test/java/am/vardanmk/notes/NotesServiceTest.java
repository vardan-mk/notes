package am.vardanmk.notes;

import am.vardanmk.notes.domain.Notes;
import am.vardanmk.notes.mapper.NotesMapper;
import am.vardanmk.notes.repository.NotesRepository;
import am.vardanmk.notes.service.NotesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class NotesServiceTest {

    @MockBean
    private NotesRepository repository;

    private NotesService service;

    private NotesMapper mapper;

    private List<Notes> mockList;

    @Autowired
    public NotesServiceTest(NotesService service, NotesMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    private Notes getNotes(String userEmail, String title, String note) {
        Notes note1 = new Notes();
        note1.setUserEmail(userEmail);
        note1.setTitle(title);
        note1.setNote(note);
        return  note1;
    }

    @BeforeEach
    void beforeEach() {
        this.mockList = Arrays.asList(
                getNotes("testUser1@mail.com", "test title 1", "test note 1"),
                getNotes("testUser1@mail.com", "test title 2", "test note 2"),
                getNotes("testUser1@mail.com", "test title 3", "test note 3"));
    }


    @Test
    public void testGetAll() {
        given(repository.findAll()).willReturn(Flux.fromIterable(mockList));

        StepVerifier.create(service.getAllUserNotes("testUser1@mail.com"))
                .expectNext(mapper.mapToNotesDto(mockList.get(0)))
                .expectNext(mapper.mapToNotesDto(mockList.get(1)))
                .expectNext(mapper.mapToNotesDto(mockList.get(2)))
                .expectComplete()
                .verify();
    }

    @Test
    public void testSaveOk() {
        given(repository.findByNoteIdAndUserEmail(any(), any())).willReturn(Mono.empty());
        given(repository.save(any())).willReturn(Mono.just(mockList.get(0)));

        StepVerifier.create(service.saveUserNote("testUser1@mail.com",mapper.mapToNotesDto(mockList.get(0))))
                .expectNext(mapper.mapToNotesDto(mockList.get(0)))
                .expectComplete()
                .verify();
    }

    @Test
    public void testSaveShouldFail() {
        Notes note = getNotes("testUser1@mail.com", "test title 1", "test note 1");
        given(repository.findByNoteIdAndUserEmail(any(), any())).willReturn(Mono.just(note));
        given(repository.save(any())).willReturn(Mono.just(mockList.get(0)));

        StepVerifier.create(service.saveUserNote("testUser1@mail.com", mapper.mapToNotesDto(mockList.get(0))))
                .expectError()
                .verify();
    }

    @Test
    public void testDelete() {
        given(repository.delete(any())).willReturn(Mono.empty());
        given(repository.findByNoteIdAndUserEmail(any(Long.class), any(String.class))).willReturn(Mono.just(mockList.get(0)));

        StepVerifier.create(service.deleteUserNote("testUser1@mail.com", 1L))
                .expectNext(mapper.mapToNotesDto(mockList.get(0)))
                .expectComplete()
                .verify();
    }


}
