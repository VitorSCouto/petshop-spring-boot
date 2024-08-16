package dev.vitor.petshop.address;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import dev.vitor.petshop.user.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/account/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    // Endpoint para listar todos os endereços associados ao usuário autenticado
    @GetMapping
    public ResponseEntity<List<Address>> getAddressesForAuthenticatedUser(Authentication authentication) {
        User user = userRepository.findBycpf(authentication.getName()).orElseThrow();
        boolean isAdmin = user.getRole().equals(Role.ADMIN);
    
        List<Address> addresses;
    
        if (isAdmin) {
            // ADMIN pode ver todos os endereços
            addresses = addressRepository.findAll();
        } else {
            // Usuário comum vê apenas seus próprios endereços
            addresses = addressRepository.findByUserId(user.getId().longValue());
        }
    
        return ResponseEntity.ok(addresses);
    }

    // Endpoint para adicionar um endereço
    @PostMapping
    public ResponseEntity<String> addAddress(@RequestBody AddressRequest addressRequest, Authentication authentication) {
        String cpf = (String) authentication.getPrincipal();
        User user = userRepository.findBycpf(cpf).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        boolean isAdmin = user.getRole().equals(Role.ADMIN);
        Long userIdToAdd = addressRequest.getUserId();

        // Se o usuário não for ADMIN, ele só pode adicionar endereços para si mesmo
        if (!isAdmin && !userIdToAdd.equals(user.getId().longValue())) {
            return ResponseEntity.badRequest().body("Usuário não tem permissão para adicionar endereços para outros usuários.");
        }

        Address address = Address.builder()
                .user(userRepository.findById(userIdToAdd.intValue()).orElse(null))
                .street(addressRequest.getStreet())
                .city(addressRequest.getCity())
                .neighborhood(addressRequest.getNeighborhood())
                .complement(addressRequest.getComplement())
                .tag(addressRequest.getTag())
                .build();

        addressRepository.save(address);
        return ResponseEntity.ok("Endereço adicionado com sucesso.");
    }


    // Endpoint para remover um endereço
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeAddress(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findBycpf(authentication.getName()).orElseThrow();
        Address address = addressRepository.findById(id).orElseThrow();

        if (!authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) &&
            !address.getId().equals(user.getId().longValue())) {
            return ResponseEntity.status(403).body("Forbidden access"); // Forbidden access
        }

        addressRepository.delete(address);
        return ResponseEntity.ok("Endereço removido com sucesso.");
    }
}
