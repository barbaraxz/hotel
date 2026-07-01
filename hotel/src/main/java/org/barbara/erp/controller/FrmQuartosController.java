package org.barbara.erp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.barbara.erp.model.Quartos;
import org.barbara.erp.service.QuartosService; // Import atualizado conforme a estrutura nova
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
    private TableColumn<Quartos, String> colQuartos;
    @FXML
    private TableColumn<Quartos, Double> colValor;
    @FXML
    private TableColumn<Quartos, String> colTipo;
    @FXML
    private TableColumn<Quartos, Boolean> colStatus; // Tipo alterado para mapear Boolean da model

    private final QuartosService service = new QuartosService();

    private final ObservableList<Quartos> lista = FXCollections.observableArrayList();
    private Quartos quartoSelecionado;

    @FXML
    public void initialize() {
        // Inicializa as opções dos ComboBoxes
        cbxTipo.setItems(FXCollections.observableArrayList("Simples", "Duplo", "Luxo"));
        cbxStatus.setItems(FXCollections.observableArrayList("Disponível", "Ocupado"));

        // Vincula as colunas aos atributos da classe Quartos
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colQuartos.setCellValueFactory(new PropertyValueFactory<>("quarto")); // Corrigido para "quarto"
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Customiza a exibição da coluna Status na TableView de forma amigável
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

        // Ouvinte para quando o usuário clicar em uma linha da tabela
        tabela.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, novo) -> {
                    if (novo != null) {
                        quartoSelecionado = novo;
                        txtQuartos.setText(novo.getQuarto());
                        txtValor.setText(String.valueOf(novo.getValor())); // Corrigido para pegar o valor diário
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

            // Converte a seleção de String para o Boolean esperado pelo BD
            boolean disponivel = "Disponível".equals(cbxStatus.getValue());
            quartos.setStatus(disponivel);

            service.salvarQuarto(quartos);

            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Quarto salvo com sucesso!");
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