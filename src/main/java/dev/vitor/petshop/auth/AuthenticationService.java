package dev.vitor.petshop.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.vitor.petshop.config.JwtService;
import dev.vitor.petshop.user.Role;
import dev.vitor.petshop.user.User;
import dev.vitor.petshop.user.UserRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        try {
            // Valida o comprimento do CPF
            if (request.getCPF() == null || request.getCPF().length() != 11) {
                return AuthenticationResponse.builder()
                        .message("O CPF deve ter exatamente 11 caracteres.")
                        .build();
            }

            if (userRepository.findBycpf(request.getCPF()).isPresent()) {
                return AuthenticationResponse.builder()
                        .message("CPF já registrado.")
                        .build();
            }

            // Cria e salva o usuário
            var user = User.builder()
                    .name(request.getName())
                    .email(request.getEmail()) //.email(request.getEmail())
                    .cpf(request.getCPF())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.CLIENTE)
                    .registrationDate(LocalDateTime.now())
                    .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .message("Registro bem-sucedido. Bem vindo(a) ao Petshop!")
                    .build();
        } catch (Exception e) {
            // Em caso de erro, retorna uma mensagem de erro
            return AuthenticationResponse.builder()
                    .message("Erro ao registrar o usuário: " + e.getMessage())
                    .build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCpf(),
                        request.getPassword()));
        var user = userRepository.findBycpf(request.getCpf()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
