CREATE TABLE mecanica (
    id_mec SERIAL PRIMARY KEY,
    nom_mec VARCHAR(50) NOT NULL,
    cnpj_mec INT NOT NULL UNIQUE,
    loc_mec VARCHAR(60) NOT NULL,
    telefone INT NOT NULL,
    parceria_id_pesquisa INT NOT NULL
);
COMMENT ON TABLE mecanica IS 'Mecanica';
COMMENT ON COLUMN mecanica.id_mec IS 'id da mecanica';
COMMENT ON COLUMN mecanica.nom_mec IS 'nome da mecanica';
COMMENT ON COLUMN mecanica.cnpj_mec IS 'cnpj da mecanica';
COMMENT ON COLUMN mecanica.loc_mec IS 'endereco da mecanica';
COMMENT ON COLUMN mecanica.telefone IS 'telefone da mecanica';

CREATE TABLE funcionario (
    id_fun SERIAL PRIMARY KEY,
    nom_func VARCHAR(50) NOT NULL,
    data_fun DATE NOT NULL UNIQUE,
    cpf_cnpj_fun INT NOT NULL UNIQUE,
    loc_fun VARCHAR(50) NOT NULL,
    cargo_fun VARCHAR(40) NOT NULL,
    mecanica_id_mec INT NOT NULL,
    mecanica_cnpj_mec INT,
    login_id_usuario INT NOT NULL,
    FOREIGN KEY (mecanica_id_mec, mecanica_cnpj_mec) REFERENCES mecanica(id_mec, cnpj_mec),
    FOREIGN KEY (login_id_usuario) REFERENCES login(id_usuario)
);
COMMENT ON TABLE funcionario IS 'funcionario da mecanica';
COMMENT ON COLUMN funcionario.id_fun IS 'id do funcionario';
COMMENT ON COLUMN funcionario.nom_func IS 'nome do funcionario';
COMMENT ON COLUMN funcionario.data_fun IS 'data de aniversário';
COMMENT ON COLUMN funcionario.cpf_cnpj_fun IS 'cnpj ou cpf do funcionario';
COMMENT ON COLUMN funcionario.loc_fun IS 'endereco do funcionario';
COMMENT ON COLUMN funcionario.cargo_fun IS 'cargo do funcionario';

CREATE TABLE cliente (
    id_cli SERIAL PRIMARY KEY,
    nom_cli VARCHAR(50) NOT NULL,
    telefo_cli INT NOT NULL,
    loc_cli VARCHAR(60) NOT NULL,
    sexo VARCHAR(1),
    cnpj_cpf INT NOT NULL UNIQUE,
    incri_cli INT UNIQUE,
    email VARCHAR(60),
    cnpj_mec INT,
    FOREIGN KEY (id_cli, cnpj_mec) REFERENCES mecanica(id_mec, cnpj_mec)
);

ALTER TABLE cliente
ADD COLUMN data_fun DATE NOT NULL UNIQUE;

COMMENT ON TABLE cliente IS 'cliente da mecanica';
COMMENT ON COLUMN cliente.id_cli IS 'id do cliente';
COMMENT ON COLUMN cliente.nom_cli IS 'nome do cliente';
COMMENT ON COLUMN cliente.telefo_cli IS 'telefone do cliente';
COMMENT ON COLUMN cliente.loc_cli IS 'endereco do cliente';
COMMENT ON COLUMN cliente.sexo IS 'sexo do cliente';
COMMENT ON COLUMN cliente.cnpj_cpf IS 'cnpj e cpf do cliente';
COMMENT ON COLUMN cliente.incri_cli IS 'inscricao estadual do cliente *caso cnpj';
COMMENT ON COLUMN cliente.email IS 'email do cliente';

