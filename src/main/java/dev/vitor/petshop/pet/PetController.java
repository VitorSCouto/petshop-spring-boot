package dev.vitor.petshop.pet;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import dev.vitor.petshop.user.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/my-pets")
@RequiredArgsConstructor
public class PetController {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final RacaRepository racaRepository;

    @GetMapping
public ResponseEntity<List<Pet>> getUserPets(Authentication authentication) {
    String cpf = (String) authentication.getPrincipal();
    User user = userRepository.findBycpf(cpf).orElse(null);

    if (user == null) {
        return ResponseEntity.badRequest().body(null);
    }

    boolean isAdmin = user.getRole().equals(Role.ADMIN);
    List<Pet> pets;

    if (isAdmin) {
        pets = petRepository.findAll(); // ADMIN pode ver todos os pets
    } else {
        pets = petRepository.findByClientId(user.getId().longValue()); // Usuário comum vê apenas seus pets
    }

    return ResponseEntity.ok(pets);
}

@PostMapping
public ResponseEntity<String> addPet(@RequestBody PetRequest petRequest, Authentication authentication) {
    String cpf = (String) authentication.getPrincipal();
    User user = userRepository.findBycpf(cpf).orElse(null);

    if (user == null) {
        return ResponseEntity.badRequest().body("Usuário não encontrado.");
    }

    Raca raca = racaRepository.findById(petRequest.getRacaId()).orElse(null);
    if (raca == null) {
        return ResponseEntity.badRequest().body("Raça não encontrada.");
    }

    // Verifica se o usuário é ADMIN
    boolean isAdmin = user.getRole().equals(Role.ADMIN);

    Pet pet = new Pet();
    if (isAdmin) {
        pet.setClientId(petRequest.getClientId());
    } else {
        pet.setClientId(user.getId().longValue());
    }
    pet.setRaca(raca);
    pet.setDataNascimento(petRequest.getDataNascimento());
    pet.setNome(petRequest.getNome());

    petRepository.save(pet);
    return ResponseEntity.ok("Pet adicionado com sucesso.");
}

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePet(@PathVariable Long id, Authentication authentication) {
        String cpf = (String) authentication.getPrincipal();
        User user = userRepository.findBycpf(cpf).orElse(null);
    
        if (user == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }
    
        // Verifica se o usuário é ADMIN
        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        
        Pet pet = petRepository.findById(id).orElse(null);
        if (pet == null) {
            return ResponseEntity.badRequest().body("Pet não encontrado.");
        }
        
        if (!isAdmin && !pet.getClientId().equals(user.getId().longValue())) {
            return ResponseEntity.badRequest().body("Pet não pertence ao usuário.");
        }
    
        petRepository.deleteById(id);
        return ResponseEntity.ok("Pet removido com sucesso.");
    }
}
