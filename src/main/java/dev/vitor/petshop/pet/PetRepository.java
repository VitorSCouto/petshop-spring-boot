package dev.vitor.petshop.pet;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByClientId(Long clientId);
    Optional<Pet> findById(Long id);
}