CREATE TABLE fornecedor (
    id_forn SERIAL PRIMARY KEY,
    nom_for VARCHAR(60) NOT NULL,
    cnpj_forn INT NOT NULL UNIQUE,
    loc_for VARCHAR(40) NOT NULL,
    tel_forn INT NOT NULL,
    incri_forn INT NOT NULL UNIQUE,
    id_mec INT NOT NULL,
    cnpj_mec INT,
    FOREIGN KEY (id_mec, cnpj_mec) REFERENCES mecanica(id_mec, cnpj_mec)
);
COMMENT ON TABLE fornecedor IS 'fornecedor de produtos para estoque';
COMMENT ON COLUMN fornecedor.id_forn IS 'id do fornecedor';
COMMENT ON COLUMN fornecedor.nom_for IS 'nome do fornecedor';
COMMENT ON COLUMN fornecedor.cnpj_forn IS 'cnpj do fornecedor';
COMMENT ON COLUMN fornecedor.loc_for IS 'endereço do fornecedor';
COMMENT ON COLUMN fornecedor.tel_forn IS 'Telefone de fornecedor';
COMMENT ON COLUMN fornecedor.incri_forn IS 'inscrição estadual';

CREATE TABLE estoque (
    id_prod SERIAL PRIMARY KEY,
    nom_prod VARCHAR(40) NOT NULL,
    ref_prod INT NOT NULL UNIQUE,
    codbarra_prod INT UNIQUE,
    ncm_prod INT NOT NULL UNIQUE,
    id_mec INT NOT NULL,
    cnpj_mec INT,
    compraid_compra INT NOT NULL,
    estoqueid_prod INT NOT NULL,
    garantiaid_garantia INT NOT NULL,
    FOREIGN KEY (id_mec, cnpj_mec) REFERENCES mecanica(id_mec, cnpj_mec),
    FOREIGN KEY (compraid_compra) REFERENCES pedido_comopra(id_compra)
);
COMMENT ON TABLE estoque IS 'tabela de produtos da mecanica em estoque';
COMMENT ON COLUMN estoque.id_prod IS 'id do produto';
COMMENT ON COLUMN estoque.nom_prod IS 'Nome do produto';
COMMENT ON COLUMN estoque.ref_prod IS 'codigo de referencia do produto';
COMMENT ON COLUMN estoque.codbarra_prod IS 'codigo de barras do produto';
COMMENT ON COLUMN estoque.ncm_prod IS 'ncm do produto';

CREATE TABLE agendamento (
    funcionariosid_fun INT NOT NULL,
    servicoid_ser INT NOT NULL,
    servico VARCHAR(100),
    date_entrada DATE,
    clienteid_cli INT NOT NULL,
    id_agendamento SERIAL PRIMARY KEY,
    FOREIGN KEY (funcionariosid_fun) REFERENCES funcionario(id_fun),
    FOREIGN KEY (servicoid_ser) REFERENCES servico(id_ser),
    FOREIGN KEY (clienteid_cli) REFERENCES cliente(id_cli)
);
COMMENT ON TABLE agendamento IS 'agendamento de serviços';
COMMENT ON COLUMN agendamento.servico IS 'serviço realizado';
COMMENT ON COLUMN agendamento.date_entrada IS 'data de entrada do servico';

CREATE TABLE servico (
    id_ser SERIAL PRIMARY KEY,
    id_veic INT,
    status VARCHAR(60) NOT NULL,
    data_enc DATE NOT NULL
);
COMMENT ON TABLE servico IS 'tabela de serviços realizados';
COMMENT ON COLUMN servico.id_veic IS 'id do veiculo';
COMMENT ON COLUMN servico.status IS 'status do servico';
COMMENT ON COLUMN servico.data_enc IS 'data de encerramento';

CREATE TABLE prod_servico (
    id_prod_ser INT NOT NULL,
    id_servico INT NOT NULL,
    valor_produto INT NOT NULL,
    quant_produto INT NOT NULL,
    PRIMARY KEY (id_prod_ser, id_servico),
    FOREIGN KEY (id_prod_ser) REFERENCES estoque(id_prod),
    FOREIGN KEY (id_servico) REFERENCES servico(id_ser)
);
COMMENT ON TABLE prod_servico IS 'Produtos da ordem de servicos';
COMMENT ON COLUMN prod_servico.id_prod_ser IS 'id do produto no servico';
COMMENT ON COLUMN prod_servico.id_servico IS 'id do serviço';
COMMENT ON COLUMN prod_servico.valor_produto IS 'valor do produto';
COMMENT ON COLUMN prod_servico.quant_produto IS 'Quantidade de produto';

