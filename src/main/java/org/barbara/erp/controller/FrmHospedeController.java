package org.barbara.erp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.barbara.erp.dao.HospedeDAO;
import org.barbara.erp.dao.HospedeDAOImpl;
import org.barbara.erp.model.Hospede;

import java.util.List;

public class FrmHospedeController {

    @FXML
    private TextField txtPesquisa; // Campo de busca no topo

    @FXML
    private TextField txtNome;     // Campos do formulário lateral
    @FXML
    private TextField txtCpf;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtEmail;

    @FXML
    private TableView<Hospede> tabelaHospedes; // Tabela central
    @FXML
    private TableColumn<Hospede, Long> colId;
    @FXML
    private TableColumn<Hospede, String> colNome;
    @FXML
    private TableColumn<Hospede, String> colCpf;
    @FXML
    private TableColumn<Hospede, String> colTelefone;
    @FXML
    private TableColumn<Hospede, String> colEmail;

    // Gerenciadores de dados
    private final HospedeDAO hospedeDAO = new HospedeDAOImpl();
    private ObservableList<Hospede> obsHospedes;
    private Hospede hospedeSelecionado;

    @FXML
    public void initialize() {
        // Vincula as colunas da TableView aos atributos da classe Hospede
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Carrega os dados do banco para preencher a tabela
        atualizarTabela();

        // Escuta cliques nas linhas da tabela para jogar os dados de volta nos campos
        tabelaHospedes.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                hospedeSelecionado = novo;
                preencherCampos(novo);
            }
        });
    }

    @FXML
    private void buscarHospedes() {
        String termo = txtPesquisa.getText();

        // Se o campo estiver vazio, traz todo mundo. Se não, busca pelo termo digitado
        if (termo == null || termo.trim().isEmpty()) {
            obsHospedes = FXCollections.observableArrayList(hospedeDAO.listarTodos());
        } else {
            // Tenta buscar por CPF primeiro; se não achar nada, traz a listagem normal ou usa como filtro
            Hospede porCpf = hospedeDAO.buscarPorCpf(termo.trim());
            if (porCpf != null) {
                obsHospedes = FXCollections.observableArrayList(porCpf);
            } else {
                // Filtro local simples por Nome caso queira pesquisar na lista carregada
                List<Hospede> todos = hospedeDAO.listarTodos();
                List<Hospede> filtrados = todos.stream()
                        .filter(h -> h.getNome().toLowerCase().contains(termo.toLowerCase()))
                        .toList();
                obsHospedes = FXCollections.observableArrayList(filtrados);
            }
        }
        tabelaHospedes.setItems(obsHospedes);
    }

    @FXML
    private void salvarHospede() {
        if (txtNome.getText().isEmpty() || txtCpf.getText().isEmpty()) {
            exibirAlerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Nome e CPF são obrigatórios!");
            return;
        }

        if (hospedeSelecionado == null) {
            // Cenário A: Criar um novo cadastro
            Hospede novo = new Hospede();
            novo.setNome(txtNome.getText());
            novo.setCpf(txtCpf.getText());
            novo.setTelefone(txtTelefone.getText());
            novo.setEmail(txtEmail.getText());

            hospedeDAO.salvar(novo);
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Hóspede cadastrado com sucesso!");
        } else {
            // Cenário B: Atualizar um registro já selecionado
            hospedeSelecionado.setNome(txtNome.getText());
            hospedeSelecionado.setCpf(txtCpf.getText());
            hospedeSelecionado.setTelefone(txtTelefone.getText());
            hospedeSelecionado.setEmail(txtEmail.getText());

            hospedeDAO.atualizar(hospedeSelecionado);
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Dados do hóspede atualizados!");
        }

        limparCampos();
        atualizarTabela();
    }

    @FXML
    private void excluirHospede() {
        if (hospedeSelecionado == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Seleção Necessária", "Selecione um hóspede na tabela para poder excluir.");
            return;
        }

        // Alerta de confirmação para evitar cliques acidentais
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente remover este hóspede?", ButtonType.YES, ButtonType.NO);
        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.YES) {
                hospedeDAO.excluir(hospedeSelecionado.getId());
                exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Hóspede removido com sucesso!");
                limparCampos();
                atualizarTabela();
            }
        });
    }

    @FXML
    private void limparCampos() {
        txtNome.clear();
        txtCpf.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtPesquisa.clear();
        hospedeSelecionado = null;
        tabelaHospedes.getSelectionModel().clearSelection();
    }

    private void atualizarTabela() {
        obsHospedes = FXCollections.observableArrayList(hospedeDAO.listarTodos());
        tabelaHospedes.setItems(obsHospedes);
    }

    private void preencherCampos(Hospede h) {
        txtNome.setText(h.getNome());
        txtCpf.setText(h.getCpf());
        txtTelefone.setText(h.getTelefone());
        txtEmail.setText(h.getEmail());
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}