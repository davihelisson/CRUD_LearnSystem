package gui;

import dao.AlunoDAO;
import dao.CursoDAO;
import modelo.Aluno;
import modelo.Curso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EditarClasseFrame extends JFrame {
    private JComboBox<String> comboCursos;
    private JTable tabelaAlunosAtivos, tabelaAlunosMatriculados;
    private DefaultTableModel modeloAlunosAtivos, modeloAlunosMatriculados;
    private final CursoDAO cursoDAO = new CursoDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();
    
    
    private Curso cursoSelecionado;
    private OnCursoAtualizadoListener listener;

    public EditarClasseFrame() {
        setTitle("Editar Classe de Curso");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        comboCursos = new JComboBox<>();
        carregarCursos();
        comboCursos.addActionListener(e -> carregarDadosAlunos());
        
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTopo.add(new JLabel("Selecione o Curso: "));
        painelTopo.add(comboCursos);
        add(painelTopo, BorderLayout.NORTH);
        

        modeloAlunosAtivos = new DefaultTableModel(new Object[]{"Selecionar", "CPF", "Nome"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        modeloAlunosMatriculados = new DefaultTableModel(new Object[]{"Selecionar", "CPF", "Nome"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        tabelaAlunosAtivos = new JTable(modeloAlunosAtivos);
        tabelaAlunosMatriculados = new JTable(modeloAlunosMatriculados);


        JScrollPane scrollAtivos = new JScrollPane(tabelaAlunosAtivos);
        JPanel painelAtivos = new JPanel(new BorderLayout());
        painelAtivos.setBorder(BorderFactory.createTitledBorder("Alunos Ativos"));
        painelAtivos.add(scrollAtivos, BorderLayout.CENTER);


        JScrollPane scrollMatriculados = new JScrollPane(tabelaAlunosMatriculados);
        JPanel painelMatriculados = new JPanel(new BorderLayout());
        painelMatriculados.setBorder(BorderFactory.createTitledBorder("Alunos Matriculados"));
        painelMatriculados.add(scrollMatriculados, BorderLayout.CENTER);

        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelAtivos, painelMatriculados);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
        
        
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        
        JButton btnMatricular = new JButton("Matricular");
        JButton btnRemover = new JButton("Remover Matrícula");
        JButton btnSalvar = new JButton("Salvar Alterações");

        painelBotoes.add(btnMatricular);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnSalvar);

        add(painelBotoes, BorderLayout.SOUTH);
        
        btnMatricular.addActionListener(e -> matricularAlunos());

        btnRemover.addActionListener(e -> removerMatriculas());
        
        btnSalvar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Alterações salvas.");
            dispose();
        });
        
    }

    private void carregarCursos() {
        comboCursos.removeAllItems();
        List<Curso> cursos = cursoDAO.listarCursos();
        for (Curso curso : cursos) {
            comboCursos.addItem(curso.getNome());
        }
    }

    private void carregarAlunosAtivos() {
        modeloAlunosAtivos.setRowCount(0);
        List<Aluno> alunos = alunoDAO.listarAlunosAtivos();
        for (Aluno aluno : alunos) {
            modeloAlunosAtivos.addRow(new Object[]{false, aluno.getCpf(), aluno.getNome(), aluno.getEmail()});
        }
    }

    private void carregarAlunosMatriculados() {
        modeloAlunosMatriculados.setRowCount(0);
        if (cursoSelecionado != null) {
            List<Aluno> alunosMatriculados = cursoDAO.carregarAlunos(String.valueOf(cursoSelecionado.getCodigo()));
            for (Aluno aluno : alunosMatriculados) {
                modeloAlunosMatriculados.addRow(new Object[]{false, aluno.getCpf(), aluno.getNome(), aluno.getEmail()});
            }
        }
    }

    private void matricularAlunos() {
        if (cursoSelecionado == null) return;
        for (int i = 0; i < modeloAlunosAtivos.getRowCount(); i++) {
            boolean selecionado = (Boolean) modeloAlunosAtivos.getValueAt(i, 0);
            if (selecionado) {
                String cpf = (String) modeloAlunosAtivos.getValueAt(i, 1);
                cursoDAO.matricularAluno(cursoSelecionado.getCodigo(), cpf);
            }
            if (listener != null) {
                listener.onCursoAtualizado();
            }
        }
        carregarAlunosMatriculados();
    }

    private void removerMatriculas() {
        if (cursoSelecionado == null) return;
        for (int i = 0; i < modeloAlunosMatriculados.getRowCount(); i++) {
            boolean selecionado = (Boolean) modeloAlunosMatriculados.getValueAt(i, 0);
            if (selecionado) {
                String cpf = (String) modeloAlunosMatriculados.getValueAt(i, 1);
                cursoDAO.removerMatricula(cursoSelecionado.getCodigo(), cpf);
            }
            if (listener != null) {
                listener.onCursoAtualizado();
            }
        }
        carregarAlunosMatriculados();
    }

    private void carregarDadosAlunos() {
    	modeloAlunosAtivos.setRowCount(0);
        modeloAlunosMatriculados.setRowCount(0);

        String nomeCurso = (String) comboCursos.getSelectedItem();
        if (nomeCurso == null) return;

        cursoSelecionado = cursoDAO.buscarCursoPorNome(nomeCurso);
        if (cursoSelecionado == null) return;

        carregarAlunosAtivos();
        carregarAlunosMatriculados();
    }
    
    public void setOnCursoAtualizado(OnCursoAtualizadoListener listener) {
        this.listener = listener;
    }
}

