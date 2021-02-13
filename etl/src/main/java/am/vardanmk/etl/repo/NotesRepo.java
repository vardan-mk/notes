package am.vardanmk.etl.repo;

import am.vardanmk.etl.model.Notes;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepo extends ReactiveCrudRepository<Notes, Long> {

}