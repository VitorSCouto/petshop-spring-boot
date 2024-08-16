package dev.vitor.petshop.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // Endpoint para listar todos os usuários
    @GetMapping
    @Secured("ADMIN") // Apenas ADMIN pode acessar
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Endpoint para adicionar ou atualizar um usuário
    @PostMapping("/save")
    @Secured("ADMIN") // Apenas ADMIN pode acessar
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().length() != 11) {
            return ResponseEntity.badRequest().body("O CPF deve ter exatamente 11 caracteres.");
        }

        // Verifica se o usuário existe
        var existingUser = userRepository.findBycpf(user.getUsername());
        if (existingUser.isPresent()) {
            // Atualiza o usuário existente
            User updateUser = existingUser.get();
            updateUser.setName(user.getName());
            updateUser.setEmail(user.getEmail());
            updateUser.setPassword(user.getPassword()); // Normalmente, a senha seria criptografada antes
            updateUser.setRole(user.getRole());
            updateUser.setRegistrationDate(user.getRegistrationDate());
            userRepository.save(updateUser);
            return ResponseEntity.ok("Usuário atualizado com sucesso.");
        } else {
            // Adiciona novo usuário
            userRepository.save(user);
            return ResponseEntity.ok("Usuário adicionado com sucesso.");
        }
    }

    // Endpoint para excluir um usuário
    @DeleteMapping("/{id}")
    @Secured("ADMIN") // Apenas ADMIN pode acessar
    public ResponseEntity<String> removeUser(@PathVariable Integer id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("id nulo");
        }

        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        userRepository.delete(user);
        return ResponseEntity.ok("Usuário removido com sucesso.");
    }
}