CREATE TABLE logistica (
    fornecedorid_forn INT NOT NULL,
    estoqueid_prod INT NOT NULL,
    id_tran SERIAL PRIMARY KEY,
    cnpj_tran INT NOT NULL UNIQUE,
    ie_tran INT UNIQUE,
    cep_tran INT NOT NULL,
    nom_tran INT NOT NULL,
    FOREIGN KEY (fornecedorid_forn) REFERENCES fornecedor(id_forn),
    FOREIGN KEY (estoqueid_prod) REFERENCES estoque(id_prod)
);
COMMENT ON TABLE logistica IS 'transportadora de produtos para estoque da mecanica';
COMMENT ON COLUMN logistica.id_tran IS 'id  da transportadora';
COMMENT ON COLUMN logistica.cnpj_tran IS 'cnpj da transportadora';
COMMENT ON COLUMN logistica.ie_tran IS 'inscrição estadual';
COMMENT ON COLUMN logistica.cep_tran IS 'cep da transportadora';

CREATE TABLE vendas (
    id_venda SERIAL PRIMARY KEY,
    cnpj_cliente INT NOT NULL,
    cep INT NOT NULL,
    endereco INT NOT NULL,
    numero INT,
    cfop INT NOT NULL UNIQUE,
    data_emissao DATE NOT NULL,
    vendedor VARCHAR(50),
    modelo INT NOT NULL,
    data_autori DATE NOT NULL,
    produto_ven VARCHAR(60) NOT NULL,
    funcionarioid_fun INT NOT NULL,
    FOREIGN KEY (funcionarioid_fun) REFERENCES funcionario(id_fun)
);
COMMENT ON TABLE vendas IS 'vendas de produtos e serviços';
COMMENT ON COLUMN vendas.id_venda IS 'id de venda de produtos';
COMMENT ON COLUMN vendas.cnpj_cliente IS 'cpf/cnpj do cliente da nf-s';
COMMENT ON COLUMN vendas.cep IS 'cep de entrega';
COMMENT ON COLUMN vendas.endereco IS 'endereço de entrega';
COMMENT ON COLUMN vendas.numero IS 'numero de entrega';
COMMENT ON COLUMN vendas.cfop IS 'CFOP de operação';
COMMENT ON COLUMN vendas.data_emissao IS 'data de emissão da nf-s';
COMMENT ON COLUMN vendas.modelo IS 'modelo de nf-e';
COMMENT ON COLUMN vendas.data_autori IS 'data de autorização de nf-e';
COMMENT ON COLUMN vendas.produto_ven IS 'produto/serviço vendido';

CREATE TABLE financeiros (
    id_pagamento SERIAL PRIMARY KEY,
    forma_pag INT NOT NULL,
    data_vencimento DATE NOT NULL,
    data_emissao DATE NOT NULL,
    contas_idempresa INT NOT NULL,
    FOREIGN KEY (contas_idempresa) REFERENCES conta_pagar(id_empresa)
);
COMMENT ON TABLE financeiros IS 'tabela de pagamentos financeiros';
COMMENT ON COLUMN financeiros.id_pagamento IS 'id da forma de pagamento';
COMMENT ON COLUMN financeiros.forma_pag IS 'Forma ultilizado para pagamento';
COMMENT ON COLUMN financeiros.data_vencimento IS 'Data de vencimento';
COMMENT ON COLUMN financeiros.data_emissao IS 'data de emissão';

