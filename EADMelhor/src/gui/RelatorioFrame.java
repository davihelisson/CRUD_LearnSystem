package gui;

import dao.CursoDAO;
import dao.AlunoDAO;
import modelo.Aluno;
import modelo.Curso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class RelatorioFrame extends JFrame {
    private JTable tabelaRelatorio;
    private DefaultTableModel modeloTabela;
    private final CursoDAO cursoDAO = new CursoDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();
    
    private List<Curso> cursosRelatorioAtual; 

    public RelatorioFrame() {
        setTitle("Relatórios do Sistema");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // ComboBox de opções
        String[] opcoes = {
        	    "Alunos Ativos",
        	    "Alunos Inativos",
        	    "Alunos por Curso",
        	    "Cursos com Mais de 5 Alunos",
        	    "Todos os Cursos com Alunos"
        	};
        JComboBox<String> comboOpcoes = new JComboBox<>(opcoes);
        JButton btnGerar = new JButton("Gerar Relatório");

        JPanel painelTopo = new JPanel();
        painelTopo.add(new JLabel("Tipo de Relatório: "));
        painelTopo.add(comboOpcoes);
        painelTopo.add(btnGerar);
        add(painelTopo, BorderLayout.NORTH);

        // Tabela
        modeloTabela = new DefaultTableModel();
        tabelaRelatorio = new JTable(modeloTabela);
        add(new JScrollPane(tabelaRelatorio), BorderLayout.CENTER);

        // Botão Exportar
        JButton btnExportar = new JButton("Exportar Relatório");
        JPanel painelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelInferior.add(btnExportar);
        add(painelInferior, BorderLayout.SOUTH);
        
        JButton btnVerAlunos = new JButton("Ver Alunos da Turma");
        btnVerAlunos.setEnabled(false);

        painelTopo.add(btnVerAlunos);

        // Ações
        btnGerar.addActionListener(e -> {
            String opcao = (String) comboOpcoes.getSelectedItem();
            btnVerAlunos.setEnabled("Alunos por Curso".equals(opcao));

            switch (opcao) {
            case "Alunos Ativos":
                gerarRelatorioAlunosAtivos();
                break;
            case "Alunos Inativos":
                gerarRelatorioAlunosInativos();
                break;
            case "Alunos por Curso":
                gerarRelatorioAlunosPorCurso();
                break;
            case "Cursos com Mais de 5 Alunos":
                gerarRelatorioCursosComMaisDeCinco();
                break;
            case "Todos os Cursos com Alunos":
                gerarRelatorioCursosComAlunos();
                break;
        }

        });
        
       
        
        btnVerAlunos.addActionListener(e -> {
            int selectedRow = tabelaRelatorio.getSelectedRow();
            if (selectedRow >= 0 && cursosRelatorioAtual != null) {
                Curso curso = cursosRelatorioAtual.get(selectedRow);
                new AlunosMatriculadosFrame(curso.getCodigo(), curso.getNome()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um curso na tabela para ver os alunos.");
            }
        });
        
        btnExportar.addActionListener(e -> exportarRelatorioParaTxt());


        // Clique duplo na tabela
        tabelaRelatorio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedRow = tabelaRelatorio.getSelectedRow();
                    String opcao = (String) comboOpcoes.getSelectedItem();

                    if ("Alunos por Curso".equals(opcao) && cursosRelatorioAtual != null && selectedRow >= 0) {
                        Curso curso = cursosRelatorioAtual.get(selectedRow);
                        new AlunosMatriculadosFrame(curso.getCodigo(), curso.getNome()).setVisible(true);
                    }
                }
            }
        });
    }

    private void gerarRelatorioAlunosAtivos() {
        modeloTabela.setColumnIdentifiers(new String[]{"CPF", "Nome", "Email", "Data de Nascimento"});
        modeloTabela.setRowCount(0);
        List<Aluno> alunos = alunoDAO.listarAlunosAtivos();
        for (Aluno a : alunos) {
            modeloTabela.addRow(new Object[]{a.getCpf(), a.getNome(), a.getEmail(), a.getDataNascimento()});
        }
    }

    private void gerarRelatorioAlunosPorCurso() {
        modeloTabela.setColumnIdentifiers(new String[]{"Curso", "Quantidade de Alunos"});
        modeloTabela.setRowCount(0);
        cursosRelatorioAtual = cursoDAO.listarCursos();

        for (Curso c : cursosRelatorioAtual) {
            int quantidade = cursoDAO.contarAlunosMatriculados(c.getCodigo());
            modeloTabela.addRow(new Object[]{c.getNome(), quantidade});
        }
    }


    private void gerarRelatorioCursosComMaisDeCinco() {
        modeloTabela.setColumnIdentifiers(new String[]{"Curso", "Quantidade de Alunos"});
        modeloTabela.setRowCount(0);
        List<Curso> cursos = cursoDAO.listarCursos();
        for (Curso c : cursos) {
            int quantidade = cursoDAO.contarAlunosMatriculados(c.getCodigo());
            if (quantidade > 5) {
                modeloTabela.addRow(new Object[]{c.getNome(), quantidade});
            }
        }
    }
    
    private void gerarRelatorioAlunosInativos() {
        modeloTabela.setColumnIdentifiers(new String[]{"CPF", "Nome", "Email", "Data de Nascimento"});
        modeloTabela.setRowCount(0);
        List<Aluno> alunos = alunoDAO.listarAlunosInativos(); // método deve existir no DAO
        for (Aluno a : alunos) {
            modeloTabela.addRow(new Object[]{
                a.getCpf(), a.getNome(), a.getEmail(), a.getDataNascimento()
            });
        }
    }

    private void gerarRelatorioCursosComAlunos() {
        modeloTabela.setColumnIdentifiers(new String[]{"Curso", "CPF", "Nome do Aluno", "Email"});
        modeloTabela.setRowCount(0);
        List<Curso> cursos = cursoDAO.listarCursos();
        for (Curso curso : cursos) {
            List<Aluno> alunos = cursoDAO.carregarAlunos(String.valueOf(curso.getCodigo()));
            for (Aluno aluno : alunos) {
                modeloTabela.addRow(new Object[]{
                    curso.getNome(), aluno.getCpf(), aluno.getNome(), aluno.getEmail()
                });
            }
        }
    }


    private void exportarRelatorioParaTxt() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório");
        fileChooser.setSelectedFile(new java.io.File("relatorio.txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();

            try (PrintWriter writer = new PrintWriter(fileToSave, "UTF-8")) {
                int colCount = modeloTabela.getColumnCount();
                int rowCount = modeloTabela.getRowCount();

                // Escrever cabeçalhos
                for (int i = 0; i < colCount; i++) {
                    writer.print(modeloTabela.getColumnName(i));
                    if (i < colCount - 1) writer.print("\t");
                }
                writer.println();

                // Escrever dados
                for (int row = 0; row < rowCount; row++) {
                    for (int col = 0; col < colCount; col++) {
                        Object value = modeloTabela.getValueAt(row, col);
                        writer.print(value != null ? value.toString() : "");
                        if (col < colCount - 1) writer.print("\t");
                    }
                    writer.println();
                }

                JOptionPane.showMessageDialog(this, "Relatório exportado com sucesso para: " + fileToSave.getAbsolutePath());

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao exportar o relatório.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
