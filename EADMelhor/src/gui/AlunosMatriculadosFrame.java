package gui;

import dao.CursoDAO;
import modelo.Aluno;
import modelo.Curso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AlunosMatriculadosFrame extends JFrame {
    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabela;
    private final CursoDAO cursoDAO = new CursoDAO();

    public AlunosMatriculadosFrame(int codigoCurso, String nomeCurso) {
        setTitle("Alunos Matriculados - " + nomeCurso);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
        carregarAlunosMatriculados(codigoCurso);
    }


	private void initComponents() {
        setLayout(new BorderLayout());

        modeloTabela = new DefaultTableModel(new Object[]{"CPF", "Nome", "Email", "Data de Nascimento"}, 0);
        tabelaAlunos = new JTable(modeloTabela);
        add(new JScrollPane(tabelaAlunos), BorderLayout.CENTER);

        JButton btnExportar = new JButton("Exportar");
        btnExportar.addActionListener(e -> exportarParaTxt());
        
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelInferior.add(btnExportar);
        add(painelInferior, BorderLayout.SOUTH);
    }

    private void carregarAlunosMatriculados(int codigoCurso) {
        modeloTabela.setRowCount(0);
        List<Aluno> alunos = cursoDAO.carregarAlunos(String.valueOf(codigoCurso));
        for (Aluno a : alunos) {
            modeloTabela.addRow(new Object[]{
                    a.getCpf(), a.getNome(), a.getEmail(), a.getDataNascimento()
            });
        }
    }

    private void exportarParaTxt() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório de Alunos");
        fileChooser.setSelectedFile(new File("alunos_matriculados.txt"));

        int escolha = fileChooser.showSaveDialog(this);
        if (escolha == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(arquivo, "UTF-8")) {
                // Cabeçalhos
                for (int i = 0; i < modeloTabela.getColumnCount(); i++) {
                    writer.print(modeloTabela.getColumnName(i));
                    if (i < modeloTabela.getColumnCount() - 1) writer.print("\t");
                }
                writer.println();

                // Dados
                for (int row = 0; row < modeloTabela.getRowCount(); row++) {
                    for (int col = 0; col < modeloTabela.getColumnCount(); col++) {
                        writer.print(modeloTabela.getValueAt(row, col));
                        if (col < modeloTabela.getColumnCount() - 1) writer.print("\t");
                    }
                    writer.println();
                }

                JOptionPane.showMessageDialog(this, "Arquivo salvo com sucesso:\n" + arquivo.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao exportar o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}

