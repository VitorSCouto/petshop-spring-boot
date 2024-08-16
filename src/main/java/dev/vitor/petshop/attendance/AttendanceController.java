package dev.vitor.petshop.attendance;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate; // Certifique-se de que esta é a importação correta


import dev.vitor.petshop.pet.Pet;
import dev.vitor.petshop.pet.PetRepository;
import dev.vitor.petshop.user.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/account/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @GetMapping
    public ResponseEntity<List<Attendance>> getUserAttendances(Authentication authentication) {
        String cpf = (String) authentication.getPrincipal();
        User user = userRepository.findBycpf(cpf).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }

        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        List<Attendance> attendances;

        if (isAdmin) {
            attendances = attendanceRepository.findAll(); // ADMIN pode ver todos os atendimentos
        } else {
            List<Pet> pets = petRepository.findByClientId(user.getId().longValue());
            attendances = attendanceRepository.findByPetIn(pets); // Usuário comum vê apenas os atendimentos dos seus pets
        }

        return ResponseEntity.ok(attendances);
    }

    @PostMapping
    public ResponseEntity<String> addAttendance(@RequestBody AttendanceRequest attendanceRequest, Authentication authentication) {
        String cpf = (String) authentication.getPrincipal();
        User user = userRepository.findBycpf(cpf).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        Pet pet = petRepository.findById(attendanceRequest.getPetId()).orElse(null);
        if (pet == null) {
            return ResponseEntity.badRequest().body("Pet não encontrado.");
        }

        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        if (!isAdmin && !user.getId().equals(pet.getClientId().longValue())) {
            return ResponseEntity.badRequest().body("Pet não pertence ao usuário.");
        }

        Attendance attendance = new Attendance();
        attendance.setPet(pet);
        attendance.setDescricao(attendanceRequest.getDescricao());
        attendance.setValor(attendanceRequest.getValor());
        attendance.setData(LocalDate.now());

        attendanceRepository.save(attendance);
        return ResponseEntity.ok("Atendimento adicionado com sucesso.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttendance(@PathVariable Long id, Authentication authentication) {
        String cpf = (String) authentication.getPrincipal();
        User user = userRepository.findBycpf(cpf).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        // Verifica se o usuário é ADMIN
        boolean isAdmin = user.getRole().equals(Role.ADMIN);

        Attendance attendance = attendanceRepository.findById(id).orElse(null);
        if (attendance == null) {
            return ResponseEntity.badRequest().body("Atendimento não encontrado.");
        }

        if (!isAdmin && !attendance.getPet().getClientId().equals(user.getId().longValue())) {
            return ResponseEntity.badRequest().body("Atendimento não pertence ao usuário.");
        }

        attendanceRepository.deleteById(id);
        return ResponseEntity.ok("Atendimento removido com sucesso.");
    }
}
