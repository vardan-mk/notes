package am.vardanmk.notes.service;

import am.vardanmk.notes.domain.dto.NotesDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotesService {

    Mono<NotesDto> getUserNote(String userEmail, long noteId);
    Flux<NotesDto> getAllUserNotes(String userEmail);
    Mono<NotesDto> saveUserNote(String userEmail, NotesDto note);
    Mono<NotesDto> updateUserNote(String userEmail, long noteId, NotesDto note);
    Mono<NotesDto> deleteUserNote(String userEmail, long noteId);
}
