/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import dao.CursoDAO;
import modelo.Curso;

import javax.swing.*;
import java.awt.*;

public class CadastrarCursoFrame extends JFrame {
    private JTextField txtCodigo, txtNome, txtCargaHoraria;
    private JButton btnSalvar, btnCancelar;

    private final CursoDAO cursoDAO = new CursoDAO();

    public CadastrarCursoFrame() {
        setTitle("Cadastrar Curso");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCodigo = new JLabel("Código:");
        JLabel lblNome = new JLabel("Nome:");
        JLabel lblCargaHoraria = new JLabel("Carga Horária:");

        txtCodigo = new JTextField(10);
        txtNome = new JTextField(20);
        txtCargaHoraria = new JTextField(10);

        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");

        gbc.gridx = 0;
        gbc.gridy = 0;
        painel.add(lblCodigo, gbc);
        gbc.gridx = 1;
        painel.add(txtCodigo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(lblNome, gbc);
        gbc.gridx = 1;
        painel.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painel.add(lblCargaHoraria, gbc);
        gbc.gridx = 1;
        painel.add(txtCargaHoraria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        painel.add(btnSalvar, gbc);
        gbc.gridx = 1;
        painel.add(btnCancelar, gbc);

        add(painel);

        btnSalvar.addActionListener(e -> salvarCurso());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void salvarCurso() {
        String codigoStr = txtCodigo.getText().trim();
        String nome = txtNome.getText().trim();
        String cargaStr = txtCargaHoraria.getText().trim();

        if (codigoStr.isEmpty() || nome.isEmpty() || cargaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nome.length() < 3) {
            JOptionPane.showMessageDialog(this, "O nome deve ter no mínimo 3 caracteres.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int codigo;
        int cargaHoraria;
        try {
            codigo = Integer.parseInt(codigoStr);
            cargaHoraria = Integer.parseInt(cargaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Código e Carga Horária devem ser numéricos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cargaHoraria < 20) {
            JOptionPane.showMessageDialog(this, "A carga horária deve ser no mínimo 20.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cursoDAO.existeCursoPorCodigo(codigo)) {
            JOptionPane.showMessageDialog(this, "Já existe um curso com este código.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cursoDAO.existeCursoPorNome(nome)) {
            JOptionPane.showMessageDialog(this, "Já existe um curso com este nome.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Curso novoCurso = new Curso(); // status será "Ativo" por padrão
        cursoDAO.adicionarCurso(novoCurso);

        JOptionPane.showMessageDialog(this, "Curso cadastrado com sucesso.");
        dispose();
    }    

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
            java.util.logging.Logger.getLogger(CadastrarCursoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadastrarCursoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadastrarCursoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadastrarCursoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CadastrarCursoFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
