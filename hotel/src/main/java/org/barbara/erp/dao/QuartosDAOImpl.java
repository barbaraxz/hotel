package org.barbara.erp.dao;

import org.barbara.erp.model.Quartos;
import org.barbara.erp.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuartosDAOImpl implements QuartosDAO {

    @Override
    public void salvar(Quartos quarto) {
        String sql = "INSERT INTO quartos (quarto, valor, tipo, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, quarto.getQuarto());
            stmt.setDouble(2, quarto.getValor());
            stmt.setString(3, quarto.getTipo());
            stmt.setBoolean(4, quarto.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar quarto: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Quartos quarto) {
        String sql = "UPDATE quartos SET quarto = ?, valor = ?, tipo = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, quarto.getQuarto());
            stmt.setDouble(2, quarto.getValor());
            stmt.setString(3, quarto.getTipo());
            stmt.setBoolean(4, quarto.getStatus());
            stmt.setLong(5, quarto.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar quarto: " + e.getMessage(), e);
        }
    }

    @Override
    public void excluir(Long id) {
        String sql = "DELETE FROM quartos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir quarto: " + e.getMessage(), e);
        }
    }

    @Override
    public Quartos buscarPorId(Long id) {
        String sql = "SELECT * FROM quartos WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Quartos(
                            rs.getLong("id"),
                            rs.getString("quarto"),
                            rs.getDouble("valor"),
                            rs.getString("tipo"),
                            rs.getBoolean("status")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar quarto: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Quartos> listarTodos() {
        List<Quartos> lista = new ArrayList<>();
        String sql = "SELECT * FROM quartos ORDER BY quarto";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Quartos q = new Quartos(
                        rs.getLong("id"),
                        rs.getString("quarto"),
                        rs.getDouble("valor"),
                        rs.getString("tipo"),
                        rs.getBoolean("status")
                );
                lista.add(q);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar quartos: " + e.getMessage(), e);
        }
        return lista;
    }
}