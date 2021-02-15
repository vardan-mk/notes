package am.vardanmk.notes.funcionalendpoints;

import am.vardanmk.notes.handlers.NotesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class NotesRoute {

    @Bean
    public RouterFunction<ServerResponse> route(NotesHandler handler) {
        return RouterFunctions
                .route(GET("/api/v1/notes").and(accept(APPLICATION_JSON)), handler::getUserAllNotes)
                .andRoute(GET("/api/v1/notes/{noteId}").and(accept(APPLICATION_JSON)), handler::getUserNote)
                .andRoute(POST("/api/v1/notes/addNote").and(accept(APPLICATION_JSON)), handler::addNewNote)
                .andRoute(PUT("/api/v1/notes/update/{noteId}").and(accept(APPLICATION_JSON)), handler::updateUserNote)
                .andRoute(DELETE("/api/v1/notes/delete/{noteId}").and(accept(APPLICATION_JSON)), handler::deleteUserNote);
    }
}