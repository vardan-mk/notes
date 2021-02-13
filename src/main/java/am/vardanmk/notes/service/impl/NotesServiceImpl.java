package am.vardanmk.notes.service.impl;

import am.vardanmk.notes.domain.Notes;
import am.vardanmk.notes.repository.NotesRepository;
import am.vardanmk.notes.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class NotesServiceImpl implements NotesService {

    private final NotesRepository notesRepo;

    @Autowired
    public NotesServiceImpl(NotesRepository notesRepo) {
        this.notesRepo = notesRepo;
    }


    @Override
    public Mono<Notes> getUserNote(String userEmail, long noteId) {
        return notesRepo.findByNoteIdAndUserEmail(noteId, userEmail);
    }

    @Override
    public Flux<Notes> getAllUserNotes(String userEmail) {
        return notesRepo.findNotesByUserEmail(userEmail);
    }

    @Override
    public Mono<Notes> saveUserNote(String userEmail, Notes note) {
        note.setUserEmail(userEmail);

        return notesRepo.save(note);
    }

    @Override
    public Mono<Notes> updateUserNote(String userEmail, long noteId, Notes note) {

        return getUserNote(userEmail, noteId).map(n -> {n.setNote(note.getNote() == null ? n.getNote() : note.getNote());
                                                     n.setTitle(note.getTitle() == null  ? n.getTitle() : note.getTitle());
                                                     n.setLastUpdateTime(LocalDateTime.now());
                                                     n.setUserEmail(n.getUserEmail());
                                                     return n;})
                                        .flatMap(n -> saveUserNote(userEmail, n));
    }

    @Override
    public Mono<Void> deleteUserNote(String userEmail, long noteId) {

        Mono<Notes> note = getUserNote(userEmail, noteId);
        return notesRepo.delete(Objects.requireNonNull(note.block()));

    }
}