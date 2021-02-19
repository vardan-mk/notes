package am.vardanmk.notes.handlers;

import am.vardanmk.notes.domain.dto.NotesDto;
import am.vardanmk.notes.service.NotesService;
import am.vardanmk.notes.mapper.ExceptionMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class NotesHandler {

    private static final Logger log = getLogger(NotesHandler.class);
    private final NotesService notesService;

    @Autowired
    public NotesHandler (NotesService notesService) {
        this.notesService = notesService;
    }


    public Mono<ServerResponse> getUserAllNotes(ServerRequest request) {

        log.info("list of notes for user ");

        Flux<NotesDto> notes = request.principal()
                .map(Principal::getName)
                .flatMapMany(notesService::getAllUserNotes);

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(notes, NotesDto.class);
    }

    public Mono<ServerResponse> getUserNote(ServerRequest request) {

        long noteId = Long.parseLong(request.pathVariable("noteId"));
        log.info("note with id " + noteId + ", for user ");

        Mono<NotesDto> note = request
                .principal()
                .map(Principal::getName)
                .flatMap(n -> notesService.getUserNote(n, noteId));

        return  note.flatMap(n -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(note, NotesDto.class))
                        .onErrorResume(ExceptionMapper::mapToServerResponse);
    }

    public Mono<ServerResponse> addNewNote(ServerRequest request) {

        log.info("adding note for user ");
        Mono<NotesDto> note = Mono.zip(request.principal()
                .map(Principal::getName), request.bodyToMono(NotesDto.class))
                .flatMap(tuple -> notesService.saveUserNote(tuple.getT1(), tuple.getT2()));

        return note.flatMap(n -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(note, NotesDto.class))
                        .onErrorResume(ExceptionMapper::mapToServerResponse);
    }


    public Mono<ServerResponse> updateUserNote(ServerRequest request) {
        long noteId = Long.parseLong(request.pathVariable("noteId"));
        log.info("updating note with id " + noteId + ", for user ");

        Mono<NotesDto> note = Mono.zip(request.principal()
                        .map(Principal::getName),
                request.bodyToMono(NotesDto.class))
                .flatMap(tuple -> notesService.updateUserNote(tuple.getT1(), noteId, tuple.getT2()));

        return note.flatMap(n -> ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(note, NotesDto.class))
                        .onErrorResume(ExceptionMapper::mapToServerResponse);
    }

    public Mono<ServerResponse> deleteUserNote(ServerRequest request) {

        long noteId = Long.parseLong(request.pathVariable("noteId"));
        log.info("deleting note with id " + noteId + ", for user " );
        Mono<NotesDto> note = request.principal()
                .map(Principal::getName)
                .flatMap(userEmail -> notesService.deleteUserNote(userEmail, noteId));

        return note.flatMap(n -> ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(note, NotesDto.class))
                .onErrorResume(ExceptionMapper::mapToServerResponse);
    }
}