CREATE TABLE conta_pagar (
    id_empresa SERIAL PRIMARY KEY,
    id_empresa2 INT,
    fornecedorid_forn INT NOT NULL,
    despesa INT,
    dat_lanc DATE NOT NULL,
    dat_emissao DATE NOT NULL,
    dat_venci DATE NOT NULL,
    valor_pagar INT,
    pagamentosid_pagamento INT NOT NULL,
    FOREIGN KEY (fornecedorid_forn) REFERENCES fornecedor(id_forn),
    FOREIGN KEY (pagamentosid_pagamento) REFERENCES financeiros(id_pagamento)
);
COMMENT ON TABLE conta_pagar IS 'tabela de contas a pagar';
COMMENT ON COLUMN conta_pagar.id_empresa IS 'id da empresa';
COMMENT ON COLUMN conta_pagar.id_empresa2 IS 'id da empresa 2';
COMMENT ON COLUMN conta_pagar.fornecedorid_forn IS 'id do fornecedor';
COMMENT ON COLUMN conta_pagar.despesa IS 'Despesa';
COMMENT ON COLUMN conta_pagar.dat_lanc IS 'data do lançamento da despesa';
COMMENT ON COLUMN conta_pagar.dat_emissao IS 'data de emissão';
COMMENT ON COLUMN conta_pagar.dat_venci IS 'data de vencimento';
COMMENT ON COLUMN conta_pagar.valor_pagar IS 'valor a pagar';

CREATE TABLE colaborador (
    id_colaborador SERIAL PRIMARY KEY,
    nom_colaborador VARCHAR(40),
    setor VARCHAR(40),
    cargo VARCHAR(40),
    salario INT NOT NULL
);
COMMENT ON TABLE colaborador IS 'colaboradores da mecanica';
COMMENT ON COLUMN colaborador.id_colaborador IS 'id do colaborador';
COMMENT ON COLUMN colaborador.nom_colaborador IS 'nome do colaborador';
COMMENT ON COLUMN colaborador.setor IS 'setor do colaborador';
COMMENT ON COLUMN colaborador.cargo IS 'cargo do colaborador';
COMMENT ON COLUMN colaborador.salario IS 'salario do colaborador';

CREATE TABLE cargo_colaborador (
    id_cargo SERIAL PRIMARY KEY,
    cargo VARCHAR(40),
    salario INT
);
COMMENT ON TABLE cargo_colaborador IS 'cargo dos colaboradores';
COMMENT ON COLUMN cargo_colaborador.id_cargo IS 'id do cargo';
COMMENT ON COLUMN cargo_colaborador.cargo IS 'cargo do colaborador';
COMMENT ON COLUMN cargo_colaborador.salario IS 'salário do cargo';

CREATE TABLE login (
    id_usuario SERIAL PRIMARY KEY,
    nom_usuario VARCHAR(40) UNIQUE,
    senha VARCHAR(30) NOT NULL,
    setor VARCHAR(40) NOT NULL,
    cargo VARCHAR(40) NOT NULL,
    data_cadastro DATE
);
COMMENT ON TABLE login IS 'tabela de login dos usuarios';
COMMENT ON COLUMN login.id_usuario IS 'id do usuario';
COMMENT ON COLUMN login.nom_usuario IS 'nome de usuario';
COMMENT ON COLUMN login.senha IS 'senha do usuario';
COMMENT ON COLUMN login.setor IS 'setor do usuario';
COMMENT ON COLUMN login.cargo IS 'cargo do usuario';
COMMENT ON COLUMN login.data_cadastro IS 'data de cadastro';

CREATE TABLE garantia (
    id_garantia SERIAL PRIMARY KEY,
    dat_lanc DATE NOT NULL,
    dat_validade DATE NOT NULL,
    numero_nota INT NOT NULL,
    produto VARCHAR(40) NOT NULL,
    id_mec INT NOT NULL,
    FOREIGN KEY (id_mec) REFERENCES mecanica(id_mec)
);
COMMENT ON TABLE garantia IS 'tabela de garantias da mecanica';
COMMENT ON COLUMN garantia.id_garantia IS 'id da garantia';
COMMENT ON COLUMN garantia.dat_lanc IS 'data de lançamento';
COMMENT ON COLUMN garantia.dat_validade IS 'data de validade da garantia';
COMMENT ON COLUMN garantia.numero_nota IS 'numero da nota';
COMMENT ON COLUMN garantia.produto IS 'produto na garantia';

