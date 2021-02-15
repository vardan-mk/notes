package am.vardanmk.notes.mapper;

import am.vardanmk.notes.exception.BadRequestException;
import am.vardanmk.notes.exception.NotFoundException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.slf4j.LoggerFactory.getLogger;

public class ExceptionMapper {

    private static final Logger log = getLogger(ExceptionMapper.class);

    public static Mono<? extends ServerResponse> mapToServerResponse(Throwable ex) {
        if (ex instanceof NotFoundException) {
            return ServerResponse.notFound().build();
        } else if (ex instanceof BadRequestException) {
            return ServerResponse.badRequest().build();
        }

        log.error("Unhandled error: {}", ex.getMessage(), ex);
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
