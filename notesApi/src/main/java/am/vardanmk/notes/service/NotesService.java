package am.vardanmk.notes.service;

import am.vardanmk.notes.domain.Notes;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotesService {

    Mono<Notes> getUserNote(String userEmail, long noteId);
    Flux<Notes> getAllUserNotes(String userEmail);
    Mono<Notes> updateUserNote(String userEmail, long noteId, Notes note);
    Mono<Notes> saveUserNote(String userEmail, Notes note);
    Mono<Void> deleteUserNote(String userEmail, long noteId);
}
