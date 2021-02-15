package am.vardanmk.notes;

import am.vardanmk.notes.domain.dto.NotesDto;
import am.vardanmk.notes.exception.BadRequestException;
import am.vardanmk.notes.service.NotesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class NotesHandlerTest {

    private WebTestClient webTestClient;

    @MockBean
    private NotesService service;

    @Autowired
    public NotesHandlerTest(WebTestClient webTestClient) {
        this.webTestClient = webTestClient;
    }

    @WithMockUser
    @Test
    public void testGetAllUserNotes() {
        NotesDto noteDto1 = new NotesDto();
        noteDto1.setUserEmail("test1@mail.ru");
        noteDto1.setTitle("test title 1");
        noteDto1.setNote("test note 1");

        NotesDto noteDto2 = new NotesDto();
        noteDto2.setUserEmail("tes1@mail.ru");
        noteDto2.setTitle("test title 2");
        noteDto2.setNote("test note 2");

        given(service.getAllUserNotes("test1@mail.ru")).willReturn(Flux.just(noteDto1, noteDto2));

        webTestClient.get()
                .uri("/api/v1/notes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(NotesDto.class)
                .value(dtoList -> {
                    assertThat(dtoList).hasSize(2);

                    assertThat(dtoList.get(0).getTitle()).isEqualTo(noteDto1.getTitle());
                    assertThat(dtoList.get(0).getNote()).isEqualTo(noteDto1.getNote());

                    assertThat(dtoList.get(1).getTitle()).isEqualTo(noteDto2.getTitle());
                    assertThat(dtoList.get(1).getNote()).isEqualTo(noteDto2.getNote());
                });
    }

    @WithMockUser
    @Test
    public void testGetUserNote() {
        NotesDto noteDto1 = new NotesDto();
        noteDto1.setNoteId(1L);
        noteDto1.setUserEmail("test1@mail.ru");
        noteDto1.setTitle("test title 1");
        noteDto1.setNote("test note 1");

        NotesDto noteDto2 = new NotesDto();
        noteDto2.setNoteId(2L);
        noteDto2.setUserEmail("tes1@mail.ru");
        noteDto2.setTitle("test title 2");
        noteDto2.setNote("test note 2");

        given(service.getUserNote("test1@mail.ru", 1L)).willReturn(Mono.just(noteDto1));

        webTestClient.get()
                .uri("/api/v1/notes/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(NotesDto.class)
                .value(dtoList -> {
                    assertThat(dtoList).hasSize(1);

                    assertThat(dtoList.get(0).getTitle()).isEqualTo(noteDto1.getTitle());
                    assertThat(dtoList.get(0).getNote()).isEqualTo(noteDto1.getNote());
                });
    }

    @WithMockUser
    @Test
    public void testGetUserNoteWithWrongIdAndUserEmail() {
        given(service.getUserNote("WRONG_USER", 3L)).willReturn(Mono.error(new BadRequestException()));

        webTestClient.get()
                .uri("/api/v1/notes/3")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
