package org.barbara.erp.model;

public class Quartos {
    private Long id;
    private String quarto; // Ex: "205"
    private Double valor;  // Valor da diária
    private String tipo;   // "Simples", "Duplo" ou "Luxo"
    private Boolean status; // true = disponível, false = indisponível

    public Quartos() {
    }

    public Quartos(Long id, String quarto, Double valor, String tipo, Boolean status) {
        this.id = id;
        this.quarto = quarto;
        this.valor = valor;
        this.tipo = tipo;
        this.status = status;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuarto() { return quarto; }
    public void setQuarto(String quarto) { this.quarto = quarto; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    @Override
    public String toString() {
        return "Quarto: " + this.getQuarto();
    }
}