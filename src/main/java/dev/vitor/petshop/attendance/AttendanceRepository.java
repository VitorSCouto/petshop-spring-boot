package dev.vitor.petshop.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import dev.vitor.petshop.pet.*;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByPetIn(List<Pet> pets); // Buscar atendimentos relacionados a uma lista de pets
}
