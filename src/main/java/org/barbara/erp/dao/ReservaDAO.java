package org.barbara.erp.dao;

import org.barbara.erp.model.Reserva;
import java.util.List;

public interface ReservaDAO {
    void salvar(Reserva reserva);
    void excluir(Long id);
    List<Reserva> listarTodas();
    List<Reserva> buscarPorNomeHospede(String nome);
}