-- Criação das Tabelas

-- Tabela: permissao
CREATE TABLE permissao (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT
);

-- Tabela: usuario
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    usuario VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50),
    estado BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela: usuario_permissao
CREATE TABLE usuario_permissao (
    permissao_id INT NOT NULL REFERENCES permissao(id),
    usuario_id INT NOT NULL REFERENCES usuario(id),
    PRIMARY KEY (permissao_id, usuario_id)
);

-- Tabela: cliente
CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    rua VARCHAR(150),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    cnpj VARCHAR(18),
    razao_social VARCHAR(150),
    ic_cli VARCHAR(20),
    representante VARCHAR(100),
    estado BOOLEAN DEFAULT TRUE,
    for_cli_trans BOOLEAN DEFAULT FALSE
);

-- Tabela: agendamento
CREATE TABLE agendamento (
    id SERIAL PRIMARY KEY,
    id_cliente INT NOT NULL REFERENCES cliente(id),
    cnpj VARCHAR(18),
    telefone VARCHAR(20),
    nome_cliente VARCHAR(100),
    nome_veiculo VARCHAR(100),
    marca_veiculo VARCHAR(100),
    data_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status CHAR(1) CHECK (status IN ('A', 'P', 'F')),
    defeito TEXT,
    obs_age TEXT
);

-- Tabela: estoque
CREATE TABLE estoque (
    id_prod SERIAL PRIMARY KEY,
    codigo_pro VARCHAR(50) UNIQUE NOT NULL,
    nome_pro VARCHAR(100) NOT NULL,
    quant_pro INT NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    prc_custo NUMERIC(10, 2) NOT NULL,
    prc_venda NUMERIC(10, 2) NOT NULL,
    tipo_emba VARCHAR(50),
    data_cad TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    prod_ser BOOLEAN DEFAULT FALSE
);

-- Tabela: fornecedor
CREATE TABLE fornecedor (
    id SERIAL PRIMARY KEY,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100),
    rua VARCHAR(150),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ie_for VARCHAR(50),
    razao_social VARCHAR(150),
    representante VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100)
);

-- Tabela: venda_nfe
CREATE TABLE venda_nfe (
    id SERIAL PRIMARY KEY,
    id_cliente INT NOT NULL REFERENCES cliente(id),
    cnpj VARCHAR(18),
    telefone VARCHAR(20),
    nome_cliente VARCHAR(100),
    nome_veiculo VARCHAR(100),
    marca_veiculo VARCHAR(100),
    forma_pagamento VARCHAR(50),
    valor NUMERIC(10, 2),
    peca VARCHAR(100),
    defeito TEXT,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cod_peca VARCHAR(50) REFERENCES estoque(codigo_pro),
    quantidade INT,
    id_usuario INT NOT NULL REFERENCES usuario(id),
    nome_usuario VARCHAR(100)
);

-- Tabela: detalhes_venda
CREATE TABLE detalhes_venda (
    id SERIAL PRIMARY KEY,
    id_venda INT NOT NULL REFERENCES venda_nfe(id),
    cod_prod VARCHAR(50) REFERENCES estoque(codigo_pro),
    quantidade INT NOT NULL,
    preco_unitario NUMERIC(10, 2) NOT NULL
);