CREATE TABLE pesquisa (
    id_pesquisa SERIAL PRIMARY KEY,
    nom_pesquisa VARCHAR(30) NOT NULL,
    tipo_pesquisa VARCHAR(30) NOT NULL
);
COMMENT ON TABLE pesquisa IS 'pesquisas de satisfação';
COMMENT ON COLUMN pesquisa.id_pesquisa IS 'id da pesquisa';
COMMENT ON COLUMN pesquisa.nom_pesquisa IS 'nome da pesquisa';
COMMENT ON COLUMN pesquisa.tipo_pesquisa IS 'tipo da pesquisa';

--Relatorio 1
SELECT id_cli, nom_cli, telefo_cli, loc_cli, sexo, cnpj_cpf, incri_cli, email
FROM cliente
WHERE sexo = 'F'
  AND EXTRACT(YEAR FROM AGE(data_fun)) BETWEEN 20 AND 30
ORDER BY nom_cli ASC;

--Relatorio 2
SELECT s.id_ser, s.status, s.data_enc, c.nom_cli, c.loc_cli
FROM servico s
JOIN cliente c ON s.id_cli = c.id_cli
WHERE EXTRACT(MONTH FROM s.data_enc) IN (1, 3, 5, 7, 9, 11)
  AND EXTRACT(YEAR FROM s.data_enc) = 2023
  AND c.loc_cli IN ('São Miguel do Oeste', 'Descanso')
ORDER BY s.data_enc ASC;

--Relatorio 3
SELECT f.nom_for, f.cnpj_forn, f.loc_for, COUNT(*) as fornecedor_count
FROM fornecedor f
JOIN estoque e ON f.id_forn = e.id_mec
WHERE f.loc_for IN ('Maravilha', 'Descanso', 'Itapiranga', 'Guaraciaba')
  AND e.nom_prod LIKE '%BMW%'
GROUP BY f.loc_for, f.nom_for, f.cnpj_forn
ORDER BY fornecedor_count DESC, f.loc_for ASC;

--Relatorio 4
SELECT s.status AS tipo_servico, COUNT(s.id_ser) AS total_servicos, SUM(ps.valor_produto) AS valor_total
FROM servico s
JOIN prod_servico ps ON s.id_ser = ps.id_servico
GROUP BY s.status
ORDER BY valor_total DESC;

INSERT INTO mecanica (nom_mec, cnpj_mec, loc_mec, telefone, parceria_id_pesquisa); VALUES
('Mecanica Alfa', 12345678901234, 'Rua A, 100', 1111111111, 1),
('Mecanica Beta', 23456789012345, 'Rua B, 200', 2222222222, 2),
('Mecanica Gamma', 34567890123456, 'Rua C, 300', 3333333333, 3),
('Mecanica Delta', 45678901234567, 'Rua D, 400', 4444444444, 4),
('Mecanica Epsilon', 56789012345678, 'Rua E, 500', 5555555555, 5);

INSERT INTO funcionario (nom_func, data_fun, cpf_cnpj_fun, loc_fun, cargo_fun, mecanica_id_mec, mecanica_cnpj_mec, login_id_usuario); VALUES
('Carlos Silva', '1985-06-15', 12345678901, 'Rua X, 10', 'Mecânico', 1, 12345678901234, 1),
('Ana Souza', '1990-07-20', 23456789012, 'Rua Y, 20', 'Atendente', 2, 23456789012345, 2),
('João Lima', '1980-08-25', 34567890123, 'Rua Z, 30', 'Supervisor', 3, 34567890123456, 3),
('Maria Oliveira', '1995-09-30', 45678901234, 'Rua W, 40', 'Gerente', 4, 45678901234567, 4),
('Lucas Pereira', '1975-10-05', 56789012345, 'Rua V, 50', 'Auxiliar', 5, 56789012345678, 5);

