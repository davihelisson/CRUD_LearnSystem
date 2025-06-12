/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import factory.ConnectionFactory;
import java.sql.Connection;
import modelo.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fatec
 */
public class CursoDAO {
    private Connection connection;
    
    public CursoDAO(){
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public void adicionarCurso(Curso curso) {
        String sql = "INSERT INTO cursos (codigo, nome, carga_horaria, ativo) VALUES (?, ?, ?, ?)";
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, curso.getCodigo());
            stmt.setString(2, curso.getNome());
            stmt.setInt(3, curso.getCargaHoraria());
            stmt.setBoolean(4, curso.isAtivo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Curso> listarCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT * FROM cursos";

        try (
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Curso curso = new Curso();
                curso.setCodigo(rs.getInt("codigo"));
                curso.setNome(rs.getString("nome"));
                curso.setCargaHoraria(rs.getInt("carga_horaria"));
                curso.setAtivo(rs.getBoolean("ativo"));
                cursos.add(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursos;
    }

    public boolean atualizarCurso(Curso curso) {
        String sql = "UPDATE cursos SET nome=?, carga_horaria=?, ativo=? WHERE codigo=?";
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, curso.getNome());
            stmt.setInt(2, curso.getCargaHoraria());
            stmt.setBoolean(3, curso.isAtivo());
            stmt.setInt(4, curso.getCodigo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void excluirCurso(int codigo) {
        String sql = "DELETE FROM cursos WHERE codigo=?";
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Curso buscarCursoPorCodigo(String codigo) {
        String sql = "SELECT * FROM cursos WHERE codigo=?";
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(codigo.trim()));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Curso(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getInt("carga_horaria"),
                        rs.getBoolean("ativo")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
}
