package dev.vitor.petshop.contact;

import lombok.Data;

@Data
public class ContactRequest {
    private Long clientId;
    private String tag;
    private String tipo; // Tipo do contato (e-mail, telefone)
    private String valor; // Valor do contato
}
