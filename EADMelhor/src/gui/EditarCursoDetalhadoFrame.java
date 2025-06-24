package gui;

import dao.CursoDAO;
import modelo.Curso;

import javax.swing.*;
import java.awt.*;

public class EditarCursoDetalhadoFrame extends JFrame {
    private JTextField txtCodigo, txtNome, txtCargaHoraria;
    private JCheckBox chkAtivo, chkInativo;
    private final Curso curso;
    private final CursoDAO cursoDAO = new CursoDAO();

    private Runnable onCursoAlterado;
    
    public EditarCursoDetalhadoFrame(Curso curso) {
        this.curso = curso;
        setTitle("Detalhes do Curso");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }
    
    public void setOnCursoAlterado(Runnable onCursoAlterado) {
        this.onCursoAlterado = onCursoAlterado;
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCodigo = new JLabel("Código:");
        JLabel lblNome = new JLabel("Nome:");
        JLabel lblCarga = new JLabel("Carga Horária:");

        txtCodigo = new JTextField(String.valueOf(curso.getCodigo()), 10);
        txtCodigo.setEditable(false);
        txtNome = new JTextField(curso.getNome(), 20);
        txtCargaHoraria = new JTextField(String.valueOf(curso.getCargaHoraria()), 10);

        chkAtivo = new JCheckBox("Ativo");
        chkInativo = new JCheckBox("Inativo");

        if (curso.isAtivo()) {
            chkAtivo.setSelected(true);
        } else {
            chkInativo.setSelected(true);
        }

        chkAtivo.addActionListener(e -> {
            if (chkAtivo.isSelected()) chkInativo.setSelected(false);
        });

        chkInativo.addActionListener(e -> {
            if (chkInativo.isSelected()) chkAtivo.setSelected(false);
        });

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnExcluir = new JButton("Excluir Curso");

        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnCancelar.addActionListener(e -> dispose());
        btnExcluir.addActionListener(e -> excluirCurso());

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblCodigo, gbc);
        gbc.gridx = 1;
        formPanel.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblNome, gbc);
        gbc.gridx = 1;
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblCarga, gbc);
        gbc.gridx = 1;
        formPanel.add(txtCargaHoraria, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(chkAtivo, gbc);
        gbc.gridx = 1;
        formPanel.add(chkInativo, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(btnSalvar, gbc);
        gbc.gridx = 1;
        formPanel.add(btnCancelar, gbc);
        gbc.gridx = 2;
        formPanel.add(btnExcluir, gbc);

        add(formPanel);
    }

    private void salvarAlteracoes() {
        String nome = txtNome.getText().trim();
        String cargaStr = txtCargaHoraria.getText().trim();

        if (nome.length() < 3) {
            JOptionPane.showMessageDialog(this, "O nome deve ter pelo menos 3 caracteres.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int carga;
        try {
            carga = Integer.parseInt(cargaStr);
            if (carga < 20) {
                JOptionPane.showMessageDialog(this, "Carga horária deve ser no mínimo 20.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Carga horária inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        curso.setNome(nome);
        curso.setCargaHoraria(carga);
        curso.setStatus(chkAtivo.isSelected() ? "Ativo" : "Inativo");

        if (cursoDAO.atualizarCurso(curso)) {
            JOptionPane.showMessageDialog(this, "Alterações salvas com sucesso!");

            // ✅ Notifica que o curso foi alterado
            if (onCursoAlterado != null) {
                onCursoAlterado.run();
            }

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar alterações.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void excluirCurso() {
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este curso?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (cursoDAO.excluirCurso(curso.getCodigo())) {
                JOptionPane.showMessageDialog(this, "Curso excluído com sucesso.");
                if (onCursoAlterado != null) {
                    onCursoAlterado.run(); // Notifica que houve mudança
                }
                dispose();
                
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir curso.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
} 


