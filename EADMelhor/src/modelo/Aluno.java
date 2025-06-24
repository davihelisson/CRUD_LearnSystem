/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 *
 * @author Fatec
 */
public class Aluno {
    private String cpf;
    private String nome;
    private String email;
    private Date dataNascimento;
    private boolean ativo;
    
    private static final SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
    private static final Pattern padraoEmail = Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$");

    public Aluno(String cpf, String nome, String email, String dataNascimento) throws IllegalArgumentException {
        setCpf(cpf);
        setNome(nome);
        setEmail(email);
        setDataNascimento(dataNascimento);
        this.ativo = true; // Ativo por padrão
    }
    
    // Atributos do CPF
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 dígitos numéricos.");
        }
        this.cpf = cpf;
    }
    
    //Atributos do Nome
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new IllegalArgumentException("Nome deve conter no mínimo 3 caracteres.");
        }
        this.nome = nome.trim();
    }
    
    //Atributos do E-mail
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !padraoEmail.matcher(email).matches()) {
            throw new IllegalArgumentException("Email em formato inválido.");
        }
        this.email = email.trim();
    }
    
    //Data de Nascimento no formato dd/mm/aaaa
    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(java.sql.Date date) {
    	if (date != null) {
            this.dataNascimento = new Date(date.getTime());
        } else {
            throw new IllegalArgumentException("Data de nascimento não pode ser nula.");
        }
    }
    
    public void setDataNascimento(String data) {
        try {
            formatoData.setLenient(false);
            this.dataNascimento = formatoData.parse(data);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Data de nascimento deve estar no formato dd/MM/yyyy.");
        }
    }
    
    
    // A parte de habilitar/desabilitar aluno
    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void desabilitar() {
        this.ativo = false;
    }

    public void reabilitar() {
        this.ativo = true;
    }

    @Override
    public String toString() {
        return String.format("CPF: %s, Nome: %s, Email: %s, Data Nasc.: %s, Ativo: %s",
                cpf, nome, email, formatoData.format(dataNascimento), ativo ? "Sim" : "Não");
    }
}
