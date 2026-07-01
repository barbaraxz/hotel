package org.barbara.erp.dao;

import org.barbara.erp.model.Hospede;
import java.util.List;

public interface HospedeDAO {
    void salvar(Hospede hospede);
    void atualizar(Hospede hospede);
    void excluir(Long id);
    Hospede buscarPorId(Long id);
    Hospede buscarPorCpf(String cpf); // Adicionado para validações com o CPF de volta
    List<Hospede> listarTodos();
}