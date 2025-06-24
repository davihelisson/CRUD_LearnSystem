CREATE DATABASE EADMelhor;

USE EADMelhor;

DROP TABLE IF EXISTS alunos;

CREATE TABLE alunos (
    cpf VARCHAR(11) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    data_nascimento DATE NOT NULL,
    ativo BOOLEAN NOT NULL
);

DROP TABLE IF EXISTS cursos;

CREATE TABLE cursos (
    codigo INT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    carga_horaria INT NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE curso_aluno (
    codigo_curso INT NOT NULL,
    cpf_aluno VARCHAR(11) NOT NULL,
    PRIMARY KEY (codigo_curso, cpf_aluno),
    FOREIGN KEY (codigo_curso) REFERENCES cursos(codigo) ON DELETE CASCADE,
    FOREIGN KEY (cpf_aluno) REFERENCES alunos(cpf) ON DELETE CASCADE
);

SHOW TABLES;

DESCRIBE alunos;
DESCRIBE cursos;
DESCRIBE curso_aluno;