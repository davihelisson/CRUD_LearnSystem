/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import modelo.Aluno;
import factory.ConnectionFactory;
import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fatec
 */
public class AlunoDAO {

    private Connection connection;

    public AlunoDAO() {
        this.connection = new ConnectionFactory().getConnection();
    }

    // Aqui vem a parte do CRUD
    // 1 - CREATE
    public void createAluno(Aluno aluno) {
        String sql = "INSERT INTO alunos (cpf, nome, email, data_nascimento, ativo) VALUES (?, ?, ?, ?, ?)";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, aluno.getCpf());
            stmt.setString(2, aluno.getNome());
            stmt.setString(3, aluno.getEmail());
            stmt.setDate(4, new java.sql.Date(aluno.getDataNascimento().getTime()));
            stmt.setBoolean(5, aluno.isAtivo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar aluno: " + e.getMessage(), e);
        }
    }

    // 2 - READ
    /* Pesquisar todos os alunos */
    public List<Aluno> readTodosAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM alunos";

        try (
                Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Aluno aluno = new Aluno(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getDate("data_nascimento").toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
                aluno.setAtivo(rs.getBoolean("ativo"));
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar alunos: " + e.getMessage(), e);
        }
        return alunos;
    }

    /* Busca por CPF exato */
    public List<Aluno> readAlunosPorCpf(String cpf) {
        String sql = "SELECT * FROM alunos WHERE cpf = ?";
        List<Aluno> lista = new ArrayList<>();
        try (
                PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, cpf);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearAluno(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na consulta por CPF: " + e.getMessage(), e);
        }
        return lista;
    }

    /* Busca por parte do nome (LIKE) */
    public List<Aluno> readAlunosPorNome(String nomeParcial) {
        String sql = "SELECT * FROM alunos WHERE nome LIKE ?";
        List<Aluno> lista = new ArrayList<>();
        try (
                PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, "%" + nomeParcial + "%");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearAluno(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro na consulta por Nome: " + e.getMessage(), e);
        }
        return lista;
    }

    // 3 - UPDATE
    public void updateAluno(Aluno aluno) {
        String sql = "UPDATE alunos SET nome = ?, email = ?, data_nascimento = ?, ativo = ? WHERE cpf = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getEmail());
            stmt.setDate(3, new java.sql.Date(aluno.getDataNascimento().getTime()));
            stmt.setBoolean(4, aluno.isAtivo());
            stmt.setString(5, aluno.getCpf());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar aluno: " + e.getMessage(), e);
        }
    }

    // 4 - DELETE
    public void deleteAluno(String cpf) {
        String sql = "DELETE FROM alunos WHERE cpf = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir aluno: " + e.getMessage(), e);
        }
    }

    // 5 - HABILITAR / DESABILITAR
    public void disableAluno(String cpf) {
        String sql = "UPDATE alunos SET ativo = false WHERE cpf = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desabilitar aluno: " + e.getMessage(), e);
        }
    }

    public void enableAluno(String cpf) {
        String sql = "UPDATE alunos SET ativo = true WHERE cpf = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao reabilitar aluno: " + e.getMessage(), e);
        }
    }

    /* ---------- utilitário interno (já usado pelos outros métodos) ---------- */
    private Aluno mapearAluno(ResultSet rs) throws SQLException {
        Aluno al = new Aluno(
                rs.getString("cpf"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getDate("data_nascimento")
                        .toLocalDate()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
        al.setAtivo(rs.getBoolean("ativo"));
        return al;
    }

    private void carregarDados(boolean filtroAtivo) {
        // Lê os alunos do banco e atualiza a tabela
    }

}