-- Tabela de Auditoria
CREATE TABLE auditoria (
    id SERIAL PRIMARY KEY,
    tabela_nome VARCHAR(100),
    operacao CHAR(1),  -- I: Insert, U: Update, D: Delete
    dados_antigos JSONB,
    dados_novos JSONB,
    usuario VARCHAR(100),
    data_operacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criando Índices (Exemplo)
CREATE INDEX idx_agendamento_status ON agendamento(status);
CREATE INDEX idx_venda_nfe_cliente ON venda_nfe(id_cliente);

-- Criação das Stored Procedures

-- 1. Registrar Venda
CREATE OR REPLACE FUNCTION registrar_venda(
    p_id_cliente INT, 
    p_cnpj VARCHAR(18), 
    p_telefone VARCHAR(20), 
    p_nome_cliente VARCHAR(100),
    p_nome_veiculo VARCHAR(100),
    p_marca_veiculo VARCHAR(100),
    p_forma_pagamento VARCHAR(50), 
    p_valor NUMERIC(10, 2),
    p_peca VARCHAR(50), 
    p_quantidade INT,
    p_id_usuario INT, 
    p_nome_usuario VARCHAR(100)
)
RETURNS VOID AS $$
DECLARE
    v_quantidade_estoque INT;
BEGIN
    -- Verificar se a quantidade disponível no estoque é suficiente
    SELECT quant_pro INTO v_quantidade_estoque
    FROM estoque
    WHERE codigo_pro = p_peca;
    
    IF v_quantidade_estoque < p_quantidade THEN
        RAISE EXCEPTION 'Estoque insuficiente para a peça %', p_peca;
    END IF;

    -- Registrar a venda na tabela venda_nfe
    INSERT INTO venda_nfe (
        id_cliente, cnpj, telefone, nome_cliente, nome_veiculo, 
        marca_veiculo, forma_pagamento, valor, cod_peca, quantidade, 
        id_usuario, nome_usuario
    )
    VALUES (
        p_id_cliente, p_cnpj, p_telefone, p_nome_cliente, p_nome_veiculo, 
        p_marca_veiculo, p_forma_pagamento, p_valor, p_peca, p_quantidade, 
        p_id_usuario, p_nome_usuario
    );

    -- Atualizar o estoque
    UPDATE estoque
    SET quant_pro = quant_pro - p_quantidade
    WHERE codigo_pro = p_peca;

    -- Registrar os detalhes da venda
    INSERT INTO detalhes_venda (id_venda, cod_prod, quantidade, preco_unitario)
    VALUES (
        currval('venda_nfe_id_seq'),
        p_peca, 
        p_quantidade, 
        (SELECT prc_venda FROM estoque WHERE codigo_pro = p_peca)
    );

END;
$$ LANGUAGE plpgsql;

-- 2. Alterar Status de Agendamento
CREATE OR REPLACE FUNCTION alterar_status_agendamento(
    p_id_agendamento INT, 
    p_novo_status CHAR(1)
)
RETURNS VOID AS $$
BEGIN
    -- Verificar se o novo status é válido
    IF p_novo_status NOT IN ('A', 'P', 'F') THEN
        RAISE EXCEPTION 'Status inválido. Use ''A'', ''P'' ou ''F''.';
    END IF;
    
    -- Atualizar o status do agendamento
    UPDATE agendamento
    SET status = p_novo_status
    WHERE id = p_id_agendamento;
    
    -- Opcional: Registrar a alteração de status em auditoria
    INSERT INTO auditoria (tabela_nome, operacao, dados_antigos, dados_novos, usuario)
    VALUES (
        'agendamento', 
        'U', 
        row_to_json((SELECT * FROM agendamento WHERE id = p_id_agendamento)), 
        row_to_json(NEW), 
        current_user
    );
    
END;
$$ LANGUAGE plpgsql;

-- 3. Criar Novo Cliente
CREATE OR REPLACE FUNCTION criar_cliente(
    p_cpf VARCHAR(14), 
    p_nome VARCHAR(100), 
    p_email VARCHAR(100),
    p_telefone VARCHAR(20), 
    p_endereco VARCHAR(150),
    p_bairro VARCHAR(100), 
    p_cidade VARCHAR(100),
    p_cnpj VARCHAR(18), 
    p_razao_social VARCHAR(150),
    p_representante VARCHAR(100)
)
RETURNS VOID AS $$
BEGIN
    -- Verificar se já existe um cliente com o mesmo CPF ou CNPJ
    IF EXISTS (SELECT 1 FROM cliente WHERE cpf = p_cpf) THEN
        RAISE EXCEPTION 'Cliente com o CPF % já existe!', p_cpf;
    END IF;

    IF EXISTS (SELECT 1 FROM cliente WHERE cnpj = p_cnpj) THEN
        RAISE EXCEPTION 'Cliente com o CNPJ % já existe!', p_cnpj;
    END IF;
    
    -- Inserir o novo cliente na tabela cliente
    INSERT INTO cliente (
        cpf, nome, email, telefone, rua, bairro, cidade, cnpj, razao_social, representante
    )
    VALUES (
        p_cpf, p_nome, p_email, p_telefone, p_endereco, p_bairro, p_cidade, p_cnpj, p_razao_social, p_representante
    );
    
END;
$$ LANGUAGE plpgsql;

-- Criação dos Gatilhos (Triggers)

-- Gatilho de Auditoria para a Tabela Agendamento
CREATE OR REPLACE FUNCTION auditoria_agendamento() 
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO auditoria (tabela_nome, operacao, dados_novos, usuario)
        VALUES ('agendamento', 'I', row_to_json(NEW), current_user);
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO auditoria (tabela_nome, operacao, dados_antigos, dados_novos, usuario)
        VALUES ('agendamento', 'U', row_to_json(OLD), row_to_json(NEW), current_user);
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO auditoria (tabela_nome, operacao, dados_antigos, usuario)
        VALUES ('agendamento', 'D', row_to_json(OLD), current_user);
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Associando o Gatilho
CREATE TRIGGER trigger_auditoria_agendamento
AFTER INSERT OR UPDATE OR DELETE ON agendamento
FOR EACH ROW EXECUTE FUNCTION auditoria_agendamento();

-- Gatilho para Atualizar o Estoque após Venda
CREATE OR REPLACE FUNCTION estoque_after_venda() 
RETURNS TRIGGER AS $$
BEGIN
    -- Atualiza o estoque
    UPDATE estoque
    SET quant_pro = quant_pro - NEW.quantidade
    WHERE codigo_pro = NEW.cod_peca;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Associando o Gatilho de Atualização de Estoque após Venda
CREATE TRIGGER trigger_estoque_after_venda
AFTER INSERT ON venda_nfe
FOR EACH ROW EXECUTE FUNCTION estoque_after_venda();

-- Políticas de Acesso

-- Criação de Usuários
CREATE USER administrador WITH PASSWORD 'admin123';
CREATE USER suporte WITH PASSWORD 'suporte123';
CREATE USER mecanico WITH PASSWORD 'mecanico123';
CREATE USER usuario WITH PASSWORD 'usuario123';

-- Atribuindo Permissões de DBA (Administrador e Suporte)
GRANT ALL PRIVILEGES ON DATABASE mecânica TO administrador, suporte;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO administrador, suporte;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO administrador, suporte;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO administrador, suporte;

-- Atribuindo Permissões de Consulta (Mecânico e Usuário)
GRANT SELECT ON ALL TABLES IN SCHEMA public TO mecanico, usuario;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO mecanico, usuario;
