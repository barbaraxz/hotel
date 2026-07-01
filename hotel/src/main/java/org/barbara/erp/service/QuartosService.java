package org.barbara.erp.service;

import org.barbara.erp.dao.QuartosDAO;
import org.barbara.erp.dao.QuartosDAOImpl;
import org.barbara.erp.model.Quartos;
import java.util.List;

public class QuartosService {
    private final QuartosDAO quartosDAO = new QuartosDAOImpl();

    public void salvarQuarto(Quartos quarto) {
        // Regra de negócio: Impossível o cadastro sem valor da diária ou identificação
        if (quarto.getQuarto() == null || quarto.getQuarto().trim().isEmpty()) {
            throw new IllegalArgumentException("A identificação do quarto é obrigatória!");
        }
        if (quarto.getValor() == null || quarto.getValor() <= 0) {
            throw new IllegalArgumentException("O valor da diária deve ser maior que zero!");
        }
        quartosDAO.salvar(quarto);
    }

    public List<Quartos> listarTodos() {
        return quartosDAO.listarTodos();
    }

    public void excluirQuarto(Long id) {
        quartosDAO.excluir(id);
    }

    public void atualizarQuarto(Quartos quarto) {
        quartosDAO.atualizar(quarto);
    }
}