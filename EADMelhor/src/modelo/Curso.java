/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;


public class Curso {   
    private int codigo;
    private String nome;
    private int cargaHoraria;
    private String status;
    private int quantidadeAlunos;
    
    
    public Curso(int codigo, String nome, int cargaHoraria, String status, int quatidadeAlunos) {
    	this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
        this.status = status;
        this.quantidadeAlunos = quantidadeAlunos;
    }
    
    public Curso() {
        this.status = "Ativo";
        this.quantidadeAlunos = 0;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
    	this.status = status;
    }

    public boolean isAtivo() {
        return "Ativo".equalsIgnoreCase(status);
    }

    public void setAtivo(boolean ativo) {
        this.status = ativo ? "Ativo" : "Inativo";
    }
    
    public int getQuantidadeAlunos() {
        return quantidadeAlunos;
    }

    public void setQuantidadeAlunos(int quantidadeAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
    }

    @Override
    public String toString() {
        return String.format("Curso: %s (Código: %d), %d horas - %s", nome, codigo, cargaHoraria, status, quantidadeAlunos);
    }
    
}
