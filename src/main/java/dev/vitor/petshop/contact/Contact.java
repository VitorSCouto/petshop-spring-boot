package dev.vitor.petshop.contact;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único

    private Long clientId; // Identificador do cliente

    private String tag; // Tag para identificação (ex: "work", "home")

    private String tipo; // Tipo do contato (e-mail, telefone)

    private String valor; // Valor do contato (ex: e-mail address, phone number)

    // Getters e Setters gerados pelo Lombok
}