INSERT INTO cliente (nom_cli, telefo_cli, loc_cli, sexo, cnpj_cpf, incri_cli, email, data_fun); VALUES
('Julia Santos', 123456789, 'Rua F, 100', 'F', 12345678901, 123, 'julia@gmail.com', '1995-05-15'),
('Pedro Martins', 234567890, 'Rua G, 200', 'M', 23456789012, 234, 'pedro@gmail.com', '1990-06-20'),
('Mariana Almeida', 345678901, 'Rua H, 300', 'F', 34567890123, 345, 'mariana@gmail.com', '1985-07-25'),
('Thiago Costa', 456789012, 'Rua I, 400', 'M', 45678901234, 456, 'thiago@gmail.com', '1980-08-30'),
('Fernanda Lima', 567890123, 'Rua J, 500', 'F', 56789012345, 567, 'fernanda@gmail.com', '1975-09-05');

INSERT INTO fornecedor (nom_for, cnpj_forn, loc_for, tel_forn, incri_forn, id_mec, cnpj_mec); VALUES
('Fornecedor Alfa', 12345678901234, 'Rua L, 1000', 123456789, 1234, 1, 12345678901234),
('Fornecedor Beta', 23456789012345, 'Rua M, 1100', 234567890, 2345, 2, 23456789012345),
('Fornecedor Gamma', 34567890123456, 'Rua N, 1200', 345678901, 3456, 3, 34567890123456),
('Fornecedor Delta', 45678901234567, 'Rua O, 1300', 456789012, 4567, 4, 45678901234567),
('Fornecedor Epsilon', 56789012345678, 'Rua P, 1400', 567890123, 5678, 5, 56789012345678);

INSERT INTO estoque (nom_prod, ref_prod, codbarra_prod, ncm_prod, id_mec, cnpj_mec, compraid_compra, estoqueid_prod, garantiaid_garantia); VALUES
('Produto Alfa', 1001, 1111111111111, 12345678, 1, 12345678901234, 1, 1, 1),
('Produto Beta', 1002, 2222222222222, 23456789, 2, 23456789012345, 2, 2, 2),
('Produto Gamma', 1003, 3333333333333, 34567890, 3, 34567890123456, 3, 3, 3),
('Produto Delta', 1004, 4444444444444, 45678901, 4, 45678901234567, 4, 4, 4),
('Produto Epsilon', 1005, 5555555555555, 56789012, 5, 56789012345678, 5, 5, 5);

INSERT INTO agendamento (funcionariosid_fun, servicoid_ser, servico, date_entrada, clienteid_cli); VALUES
(1, 1, 'Troca de óleo', '2023-01-15', 1),
(2, 2, 'Revisão geral', '2023-03-20', 2),
(3, 3, 'Alinhamento', '2023-05-25', 3),
(4, 4, 'Balanceamento', '2023-07-30', 4),
(5, 5, 'Troca de pneus', '2023-09-05', 5);

INSERT INTO servico (id_veic, status, data_enc); VALUES
(1, 'Concluído', '2023-01-20'),
(2, 'Pendente', '2023-03-25'),
(3, 'Em andamento', '2023-05-30'),
(4, 'Concluído', '2023-08-05'),
(5, 'Cancelado', '2023-10-10');

INSERT INTO prod_servico (id_prod_ser, id_servico, valor_produto, quant_produto); VALUES
(1, 1, 100, 2),
(2, 2, 200, 3),
(3, 3, 300, 4),
(4, 4, 400, 5),
(5, 5, 500, 6);

INSERT INTO logistica (fornecedorid_forn, estoqueid_prod, cnpj_tran, ie_tran, cep_tran, nom_tran); VALUES
(1, 1, 12345678901234, 12345, 12345678, 'Transportadora Alfa'),
(2, 2, 23456789012345, 23456, 23456789, 'Transportadora Beta'),
(3, 3, 34567890123456, 34567, 34567890, 'Transportadora Gamma'),
(4, 4, 45678901234567, 45678, 45678901, 'Transportadora Delta'),
(5, 5, 56789012345678, 56789, 56789012, 'Transportadora Epsilon');

