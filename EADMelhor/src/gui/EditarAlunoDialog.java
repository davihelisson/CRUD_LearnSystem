/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import dao.AlunoDAO;
import modelo.Aluno;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Fatec
 */
public class EditarAlunoDialog extends JDialog {

    private final JTextField txtNome  = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextField txtData  = new JTextField(10);
    private final JCheckBox chkAtivo     = new JCheckBox("Ativo");
    private final JCheckBox chkInativo   = new JCheckBox("Inativo");
    private boolean atualizado = false;
    private final Aluno aluno;
    
    
    public EditarAlunoDialog(Frame owner, Aluno aluno) {
        super(owner, "Editar Aluno", true);
        this.aluno = aluno;

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridLayout(6, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        // CPF somente leitura
        form.add(new JLabel("CPF:"));
        JTextField txtCpf = new JTextField(aluno.getCpf());
        txtCpf.setEditable(false);
        form.add(txtCpf);

        form.add(new JLabel("Nome:"));
        txtNome.setText(aluno.getNome());
        form.add(txtNome);

        form.add(new JLabel("Email:"));
        txtEmail.setText(aluno.getEmail());
        form.add(txtEmail);

        form.add(new JLabel("Data Nasc. (dd/MM/yyyy):"));
        txtData.setText(new java.text.SimpleDateFormat("dd/MM/yyyy")
                .format(aluno.getDataNascimento()));
        form.add(txtData);

        form.add(new JLabel("Status:"));
        JPanel pStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkAtivo.setSelected(aluno.isAtivo());
        chkInativo.setSelected(!aluno.isAtivo());
        pStatus.add(chkAtivo);
        pStatus.add(chkInativo);
        form.add(pStatus);

        // ---------- Check-boxes mutuamente exclusivos ----------
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

        /* ---------- Botões ---------- */
        JPanel pBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnExcluir = new JButton("Excluir Aluno");
        pBotoes.add(btnSalvar);
        pBotoes.add(btnCancelar);
        pBotoes.add(btnExcluir);

        add(form, BorderLayout.CENTER);
        add(pBotoes, BorderLayout.SOUTH);

        // ---------- Ações ----------
        btnSalvar.addActionListener(e -> {
            try {
                aluno.setNome(txtNome.getText().trim());
                aluno.setEmail(txtEmail.getText().trim());
                aluno.setDataNascimento(txtData.getText().trim());
                aluno.setAtivo(chkAtivo.isSelected());

                new AlunoDAO().updateAluno(aluno);
                JOptionPane.showMessageDialog(this, "Dados atualizados!");
                atualizado = true;
                dispose();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Erro na Atualização", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancelar.addActionListener(e -> dispose());
        
        btnExcluir.addActionListener(e -> {
            int opcao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o aluno com CPF " + aluno.getCpf() + "?",
                    "Confirmação", JOptionPane.YES_NO_OPTION);

            if (opcao == JOptionPane.YES_OPTION) {
                try {
                    AlunoDAO dao = new AlunoDAO();
                    dao.deleteAluno(aluno.getCpf());
                    JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso.");
                    dispose(); // fecha a janela de edição
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
                }
            }
        });
    }

    /**
     * @return true se o registro foi salvo
     */
    public boolean isAtualizado() {
        return atualizado;
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
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
