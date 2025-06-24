/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import factory.ConnectionFactory;
import java.sql.Connection;

import modelo.Aluno;
import modelo.Curso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CursoDAO {
    private Connection connection;
    
    public CursoDAO(){
        this.connection = new ConnectionFactory().getConnection();
    }
    
    public void adicionarCurso(Curso curso) {
        String sql = "INSERT INTO cursos (codigo, nome, carga_horaria, status, quantidade_alunos) VALUES (?, ?, ?, ?, ?)";
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {

        	stmt.setInt(1, curso.getCodigo());
            stmt.setString(2, curso.getNome());
            stmt.setInt(3, curso.getCargaHoraria());
            
            String status = curso.getStatus();
            if (status == null || status.isBlank()) {
                status = "Ativo";
            }
            
            stmt.setString(4, curso.getStatus());
            stmt.setInt(5, 0);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Curso> listarCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = """
                SELECT 
				    c.codigo,
				    c.nome,
				    c.carga_horaria,
				    c.status,
				    COUNT(ca.cpf_aluno) AS quantidade_alunos
				FROM cursos c
				LEFT JOIN curso_aluno ca ON ca.codigo_curso = c.codigo
				GROUP BY c.codigo, c.nome, c.carga_horaria, c.status

            """;

        try (
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

        	while (rs.next()) {
                Curso curso = new Curso(
                    rs.getInt("codigo"),
                    rs.getString("nome"),
                    rs.getInt("carga_horaria"),
                    rs.getString("status"),
                    rs.getInt("quantidade_alunos")
                );
                cursos.add(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cursos;
    }

    public boolean atualizarCurso(Curso curso) {
        String sql = "UPDATE cursos SET nome=?, carga_horaria=?, status=? WHERE codigo=?";
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {

        	stmt.setString(1, curso.getNome());
            stmt.setInt(2, curso.getCargaHoraria());
            stmt.setString(3, curso.getStatus());
            stmt.setInt(4, curso.getCodigo());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluirCurso(int codigo) {
        String sql = "DELETE FROM cursos WHERE codigo = ?";
        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean existeCursoPorCodigo(int codigo) {
        String sql = "SELECT 1 FROM cursos WHERE codigo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeCursoPorNome(String nome) {
        String sql = "SELECT 1 FROM cursos WHERE LOWER(nome) = LOWER(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
                        rs.getString("status"),
                        rs.getInt("quantidade_alunos")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Curso buscarCursoPorNome(String nome) {
        String sql = "SELECT * FROM cursos WHERE LOWER(nome) = LOWER(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Curso(
                    rs.getInt("codigo"),
                    rs.getString("nome"),
                    rs.getInt("carga_horaria"),
                    rs.getString("status"),
                    rs.getInt("quantidade_alunos")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Aluno> carregarAlunos(String codigoCurso) {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT a.* FROM alunos a " +
                     "JOIN curso_aluno ca ON a.cpf = ca.cpf_aluno " +
                     "WHERE ca.codigo_curso = ?";

        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigoCurso);
            ResultSet rs = stmt.executeQuery();
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
            e.printStackTrace();
        }

        return alunos;
    }
    
    public void matricularAluno(int codigoCurso, String cpfAluno) {
        String sql = "INSERT IGNORE INTO curso_aluno (codigo_curso, cpf_aluno) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigoCurso);
            stmt.setString(2, cpfAluno);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    public void removerMatricula(int codigoCurso, String cpfAluno) {
        String sql = "DELETE FROM curso_aluno WHERE codigo_curso = ? AND cpf_aluno = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigoCurso);
            stmt.setString(2, cpfAluno);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int contarAlunosMatriculados(int codigoCurso) {
        String sql = "SELECT COUNT(*) FROM curso_aluno WHERE codigo_curso = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigoCurso);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0; // Se ocorrer erro, retorna 0 por padrão
    }


}
