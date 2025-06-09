CREATE DATABASE EADMelhor;

USE EADMelhor;

CREATE TABLE alunos (
    cpf VARCHAR(11) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    ativo BOOLEAN NOT NULL
);


