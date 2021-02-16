package am.vardanmk.notes.funcionalendpoints;

import am.vardanmk.notes.domain.dto.NotesDto;
import am.vardanmk.notes.handlers.NotesHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class NotesRoute {

    @RouterOperations({
            @RouterOperation(path = "/api/v1/notes", method = RequestMethod.GET,
                    operation = @Operation(operationId = "getUserAllNotes", tags = "note",
                                            summary = "Get all notes for current user",
                            security = @SecurityRequirement(name = "jwtToken"),
                            responses = @ApiResponse(responseCode = "200",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = NotesDto.class)))))),

            @RouterOperation(path = "/api/v1/notes/{noteId}", method = RequestMethod.GET,
                    operation = @Operation(operationId = "getUserNote", tags = "note",
                            summary = "Get current user note by specified id",
                            security = @SecurityRequirement(name = "jwtToken"),
                            parameters = @Parameter(name = "noteId", in = ParameterIn.PATH, description = "Id of the note"),
                            responses = {
                                    @ApiResponse(responseCode = "200"),
                                    @ApiResponse(responseCode = "404", description = "Note with given id not found")
                            })),

            @RouterOperation(path = "/api/v1/notes/addNote", method = RequestMethod.POST,
                    operation = @Operation(operationId = "addNewNote", tags = "note",
                            summary = "Add new note for current user",
                            security = @SecurityRequirement(name = "jwtToken"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = NotesDto.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201"),
                                    @ApiResponse(responseCode = "405", description = "Bad Request")
                            })),
            @RouterOperation(path = "/api/v1/notes/update/{noteId}", method = RequestMethod.PUT,
                    operation = @Operation(operationId = "updateUserNote", tags = "note",
                            summary = "Update an existing note for current user",
                            security = @SecurityRequirement(name = "jwtToken"),
                            parameters = @Parameter(name = "noteId", in = ParameterIn.PATH, description = "Id of the note"),
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = NotesDto.class))),
                            responses = {
                                    @ApiResponse(responseCode = "202"),
                                    @ApiResponse(responseCode = "404", description = "Note with given id not found")
                            })),
            @RouterOperation(path = "/api/v1/notes/delete/{noteId}", method = RequestMethod.DELETE,
                    operation = @Operation(operationId = "deleteUserNote", tags = "note",
                            summary = "Delete an existing note for current user",
                            security = @SecurityRequirement(name = "jwtToken"),
                            parameters = @Parameter(name = "noteId", in = ParameterIn.PATH, description = "Id of the note"),
                            responses = {
                                    @ApiResponse(responseCode = "204"),
                                    @ApiResponse(responseCode = "404", description = "Note with given id not found")
                            }))
    })

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