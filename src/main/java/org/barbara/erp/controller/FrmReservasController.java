package org.barbara.erp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.barbara.erp.model.Hospede;
import org.barbara.erp.model.Quartos;
import org.barbara.erp.model.Reserva;
import org.barbara.erp.service.HospedeService;
import org.barbara.erp.service.QuartosService;
import org.barbara.erp.service.ReservaService;

import java.time.LocalDate;

public class FrmReservasController {

    @FXML
    private TextField txtPesquisa;
    @FXML
    private ComboBox<Hospede> cbxHospede;
    @FXML
    private ComboBox<Quartos> cbxQuarto;
    @FXML
    private DatePicker dtCheckIn;
    @FXML
    private DatePicker dtCheckOut;

    // Tabela e Colunas
    @FXML
    private TableView<Reserva> tabela;
    @FXML
    private TableColumn<Reserva, Long> colId;
    @FXML
    private TableColumn<Reserva, String> colHospede;
    @FXML
    private TableColumn<Reserva, String> colQuarto;
    @FXML
    private TableColumn<Reserva, LocalDate> colCheckIn;
    @FXML
    private TableColumn<Reserva, LocalDate> colCheckOut;
    @FXML
    private TableColumn<Reserva, Double> colValor;

    // Serviços
    private final ReservaService reservaService = new ReservaService();
    private final HospedeService hospedeService = new HospedeService();
    private final QuartosService quartosService = new QuartosService();

    private final ObservableList<Reserva> listaReservas = FXCollections.observableArrayList();
    private Reserva reservaSelecionada;

    @FXML
    public void initialize() {
        // 1. Vincula as colunas simples às propriedades da model
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        colCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        // 2. Vincula com segurança (protegido contra valores nulos vindos do banco)
        colHospede.setCellValueFactory(cellData -> {
            if (cellData.getValue() != null && cellData.getValue().getHospede() != null) {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHospede().getNome());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        colQuarto.setCellValueFactory(cellData -> {
            if (cellData.getValue() != null && cellData.getValue().getQuarto() != null) {
                // Ajustado para ler o método correto do modelo Quartos
                return new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getQuarto().getQuarto()));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        // 3. Carrega os dados nos ComboBoxes e na Tabela com segurança
        try {
            carregarCombos();
            carregarTabela();
        } catch (Exception e) {
            System.err.println("Aviso: Falha ao carregar dados do Banco de Dados. Verifique a conexão.");
            e.printStackTrace();
        }

        // 4. Listener para quando clicar em uma linha da tabela
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, old, novo) -> {
            if (novo != null) {
                reservaSelecionada = novo;
                cbxHospede.setValue(novo.getHospede());
                cbxQuarto.setValue(novo.getQuarto());
                dtCheckIn.setValue(novo.getCheckIn());
                dtCheckOut.setValue(novo.getCheckOut());
            }
        });
    }

    private void carregarCombos() {
        try {
            cbxHospede.setItems(FXCollections.observableArrayList(hospedeService.listar()));
            cbxQuarto.setItems(FXCollections.observableArrayList(quartosService.listarTodos()));
        } catch (Exception e) {
            System.err.println("Erro ao buscar listas para os ComboBoxes: " + e.getMessage());
        }
    }

    private void carregarTabela() {
        try {
            listaReservas.setAll(reservaService.listarTodas());
            tabela.setItems(listaReservas);
        } catch (Exception e) {
            System.err.println("Erro ao listar dados na tabela de Reservas: " + e.getMessage());
        }
    }

    @FXML
    public void buscarReservas() {
        String busca = txtPesquisa.getText();
        if (busca == null || busca.trim().isEmpty()) {
            carregarTabela();
        } else {
            try {
                listaReservas.setAll(reservaService.buscarPorNomeHospede(busca));
                tabela.setItems(listaReservas);
            } catch (Exception e) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro na Busca", "Falha ao realizar pesquisa.");
            }
        }
    }

    @FXML
    public void salvar() {
        try {
            if (cbxHospede.getValue() == null || cbxQuarto.getValue() == null ||
                    dtCheckIn.getValue() == null || dtCheckOut.getValue() == null) {
                exibirAlerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Por favor, preencha todos os campos.");
                return;
            }

            Reserva novaReserva = new Reserva();
            novaReserva.setHospede(cbxHospede.getValue());
            novaReserva.setQuarto(cbxQuarto.getValue());
            novaReserva.setCheckIn(dtCheckIn.getValue());
            novaReserva.setCheckOut(dtCheckOut.getValue());


            // 1. Pega as datas selecionadas na tela
            LocalDate checkIn = dtCheckIn.getValue();
            LocalDate checkOut = dtCheckOut.getValue();
            // 2. Calcula a quantidade de dias (diárias) entre as duas datas
            long diarias = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);

            // Segurança: Se a pessoa colocar o mesmo dia ou checkout antes do checkin, conta como 1 diária
            if (diarias <= 0) {
                diarias = 1;
            }
            // 3. Pega o quarto selecionado para descobrir o preço dele
            Quartos quartoSelecionado = cbxQuarto.getValue();
            double precoDiaria = quartoSelecionado.getValor();

            // 4. Multiplica os dias pelo preço real do quarto e salva na reserva!
            double valorCalculado = diarias * precoDiaria;
            novaReserva.setValorTotal(valorCalculado);

            reservaService.salvarReserva(novaReserva);

            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Check-in realizado com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (IllegalArgumentException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    public void remover() {
        if (reservaSelecionada == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Aviso", "Selecione uma reserva na tabela para realizar o Check-out.");
            return;
        }
        try {
            reservaService.finalizarOuExcluirReserva(reservaSelecionada.getId());
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Check-out concluído com sucesso!");
            carregarTabela();
            limparCampos();
        } catch (RuntimeException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro no Banco", e.getMessage());
        }
    }

    @FXML
    public void limparCampos() {
        cbxHospede.setValue(null);
        cbxQuarto.setValue(null);
        dtCheckIn.setValue(null);
        dtCheckOut.setValue(null);
        txtPesquisa.clear();
        reservaSelecionada = null;
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