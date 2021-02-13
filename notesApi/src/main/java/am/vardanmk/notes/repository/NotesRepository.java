package am.vardanmk.notes.repository;

import am.vardanmk.notes.domain.Notes;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NotesRepository extends ReactiveCrudRepository<Notes, Long> {

    Flux<Notes> findNotesByUserEmail(String email);

    Mono<Notes> findByNoteIdAndUserEmail(long noteId, String email);
}
