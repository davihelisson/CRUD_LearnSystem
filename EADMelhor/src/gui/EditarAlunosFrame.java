/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import dao.AlunoDAO;
import modelo.Aluno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
/**
 *
 * @author Fatec
 */
public class EditarAlunosFrame extends javax.swing.JFrame {

    private JTextField txtCpf, txtNome;
    private JTable tabela;
    
    public EditarAlunosFrame() {
        setTitle("Editar Alunos");
        setSize(750, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        /* ---------- PAINEL SUPERIOR (campos + botões) ---------- */
        JPanel painelSup = new JPanel(new BorderLayout(10, 10));
        painelSup.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        // --- painel dos campos (esquerda)
        JPanel pCampos = new JPanel();
        pCampos.setLayout(new BoxLayout(pCampos, BoxLayout.Y_AXIS));

        pCampos.add(criarLinhaCampo("CPF:", txtCpf = new JTextField(15)));
        pCampos.add(criarLinhaCampo("Nome:", txtNome = new JTextField(15)));

        // --- painel dos botões (direita)
        JPanel pBotoes = new JPanel(new GridLayout(3, 1, 8, 8));
        JButton btnPesquisar = new JButton("Pesquisar");
        JButton btnEditar    = new JButton("Editar");
        JButton btnVoltar    = new JButton("Voltar");
        pBotoes.add(btnPesquisar);
        pBotoes.add(btnEditar);
        pBotoes.add(btnVoltar);

        painelSup.add(pCampos, BorderLayout.CENTER);
        painelSup.add(pBotoes, BorderLayout.EAST);
        add(painelSup, BorderLayout.NORTH);

        /* ---------- TABELA ---------- */
        tabela = new JTable();
        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        /* ---------- AÇÕES ---------- */
        btnPesquisar.addActionListener(e -> carregarDados(true));
        btnVoltar   .addActionListener(e -> dispose());

        // clique-duplo na linha
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && tabela.getSelectedRow() != -1) {
                    abrirDialogEdicao();
                }
            }
        });

        // botão Editar
        btnEditar.addActionListener(e -> {
            if (tabela.getSelectedRow() != -1) abrirDialogEdicao();
            else JOptionPane.showMessageDialog(
                    EditarAlunosFrame.this, "Selecione um aluno na tabela.");
        });

        carregarDados(false); // lista completa ao abrir
    }

    /* ---------- util ---------- */
    private JPanel criarLinhaCampo(String rotulo, JTextField campo) {
        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lbl = new JLabel(rotulo);
        lbl.setPreferredSize(new Dimension(60, 25));
        linha.add(lbl);
        linha.add(campo);
        return linha;
    }

    protected void carregarDados(boolean filtrar) {
        String cpf  = txtCpf.getText().trim();
        String nome = txtNome.getText().trim();

        AlunoDAO dao = new AlunoDAO();
        List<Aluno> lista;
        if (filtrar && !cpf.isEmpty())       lista = dao.readAlunosPorCpf(cpf);
        else if (filtrar && !nome.isEmpty()) lista = dao.readAlunosPorNome(nome);
        else                                 lista = dao.readTodosAlunos();

        atualizarTabela(lista);
    }

    private void atualizarTabela(List<Aluno> alunos) {
        String[] colunas = { "CPF", "Nome", "Email",
                             "Data Nasc.", "Ativo" };
        DefaultTableModel m = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        alunos.forEach(a -> m.addRow(new Object[]{
            a.getCpf(), a.getNome(), a.getEmail(),
            sdf.format(a.getDataNascimento()),
            a.isAtivo() ? "Sim" : "Não"
        }));
        tabela.setModel(m);
    }

    private void abrirDialogEdicao() {
        int row = tabela.getSelectedRow();
        if (row == -1) return;

        String cpf = tabela.getValueAt(row, 0).toString();
        String nome = tabela.getValueAt(row, 1).toString();
        String email = tabela.getValueAt(row, 2).toString();
        String data = tabela.getValueAt(row, 3).toString();
        boolean ativo = tabela.getValueAt(row, 4).toString().equals("Sim");

        Aluno aluno = new Aluno(cpf, nome, email, data);
        aluno.setAtivo(ativo);

        /* Abre diálogo modal; se atualizar, recarrega lista */
        EditarAlunoDialog dlg = new EditarAlunoDialog(this, aluno);
        dlg.setVisible(true);
        if (dlg.isAtualizado()) carregarDados(false);
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
            java.util.logging.Logger.getLogger(EditarAlunosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditarAlunosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditarAlunosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditarAlunosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditarAlunosFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
