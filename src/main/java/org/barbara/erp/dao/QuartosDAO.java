package org.barbara.erp.dao;

import org.barbara.erp.model.Quartos;
import java.util.List;

public interface QuartosDAO {
    void salvar(Quartos quarto);
    void atualizar(Quartos quarto);
    void excluir(Long id);
    Quartos buscarPorId(Long id);
    List<Quartos> listarTodos();
}