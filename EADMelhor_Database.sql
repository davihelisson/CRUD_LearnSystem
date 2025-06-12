CREATE DATABASE EADMelhor;

USE EADMelhor;

CREATE TABLE alunos (
    cpf VARCHAR(11) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    ativo BOOLEAN NOT NULL
);

CREATE TABLE Cursos (
    Codigo INT PRIMARY KEY,
    Nome VARCHAR(255) NOT NULL,
    CargaHoraria INT NOT NULL,
    Status VARCHAR(50) NOT NULL, 
    QuantidadeAlunos INT NOT NULL
);



