package org.barbara.erp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.barbara.erp.model.Quartos;
import org.barbara.erp.service.QuartosService;
import javafx.fxml.FXML;

public class FrmQuartosController {
    @FXML
    private TextField txtQuartos;
    @FXML
    private TextField txtValor;
    @FXML
    private ComboBox<String> cbxTipo;
    @FXML
    private ComboBox<String> cbxStatus;
    @FXML
    private TableView<Quartos> tabela;
    @FXML
    private TableColumn<Quartos, Long> colId;

    @FXML
    private TableColumn<Quartos, String> colQuarto; // ✨ CORRIGIDO: Removido o "s" para bater com o fx:id do FXML

    @FXML
    private TableColumn<Quartos, Double> colValor;
    @FXML
    private TableColumn<Quartos, String> colTipo;
    @FXML
    private TableColumn<Quartos, Boolean> colStatus;

    private final QuartosService service = new QuartosService();
    private final ObservableList<Quartos> lista = FXCollections.observableArrayList();
    private Quartos quartoSelecionado;

    @FXML
    public void initialize() {
        cbxTipo.setItems(FXCollections.observableArrayList("Simples", "Duplo", "Luxo"));
        cbxStatus.setItems(FXCollections.observableArrayList("Disponível", "Ocupado"));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        colQuarto.setCellValueFactory(new PropertyValueFactory<>("quarto")); // ✨ CORRIGIDO: Variável alterada para colQuarto

        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colStatus.setCellFactory(column -> new TableCell<Quartos, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Disponível" : "Ocupado");
                }
            }
        });

        carregarTabela();

        tabela.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, novo) -> {
                    if (novo != null) {
                        quartoSelecionado = novo;
                        txtQuartos.setText(novo.getQuarto());
                        txtValor.setText(String.valueOf(novo.getValor()));
                        cbxTipo.setValue(novo.getTipo());
                        cbxStatus.setValue(novo.getStatus() ? "Disponível" : "Ocupado");
                    }
                });
    }

    private void carregarTabela() {
        lista.setAll(service.listarTodos());
        tabela.setItems(lista);
    }

    @FXML
    public void salvar() {
        try {
            Quartos quartos = new Quartos();
            quartos.setQuarto(txtQuartos.getText());
            quartos.setValor(Double.valueOf(txtValor.getText()));
            quartos.setTipo(cbxTipo.getValue());

            boolean disponivel = "Disponível".equals(cbxStatus.getValue());
            quartos.setStatus(disponivel);

            service.salvarQuarto(quartos);

            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Quarto saved successfully!");
            carregarTabela();
            limparCampos();
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "O valor da diária inserido é inválido.");
        } catch (IllegalArgumentException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Regra de Negócio", e.getMessage());
        }
    }

    @FXML
    public void atualizar() {
        if (quartoSelecionado == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Aviso", "Selecione um quarto na tabela para atualizar.");
            return;
        }
        try {
            quartoSelecionado.setQuarto(txtQuartos.getText());
            quartoSelecionado.setValor(Double.valueOf(txtValor.getText()));
            quartoSelecionado.setTipo(cbxTipo.getValue());

            boolean disponivel = "Disponível".equals(cbxStatus.getValue());
            quartoSelecionado.setStatus(disponivel);

            service.atualizarQuarto(quartoSelecionado);

            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Quarto atualizado com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (NumberFormatException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Validação", "O valor da diária inserido é inválido.");
        } catch (IllegalArgumentException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Regra de Negócio", e.getMessage());
        }
    }

    @FXML
    public void remover() {
        if (quartoSelecionado == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Aviso", "Selecione um quarto na tabela para remover.");
            return;
        }
        try {
            service.excluirQuarto(quartoSelecionado.getId());
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Quarto removido com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (RuntimeException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro no Banco", "Não foi possível remover o quarto. Verifique se existem reservas associadas.");
        }
    }

    private void limparCampos() {
        txtQuartos.clear();
        txtValor.clear();
        cbxTipo.setValue(null);
        cbxStatus.setValue(null);
        quartoSelecionado = null;
        tabela.getSelectionModel().clearSelection();
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}