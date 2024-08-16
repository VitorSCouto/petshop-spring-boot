package dev.vitor.petshop.pet;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.annotation.Secured;


import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/all-breeds")
@RequiredArgsConstructor
public class RacaController {

    private final RacaRepository racaRepository;

    // Endpoint para listar todas as raças
    @GetMapping
    public ResponseEntity<List<Raca>> getAllRacas() {
        List<Raca> racas = racaRepository.findAll();
        return ResponseEntity.ok(racas);
    }

    // Endpoint para adicionar uma nova raça (apenas ADMIN pode acessar)
    @PostMapping
    @Secured("ADMIN")
    public ResponseEntity<String> addRaca(@RequestBody Raca raca) { //parametro descricao
        racaRepository.save(raca);
        return ResponseEntity.ok("Raça adicionada com sucesso.");
    }

    // Endpoint para remover uma raça (apenas ADMIN pode acessar)
    @DeleteMapping("/{id}")
    @Secured("ADMIN")
    public ResponseEntity<String> removeRaca(@PathVariable Long id) {
        if (racaRepository.existsById(id)) {
            racaRepository.deleteById(id);
            return ResponseEntity.ok("Raça removida com sucesso.");
        } else {
            return ResponseEntity.badRequest().body("Raça não encontrada.");
        }
    }
}
