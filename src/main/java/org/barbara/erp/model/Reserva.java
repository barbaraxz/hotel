package org.barbara.erp.model;

import java.time.LocalDate;

public class Reserva {
    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Hospede hospede;
    private Quartos quarto;
    private Double valorTotal;

    // Construtor vazio
    public Reserva() {
    }

    // Construtor completo na ordem exata que o banco de dados devolve os dados
    public Reserva(Long id, LocalDate checkIn, LocalDate checkOut, Hospede hospede, Quartos quarto, Double valorTotal) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.hospede = hospede;
        this.quarto = quarto;
        this.valorTotal = valorTotal;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    public LocalDate getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

    public Hospede getHospede() { return hospede; }
    public void setHospede(Hospede hospede) { this.hospede = hospede; }

    public Quartos getQuarto() { return quarto; }
    public void setQuarto(Quartos quarto) { this.quarto = quarto; }

    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
}