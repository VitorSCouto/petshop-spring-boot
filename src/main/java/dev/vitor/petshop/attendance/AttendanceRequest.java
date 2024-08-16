package dev.vitor.petshop.attendance;

import java.math.BigDecimal;
import java.time.LocalDate; // Certifique-se de que esta é a importação correta


public class AttendanceRequest {
    private Long petId; // Identificador do pet
    private String descricao; // Descrição do atendimento
    private BigDecimal valor; // Valor do atendimento
    private LocalDate data; // Data do atendimento

    // Getters e Setters

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
