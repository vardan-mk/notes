package am.vardanmk.notes.controller;

import am.vardanmk.notes.config.JwtUtils;
import am.vardanmk.notes.config.dto.AuthenticationRequest;
import am.vardanmk.notes.config.dto.AuthenticationResponse;
import am.vardanmk.notes.domain.Users;
import am.vardanmk.notes.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final static ResponseEntity<AuthenticationResponse> UNAUTHORIZED =
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    private final UserServiceImpl userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserServiceImpl userService,
                          JwtUtils jwtUtils,
                          PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "login operation to retrieve jwt token",  tags = "user" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthenticationResponse.class)))),
            @ApiResponse(responseCode = "405", description = "Bad Request") })
    @PostMapping("/login")
    public Mono<ResponseEntity<AuthenticationResponse>> login(@RequestBody AuthenticationRequest authReq) {

        return userService.findByUsername(authReq.getUsername()).map(userDetails -> {
            return passwordEncoder.encode(authReq.getPassword()).equals(userDetails.getPassword()) ?
                    ResponseEntity.ok(new AuthenticationResponse(jwtUtils.generateToken((Users)userDetails)))
                    : UNAUTHORIZED; })
                .defaultIfEmpty(UNAUTHORIZED);
    }
}
