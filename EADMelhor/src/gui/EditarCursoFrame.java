/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import dao.CursoDAO;
import modelo.Curso;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.lang.String;

public class EditarCursoFrame extends javax.swing.JFrame {

    private JTextField txtCodigo, txtNome, txtCargaHoraria;
    private JCheckBox chkAtivo, chkInativo;
    private JTable tabelaCursos;
    private DefaultTableModel modeloTabela;
    private Curso cursoSelecionado;
    
    private final CursoDAO cursoDAO = new CursoDAO();
    

    public EditarCursoFrame() {
        setTitle("Editar Curso");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        carregarCursos();

        setLayout(new BorderLayout());
        
        modeloTabela = new DefaultTableModel(new Object[]{"Código", "Nome", "Carga Horária", "Qtd. Alunos", "Status"}, 0);

        // Painel superior para edição
        JPanel painelEdicao = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCodigo = new JLabel("Código:");
        JLabel lblNome = new JLabel("Nome:");
        JLabel lblCargaHoraria = new JLabel("Carga Horária:");

        txtCodigo = new JTextField(10);
        txtCodigo.setEditable(false);
        txtNome = new JTextField(20);
        txtCargaHoraria = new JTextField(10);

        chkAtivo = new JCheckBox("Ativo");
        chkInativo = new JCheckBox("Inativo");

        // Garantir apenas um checkbox selecionado
        chkAtivo.addActionListener(e -> {
            if (chkAtivo.isSelected()) {
                chkInativo.setSelected(false);
            }
        });
        chkInativo.addActionListener(e -> {
            if (chkInativo.isSelected()) {
                chkAtivo.setSelected(false);
            }
        });

        JButton btnSalvar = new JButton("Salvar Alterações");
        JButton btnEditarClasse = new JButton("Editar Classe");
        JButton btnVoltar = new JButton("Voltar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelEdicao.add(lblCodigo, gbc);
        gbc.gridx = 1;
        painelEdicao.add(txtCodigo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelEdicao.add(lblNome, gbc);
        gbc.gridx = 1;
        painelEdicao.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelEdicao.add(lblCargaHoraria, gbc);
        gbc.gridx = 1;
        painelEdicao.add(txtCargaHoraria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        painelEdicao.add(chkAtivo, gbc);
        gbc.gridx = 1;
        painelEdicao.add(chkInativo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        painelEdicao.add(btnSalvar, gbc);
        gbc.gridx = 1;
        painelEdicao.add(btnEditarClasse, gbc);
        gbc.gridx = 2;
        painelEdicao.add(btnVoltar, gbc);

        add(painelEdicao, BorderLayout.NORTH);

        // Painel inferior com tabela
        modeloTabela = new DefaultTableModel(new Object[]{"Código", "Nome", "Carga Horária", "Qtd. Alunos", "Status"}, 0);
        tabelaCursos = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaCursos);
        add(scrollPane, BorderLayout.CENTER);

        tabelaCursos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tabelaCursos.getSelectedRow();
                if (selectedRow >= 0) {
                    Object codigoObj = modeloTabela.getValueAt(selectedRow, 1);
                    if (codigoObj != null) {
                        String codigoStr = codigoObj.toString();
                        
                        CursoDAO cursoDAO = new CursoDAO();
                        cursoSelecionado = cursoDAO.buscarCursoPorCodigo(codigoStr);
                        preencherCamposCurso(cursoSelecionado);
                    } else {
                        JOptionPane.showMessageDialog(EditarCursoFrame.this, "Código do curso não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnSalvar.addActionListener(e -> salvarAlteracoes());
        btnEditarClasse.addActionListener(e -> editarClasse());
        btnVoltar.addActionListener(e -> dispose());

    }

    private void carregarCursos() {
        modeloTabela.setRowCount(0);
        CursoDAO cursoDAO = new CursoDAO();
        List<Curso> cursos = cursoDAO.listarCursos();
        for (Curso c : cursos) {
            modeloTabela.addRow(new Object[]{
                c.getCodigo(),
                c.getNome(),
                c.getCargaHoraria(),
                "-", // aqui entraria quantidade de alunos (ajustar depois)
                c.isAtivo() ? "Ativo" : "Inativo"
            });
        }
    }

    private void preencherCamposCurso(Curso curso) {
        if (curso == null) {
            return;
        }
        txtCodigo.setText(String.valueOf(curso.getCodigo()));
        txtNome.setText(curso.getNome());
        txtCargaHoraria.setText(String.valueOf(curso.getCargaHoraria()));
        chkAtivo.setSelected(curso.isAtivo());
        chkInativo.setSelected(!curso.isAtivo());
    }

    private void salvarAlteracoes() {
        if (cursoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um curso para editar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nome = txtNome.getText().trim();
        String cargaStr = txtCargaHoraria.getText().trim();

        if (nome.length() < 3) {
            JOptionPane.showMessageDialog(this, "O nome deve ter ao menos 3 caracteres.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int carga;
        try {
            carga = Integer.parseInt(cargaStr);
            if (carga < 20) {
                JOptionPane.showMessageDialog(this, "A carga horária deve ser no mínimo 20.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Carga horária inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cursoSelecionado.setNome(nome);
        cursoSelecionado.setCargaHoraria(carga);
        cursoSelecionado.setAtivo(chkAtivo.isSelected());
        
        
         if (cursoDAO.atualizarCurso(cursoSelecionado)) {
             JOptionPane.showMessageDialog(this, "Erro ao salvar as alterações.", "Erro", JOptionPane.ERROR_MESSAGE);
         } else {
             JOptionPane.showMessageDialog(this, "Alterações foram salvas com sucesso!");
             carregarCursos();
         }
    }

    private void editarClasse() {
        if (cursoSelecionado != null) {
            JOptionPane.showMessageDialog(this, "Abrir edição da classe de alunos do curso: " + cursoSelecionado.getNome());
            // abrir nova tela com alunos do cursoSelecionado
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EditarCursoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditarCursoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditarCursoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditarCursoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditarCursoFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
