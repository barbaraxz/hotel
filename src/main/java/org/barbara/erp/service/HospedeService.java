package org.barbara.erp.service;

import org.barbara.erp.dao.HospedeDAO;
import org.barbara.erp.dao.HospedeDAOImpl;
import org.barbara.erp.model.Hospede;
import java.util.List;

public class HospedeService {
    private final HospedeDAO dao = new HospedeDAOImpl();

    public void salvar(Hospede hospede) {
        validarCamposObrigatorios(hospede);
        dao.salvar(hospede);
    }

    public void atualizar(Hospede hospede) {
        validarCamposObrigatorios(hospede);

        dao.atualizar(hospede);
    }

    public void remover(Long id) {
        dao.excluir(id);
    }

    public List<Hospede> listar() {
        return dao.listarTodos();
    }

    private void validarCamposObrigatorios(Hospede h) {
        if (h.getNome() == null || h.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do hóspede é obrigatório!");
        }
        if (h.getTelefone() == null || h.getTelefone().trim().isEmpty()) {
            throw new IllegalArgumentException("O telefone do hóspede é obrigatório!");
        }
        if (h.getEmail() == null || h.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail do hóspede é obrigatório!");
        }
    }
}