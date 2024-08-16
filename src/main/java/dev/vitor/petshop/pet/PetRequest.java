package dev.vitor.petshop.pet;

import lombok.Data;

@Data
public class PetRequest {
    private Long clientId;
    private Long racaId; // ID da raça
    private java.time.LocalDate dataNascimento; // Data de nascimento como String
    private String nome;

    // Getters e setters são gerados automaticamente pela anotação @Data do Lombok
}
