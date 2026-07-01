package org.barbara.erp.dao;

import org.barbara.erp.model.Hospede;
import org.barbara.erp.model.Quartos;
import org.barbara.erp.model.Reserva;
import org.barbara.erp.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAOImpl implements ReservaDAO {

    @Override
    public void salvar(Reserva reserva) {
        // Ajustado para checkin e checkout sem underline
        String sql = "INSERT INTO reservas (check_in, check_out, hospede_id, quarto_id, valor_total) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(reserva.getCheckIn()));
            stmt.setDate(2, Date.valueOf(reserva.getCheckOut()));
            stmt.setLong(3, reserva.getHospede().getId());
            stmt.setLong(4, reserva.getQuarto().getId());
            stmt.setDouble(5, reserva.getValorTotal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao registrar reserva: " + e.getMessage(), e);
        }
    }

    @Override
    public void excluir(Long id) {
        String sql = "DELETE FROM reservas WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cancelar reserva: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reserva> listarTodas() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.id, r.check_in, r.check_out, r.valor_total, " +
                "h.id AS h_id, h.nome, h.telefone, h.email, h.cpf, " +
                "q.id AS q_id, q.quarto, q.valor, q.tipo, q.status " +
                "FROM reservas r " +
                "JOIN hospedes h ON r.hospede_id = h.id " +
                "JOIN quartos q ON r.quarto_id = q.id " +
                "ORDER BY r.check_in DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Hospede h = new Hospede(rs.getLong("h_id"), rs.getString("nome"), rs.getString("telefone"), rs.getString("email"), rs.getString("cpf"));
                Quartos q = new Quartos(rs.getLong("q_id"), rs.getString("quarto"), rs.getDouble("valor"), rs.getString("tipo"), rs.getBoolean("status"));

                Reserva r = new Reserva(
                        rs.getLong("id"),
                        rs.getDate("check_in").toLocalDate(),
                        rs.getDate("check_out").toLocalDate(),
                        h, q,
                        rs.getDouble("valor_total")
                );
                lista.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar reservas: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<Reserva> buscarPorNomeHospede(String nome) {
        List<Reserva> lista = new ArrayList<>();
        // Ajustado de check_in/check_out para checkin/checkout
        String sql = "SELECT r.id, r.check_in, r.check_out, r.valor_total, " +
                "h.id AS h_id, h.nome, h.telefone, h.email, h.cpf, " +
                "q.id AS q_id, q.quarto, q.valor, q.tipo, q.status " +
                "FROM reservas r " +
                "JOIN hospedes h ON r.hospede_id = h.id " +
                "JOIN quartos q ON r.quarto_id = q.id " +
                "WHERE h.nome ILIKE ? " +
                "ORDER BY r.checkin DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Hospede h = new Hospede(rs.getLong("h_id"), rs.getString("nome"), rs.getString("telefone"), rs.getString("email"), rs.getString("cpf"));
                    Quartos q = new Quartos(rs.getLong("q_id"), rs.getString("quarto"), rs.getDouble("valor"), rs.getString("tipo"), rs.getBoolean("status"));

                    Reserva r = new Reserva(
                            rs.getLong("id"),
                            rs.getDate("r.check_in").toLocalDate(), // Ajustado aqui também
                            rs.getDate("r.check_out").toLocalDate(), // Ajustado aqui também
                            h, q,
                            rs.getDouble("valor_total")
                    );
                    lista.add(r);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reserva: " + e.getMessage(), e);
        }
        return lista;
    }
}