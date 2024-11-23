# Sistema de Gestão Empresarial

Um sistema completo para a gestão de clientes, fornecedores, agendamentos, vendas e produtos, com interface gráfica em Java (JFRAME) e integração com banco de dados.

## 📋 Funcionalidades

- **Cadastro e Consulta**:
  - Clientes
  - Fornecedores
  - Agendamentos
  - Produtos
  - Vendas
  
- **Interface Gráfica**:
  - Layout amigável e intuitivo.
  - Tela de login com validação de usuário e senha.
  - Botões e campos intuitivos.

- **Banco de Dados**:
  - Persistência de dados utilizando PostgreSQL.

- **Polimorfismo e Orientação a Objetos**:
  - Implementação modular baseada nos princípios da POO.
  - Utilização de herança, polimorfismo e encapsulamento.

---

## 🚀 Tecnologias Utilizadas

- **Linguagem**: Java 11+
- **Bibliotecas**:
  - JFRAME (interface gráfica)
  - JDBC (conexão com banco de dados)
- **Banco de Dados**:
  - PostgreSQL
- **IDE**:
  - Eclipse

---

## 🖥️ Estrutura do Projeto

```plaintext
/src
 ├── front                # Telas do sistema (JFRAME)
 ├── gestao               # Modelos de dados (Cliente, Agendamento, etc.)
 ├── modelo
 │    ├── dominio.dao     # DAO para acesso ao banco de dados
 │    ├── conexao         # Configuração da conexão com o banco
/db                      # Scripts SQL para criar as tabelas
/screenshots             # Imagens do sistema para documentação
```

## ⚙️ Como Executar o Sistema

# Clone este repositório
git clone https://github.com/Wyul/NexusERP/

# Configure o banco de dados

Restaure o backup do banco de dados que está no caminho NexusERP/ERP/java-jdbc, com nome erp.backup (backup geradp via função do sistema).
Pode ser feito via pgadmin4, definindo a versão do Postgres 17 (somente nesta versão vai subir o backup)


lembre de usar trocar a senha do banco de dados "ERP/java-jdbc/src/main/java/modelo/dominio/dao/conexao/ConexaoSQL.java"

# Configure a conexão no arquivo ConexaoSQL.java
private static final String URL = "jdbc:jdbc:postgresql://localhost:5432/erp";
private static final String USER = "postgres";
private static final String PASSWORD = "1234"; // precisa trocar sua senha para do banco

# Execute o método main na classe MainFrame para iniciar o sistema
MainFrame.java


## 🛠️ Funcionalidades Detalhadas

Clientes:

Cadastro de clientes com validação de CNPJ ou CPF.
Consulta por nome ou CNPJ/CPF.

Fornecedores:

Cadastro completo com dados fiscais (CNPJ, IE) e contato.
Listagem e busca avançada.

Agendamentos:

Agendamento de serviços vinculados a clientes e veículos.
Persistência de data e situação (Aberto/Fechado/Pendente).

Produtos:

Cadastro de produtos com descrição, preço e categoria.

Vendas:

Registro de vendas com informações detalhadas.
Suporte a produtos e emissão de notas fiscais.



