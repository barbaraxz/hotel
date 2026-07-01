package org.barbara.erp.dao;

import org.barbara.erp.model.Hospede;
import org.barbara.erp.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospedeDAOImpl implements HospedeDAO {

    @Override
    public void salvar(Hospede hospede) {
        String sql = "INSERT INTO hospedes (nome, telefone, email, cpf) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hospede.getNome());
            stmt.setString(2, hospede.getTelefone());
            stmt.setString(3, hospede.getEmail());
            stmt.setString(4, hospede.getCpf());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar hóspede: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizar(Hospede hospede) {
        String sql = "UPDATE hospedes SET nome = ?, telefone = ?, email = ?, cpf = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, hospede.getNome());
            stmt.setString(2, hospede.getTelefone());
            stmt.setString(3, hospede.getEmail());
            stmt.setString(4, hospede.getCpf());
            stmt.setLong(5, hospede.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar hóspede: " + e.getMessage(), e);
        }
    }

    @Override
    public void excluir(Long id) {
        String sql = "DELETE FROM hospedes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir hóspede: " + e.getMessage(), e);
        }
    }

    @Override
    public Hospede buscarPorId(Long id) {
        String sql = "SELECT * FROM hospedes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Hospede(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("telefone"),
                            rs.getString("email"),
                            rs.getString("cpf")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar hóspede por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Hospede buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM hospedes WHERE cpf = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Hospede(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("telefone"),
                            rs.getString("email"),
                            rs.getString("cpf")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar hóspede por CPF: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Hospede> listarTodos() {
        List<Hospede> lista = new ArrayList<>();
        String sql = "SELECT * FROM hospedes ORDER BY nome";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Hospede h = new Hospede(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("cpf")
                );
                lista.add(h);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar hóspedes: " + e.getMessage(), e);
        }
        return lista;
    }
}