INSERT INTO vendas (cnpj_cliente, cep, endereco, numero, cfop, data_emissao, vendedor, modelo, data_autori, produto_ven, funcionarioid_fun); VALUES
(12345678901, 12345678, 1111, 10, 1001, '2023-01-15', 'Carlos Silva', 1, '2023-01-16', 'Produto Alfa', 1),
(23456789012, 23456789, 2222, 20, 2002, '2023-03-20', 'Ana Souza', 2, '2023-03-21', 'Produto Beta', 2),
(34567890123, 34567890, 3333, 30, 3003, '2023-05-25', 'João Lima', 3, '2023-05-26', 'Produto Gamma', 3),
(45678901234, 45678901, 4444, 40, 4004, '2023-07-30', 'Maria Oliveira', 4, '2023-07-31', 'Produto Delta', 4),
(56789012345, 56789012, 5555, 50, 5005, '2023-09-05', 'Lucas Pereira', 5, '2023-09-06', 'Produto Epsilon', 5);

INSERT INTO financeiros (forma_pag, data_vencimento, data_emissao, contas_idempresa); VALUES
(1, '2023-01-15', '2023-01-01', 1),
(2, '2023-03-20', '2023-03-01', 2),
(3, '2023-05-25', '2023-05-01', 3),
(4, '2023-07-30', '2023-07-01', 4),
(5, '2023-09-05', '2023-09-01', 5);

INSERT INTO conta_pagar (id_empresa2, fornecedorid_forn, despesa, dat_lanc, dat_emissao, dat_venci, valor_pagar, pagamentosid_pagamento); VALUES
(2, 1, 1000, '2023-01-01', '2023-01-01', '2023-01-15', 1000, 1),
(3, 2, 2000, '2023-03-01', '2023-03-01', '2023-03-20', 2000, 2),
(4, 3, 3000, '2023-05-01', '2023-05-01', '2023-05-25', 3000, 3),
(5, 4, 4000, '2023-07-01', '2023-07-01', '2023-07-30', 4000, 4),
(6, 5, 5000, '2023-09-01', '2023-09-01', '2023-09-05', 5000, 5);

INSERT INTO colaborador (nom_colaborador, setor, cargo, salario); VALUES
('Carlos Silva', 'Manutenção', 'Mecânico', 3000),
('Ana Souza', 'Recepção', 'Atendente', 2000),
('João Lima', 'Supervisão', 'Supervisor', 4000),
('Maria Oliveira', 'Administração', 'Gerente', 5000),
('Lucas Pereira', 'Auxiliar', 'Auxiliar', 1500);

INSERT INTO cargo_colaborador (cargo, salario); VALUES
('Mecânico', 3000),
('Atendente', 2000),
('Supervisor', 4000),
('Gerente', 5000),
('Auxiliar', 1500);

INSERT INTO login (nom_usuario, senha, setor, cargo, data_cadastro); VALUES
('carlos_silva', 'senha123', 'Manutenção', 'Mecânico', '2023-01-01'),
('ana_souza', 'senha456', 'Recepção', 'Atendente', '2023-03-01'),
('joao_lima', 'senha789', 'Supervisão', 'Supervisor', '2023-05-01'),
('maria_oliveira', 'senha012', 'Administração', 'Gerente', '2023-07-01'),
('lucas_pereira', 'senha345', 'Auxiliar', 'Auxiliar', '2023-09-01');

INSERT INTO garantia (dat_lanc, dat_validade, numero_nota, produto, id_mec); VALUES
('2023-01-01', '2023-12-31', 1001, 'Produto Alfa', 1),
('2023-03-01', '2024-02-29', 2002, 'Produto Beta', 2),
('2023-05-01', '2024-04-30', 3003, 'Produto Gamma', 3),
('2023-07-01', '2024-06-30', 4004, 'Produto Delta', 4),
('2023-09-01', '2024-08-31', 5005, 'Produto Epsilon', 5);

INSERT INTO pesquisa (nom_pesquisa, tipo_pesquisa); VALUES
('Satisfação Cliente', 'Qualitativa'),
('Qualidade Serviço', 'Quantitativa'),
('Tempo de Espera', 'Qualitativa'),
('Atendimento', 'Quantitativa'),
('Feedback Produto', 'Qualitativa');















