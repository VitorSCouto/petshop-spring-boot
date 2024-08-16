package dev.vitor.petshop.contact;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import dev.vitor.petshop.user.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/account/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Contact>> getUserContacts(Authentication authentication) {
        String cpf = (String) authentication.getPrincipal();
        User user = userRepository.findBycpf(cpf).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        List<Contact> contacts;

        if (isAdmin) {
            contacts = contactRepository.findAll(); // ADMIN pode ver todos os contatos
        } else {
            contacts = contactRepository.findByClientId(user.getId().longValue()); // Usuário comum vê apenas seus contatos
        }

        return ResponseEntity.ok(contacts);
    }

    @PostMapping
    public ResponseEntity<String> addContact(@RequestBody ContactRequest contactRequest, Authentication authentication) {
        String cpf = (String) authentication.getPrincipal();
        User user = userRepository.findBycpf(cpf).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        Long clientIdToAdd = contactRequest.getClientId();
        
        // Se o usuário não for ADMIN, ele só pode adicionar contatos para si mesmo
        if (!isAdmin && !clientIdToAdd.equals(user.getId().longValue())) {
            return ResponseEntity.badRequest().body("Usuário não tem permissão para adicionar contatos para outros clientes.");
        }

        Contact contact = new Contact();
        contact.setClientId(clientIdToAdd);
        contact.setTag(contactRequest.getTag());
        contact.setTipo(contactRequest.getTipo());
        contact.setValor(contactRequest.getValor());

        contactRepository.save(contact);
        return ResponseEntity.ok("Contato adicionado com sucesso.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable Long id, Authentication authentication) {
        String cpf = (String) authentication.getPrincipal();
        User user = userRepository.findBycpf(cpf).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        // Verifica se o usuário é ADMIN
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact == null) {
            return ResponseEntity.badRequest().body("Contato não encontrado.");
        }

        if (!isAdmin && !contact.getClientId().equals(user.getId().longValue())) {
            return ResponseEntity.badRequest().body("Contato não pertence ao usuário.");
        }

        contactRepository.deleteById(id);
        return ResponseEntity.ok("Contato removido com sucesso.");
    }
}
