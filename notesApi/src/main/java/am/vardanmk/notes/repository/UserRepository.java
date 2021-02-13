package am.vardanmk.notes.repository;

import am.vardanmk.notes.domain.Users;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<Users, Long> {
    Mono<Users> findByEmail(String email);
}
