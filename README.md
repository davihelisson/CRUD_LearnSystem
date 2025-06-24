# Sistema de Gerenciamento EADMelhor 📚💻

Este é um sistema desktop desenvolvido em **Java Swing** para gerenciar alunos, cursos e matrículas no contexto de uma plataforma de ensino à distância. O sistema é voltado para instituições de ensino que desejam um controle eficaz e simples de operações acadêmicas básicas, como cadastro de cursos, gerenciamento de alunos e geração de relatórios administrativos.

## 🎯 Funcionalidades

- ✅ Cadastro, edição e exclusão de **cursos**
- ✅ Cadastro, edição e exclusão de **alunos**
- ✅ **Matrícula** e **remoção de matrícula** de alunos em cursos
- ✅ Visualização da **quantidade de alunos por curso**
- ✅ **Consulta de alunos ativos, inativos** e **matriculados por disciplina**
- ✅ Geração de **relatórios dinâmicos** diretamente pela interface:
  - Alunos Ativos
  - Alunos Inativos
  - Alunos por Curso
  - Cursos com mais de 5 alunos
  - Cursos com os respectivos alunos
- ✅ Exportação de relatórios em formato `.txt`
- ✅ Interface com **tema escuro (dark mode)** utilizando FlatLaf

## 🛠️ Tecnologias Utilizadas

- **Java 11+**
- **Swing (GUI)**
- **JDBC com MySQL**
- **FlatLaf** (tema escuro moderno para interfaces Swing)
- **Modelo DAO** para separação da lógica de persistência
- **Padrão MVC** aplicado parcialmente

## 📦 Estrutura de Pastas

```
src/
├── dao/                 # Acesso a dados (AlunoDAO, CursoDAO)
├── modelo/              # Classes modelo (Aluno, Curso)
├── gui/                 # Interfaces gráficas (Swing)
│   ├── TelaInicial.java
│   ├── GerenciarCursosFrame.java
│   ├── EditarCursoFrame.java
│   ├── EditarClasseFrame.java
│   ├── RelatoriosFrame.java
│   └── AlunosMatriculadosFrame.java
└── database/            # Script SQL para criação do banco
```

## 🧪 Pré-requisitos

- JDK 11 ou superior
- MySQL Server
- Um gerenciador de dependências (ou inclusão manual do `.jar` do FlatLaf)

## 🗂️ Script de Banco de Dados

O banco de dados pode ser criado usando o arquivo `database.sql`. Ele cria três tabelas principais:

- `alunos`
- `cursos`
- `curso_aluno` (tabela de junção para matrículas)

Com chaves primárias, relacionamentos e integridade referencial por meio de `FOREIGN KEY`.

## 🚀 Como Executar

1. Clone este repositório
2. Importe o projeto em sua IDE Java (NetBeans, Eclipse, IntelliJ, etc.)
3. Configure a conexão JDBC com seu banco MySQL
4. Execute a classe `TelaInicial.java`
5. Aproveite a experiência!

## 📌 Observações

- Os relatórios são exportados em `.txt` para garantir compatibilidade em qualquer sistema.
- O sistema utiliza **interface gráfica leve e responsiva**.
- Toda a lógica de matrícula e contagem de alunos é feita através do relacionamento entre `curso` e `aluno` via `curso_aluno`.

## 📈 Melhorias Futuras (Sugestões)

- Autenticação de usuário e controle de acesso
- Exportação para `.pdf` e `.xlsx`
- Dashboard com estatísticas em tempo real
- Integração com serviços web ou API REST
- Testes unitários e integração contínua

## 👨‍💻 Autor

Desenvolvido por [Seu Nome Aqui] como parte de um projeto de estudo e aprimoramento em Java, Orientação a Objetos e construção de aplicações GUI com Swing.

---

📝 Licença: Este projeto é livre para uso educacional e pessoal.