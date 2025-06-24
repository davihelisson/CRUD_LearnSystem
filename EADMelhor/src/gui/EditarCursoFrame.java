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

public class EditarCursoFrame extends JFrame {

    private JTextField txtCodigo, txtNome;
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
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        modeloTabela = new DefaultTableModel(new Object[]{"Código", "Nome", "Carga Horária", "Qtd. Alunos", "Status"}, 0);
        tabelaCursos = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaCursos);

        JPanel painelPesquisa = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCodigo = new JLabel("Código:");
        JLabel lblNome = new JLabel("Nome:");
        txtCodigo = new JTextField(10);
        txtNome = new JTextField(20);
        JButton btnPesquisar = new JButton("Pesquisar");
        JButton btnEditarCurso = new JButton("Editar Curso");
        JButton btnVoltar = new JButton("Voltar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        painelPesquisa.add(lblCodigo, gbc);
        gbc.gridx = 1;
        painelPesquisa.add(txtCodigo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelPesquisa.add(lblNome, gbc);
        gbc.gridx = 1;
        painelPesquisa.add(txtNome, gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        painelPesquisa.add(btnPesquisar, gbc);
        gbc.gridy = 1;
        painelPesquisa.add(btnEditarCurso, gbc);
        gbc.gridy = 2;
        painelPesquisa.add(btnVoltar, gbc);

        add(painelPesquisa, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        btnPesquisar.addActionListener(e -> pesquisarCursos());
        
        btnEditarCurso.addActionListener(e -> {
            int selectedRow = tabelaCursos.getSelectedRow();
            if (selectedRow >= 0) {
                int codigo = (int) modeloTabela.getValueAt(selectedRow, 0);
                Curso curso = cursoDAO.buscarCursoPorCodigo(String.valueOf(codigo));
                if (curso != null) {
                	EditarCursoDetalhadoFrame detalhadoFrame = new EditarCursoDetalhadoFrame(curso);
                	detalhadoFrame.setOnCursoAlterado(() -> carregarCursos()); // <-- aqui é o pulo do gato
                	detalhadoFrame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um curso para editar.");
            }
        });
        
        
        
        btnVoltar.addActionListener(e -> dispose());

        tabelaCursos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tabelaCursos.getSelectedRow();
                if (selectedRow >= 0) {
                    String codigoStr = modeloTabela.getValueAt(selectedRow, 0).toString();
                    List<Curso> cursos = cursoDAO.listarCursos();

                }
            }
        });
    }

    private void carregarCursos() {
        modeloTabela.setRowCount(0);
        List<Curso> cursos = cursoDAO.listarCursos();
        for (Curso c : cursos) {
            modeloTabela.addRow(new Object[]{
                    c.getCodigo(),
                    c.getNome(),
                    c.getCargaHoraria(),
                    c.getQuantidadeAlunos(),
                    c.getStatus()
            });
        }
    }

    private void pesquisarCursos() {
        modeloTabela.setRowCount(0);
        String codigo = txtCodigo.getText().trim();
        String nome = txtNome.getText().trim().toLowerCase();
        List<Curso> cursos = cursoDAO.listarCursos();

        for (Curso c : cursos) {
            boolean codigoMatch = codigo.isEmpty() || String.valueOf(c.getCodigo()).contains(codigo);
            boolean nomeMatch = nome.isEmpty() || c.getNome().toLowerCase().contains(nome);

            if (codigoMatch && nomeMatch) {
                modeloTabela.addRow(new Object[]{
                        c.getCodigo(),
                        c.getNome(),
                        c.getCargaHoraria(),
                        c.getQuantidadeAlunos(),
                        c.isAtivo() ? "Ativo" : "Inativo"
                });
            }
        }
    }

    private void abrirEdicaoCurso() {
        int selectedRow = tabelaCursos.getSelectedRow();
        if (selectedRow >= 0) {
            String codigoStr = modeloTabela.getValueAt(selectedRow, 0).toString();
            Curso curso = cursoDAO.buscarCursoPorCodigo(codigoStr);
            if (curso != null) {
                new EditarCursoDetalhadoFrame(curso).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um curso para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void abrirEdicaoClasse() {
        int selectedRow = tabelaCursos.getSelectedRow();
        if (selectedRow >= 0) {
            String codigoStr = modeloTabela.getValueAt(selectedRow, 0).toString();
            Curso curso = cursoDAO.buscarCursoPorCodigo(codigoStr);
            if (curso != null) {
                new EditarClasseFrame().setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um curso para editar a classe.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    

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
