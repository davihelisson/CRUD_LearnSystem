package gui;

import dao.AlunoDAO;
import modelo.Aluno;
import modelo.Curso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class ClasseAlunosFrame extends JFrame {

    private Curso curso;
    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabela;
    private final AlunoDAO alunoDAO = new AlunoDAO();

    public ClasseAlunosFrame(Curso curso) {
        this.curso = curso;

        setTitle("Classe de Alunos - " + curso.getNome());
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        //carregarAlunos();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        modeloTabela = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Email", "Data Matrícula", "Status"}, 0
        );
        tabelaAlunos = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaAlunos);
        add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar Novo Aluno");
        JButton btnRemover = new JButton("Remover Aluno");
        JButton btnVoltar = new JButton("Voltar");

        btnAdicionar.addActionListener(e -> adicionarAluno());
        //btnRemover.addActionListener(e -> removerAluno());
        btnVoltar.addActionListener(e -> dispose());

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnVoltar);
        add(painelBotoes, BorderLayout.SOUTH);
    }
/*
    private void carregarAlunos() {
        modeloTabela.setRowCount(0);
        List<Aluno> alunos = alunoDAO.listarPorCurso(curso.getCodigo());
        for (Aluno a : alunos) {
            modeloTabela.addRow(new Object[]{
                a.getId(),
                a.getNome(),
                a.getEmail(),
                a.getDataMatricula(),
                a.isAtivo() ? "Ativo" : "Inativo"
            });
        }
    }
*/
    private void adicionarAluno() {
        JOptionPane.showMessageDialog(this, "Abrir tela de adicionar aluno ao curso \"" + curso.getNome() + "\"");
        // Você pode abrir outro frame de cadastro aqui
    }
    
/*
    private void removerAluno() {
        int linhaSelecionada = tabelaAlunos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int idAluno = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja remover este aluno?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                if (alunoDAO.remover(idAluno)) {
                    JOptionPane.showMessageDialog(this, "Aluno removido com sucesso.");
                    carregarAlunos();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao remover aluno.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para remover.");
        }
    }*/
}
