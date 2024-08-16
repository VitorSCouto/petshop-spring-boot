package dev.vitor.petshop.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String token;
    private String message; // Novo campo para a mensagem
}
