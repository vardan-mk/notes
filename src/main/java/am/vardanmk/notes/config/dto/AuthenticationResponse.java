package am.vardanmk.notes.config.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthenticationResponse implements Serializable {

    private final String authToken;

    public AuthenticationResponse(String authToken) { this.authToken = authToken; }
}
