package org.barbara.erp.service;

import org.barbara.erp.dao.ReservaDAO;
import org.barbara.erp.dao.ReservaDAOImpl;
import org.barbara.erp.model.Reserva;

import java.util.List;

public class ReservaService {

    private final ReservaDAO reservaDAO = new ReservaDAOImpl();

    public void salvarReserva(Reserva reserva) {
        // Regra de negócio simples: valida se a data de check-out não é anterior ao check-in
        if (reserva.getCheckOut().isBefore(reserva.getCheckIn())) {
            throw new IllegalArgumentException("A data de Check-out não pode ser anterior à data de Check-in!");
        }
        reservaDAO.salvar(reserva);
    }

    public void finalizarOuExcluirReserva(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID da reserva inválido.");
        }
        reservaDAO.excluir(id);
    }

    public List<Reserva> listarTodas() {
        return reservaDAO.listarTodas();
    }

    public List<Reserva> buscarPorNomeHospede(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return reservaDAO.listarTodas();
        }
        return reservaDAO.buscarPorNomeHospede(nome);
    }
}