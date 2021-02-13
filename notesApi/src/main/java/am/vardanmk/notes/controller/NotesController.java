package am.vardanmk.notes.controller;

import am.vardanmk.notes.domain.Notes;
import am.vardanmk.notes.service.NotesService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.security.Principal;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("api/v1")
public class NotesController {

    private static final Logger log = getLogger(NotesController.class);
    private final NotesService notesService;

    @Autowired
    public NotesController (NotesService notesService) {
        this.notesService = notesService;
    }


    @GetMapping(value = "/notes", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Notes> getUserAllNotes(@AuthenticationPrincipal Principal user) {
       log.info("list of notes for user " + user.getName());
       return notesService.getAllUserNotes(user.getName());
    }

    @GetMapping(value = "/notes/{noteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Notes> getUserNote(@AuthenticationPrincipal Principal user, @PathVariable long noteId) {
        log.info("note with id " + noteId + ", for user " + user.getName());
        return notesService.getUserNote(user.getName(), noteId);
    }

    @PostMapping(value = "/addnote")
    public Mono<Notes> addNewNote(@AuthenticationPrincipal Principal user, @Valid @RequestBody Notes note) {
        log.info("adding note for user " + user.getName());
        return notesService.saveUserNote(user.getName(), note);
    }

    @PutMapping(value = "/notes/update/{noteId}", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Notes> updateUserNote(@AuthenticationPrincipal Principal user,
                                      @PathVariable long noteId,
                                      @RequestBody Notes note) {
        log.info("updating note with id " + noteId + ", for user " + user.getName());
        return notesService.updateUserNote(user.getName(), noteId, note);
    }

    @DeleteMapping(value = "/notes/delete/{noteId}")
    public Mono<Void> deleteUserNote(@AuthenticationPrincipal Principal user, @PathVariable long noteId) {
        log.info("deleting note with id" + noteId + ", for user " + user.getName());
        return notesService.deleteUserNote(user.getName(), noteId);
    }
}
