-- Índice para o CPF do cliente (útil em buscas por cliente)
CREATE INDEX idx_cliente_cpf ON cliente(cpf);

-- Índice para o código do produto (útil para a tabela de estoque)
CREATE INDEX idx_estoque_codigo_pro ON estoque(codigo_pro);

-- Índice para o nome do usuário (útil para login ou buscas)
CREATE INDEX idx_usuario_nome ON usuario(nome);

-- Índice para o CNPJ do fornecedor (útil em consultas de fornecedor)
CREATE INDEX idx_fornecedor_cnpj ON fornecedor(cnpj);

-- Índice para a coluna 'status' da tabela agendamento (útil em filtros de status)
CREATE INDEX idx_agendamento_status ON agendamento(status);


-- View para Agendamentos
CREATE VIEW vw_agendamentos AS
SELECT 
    a.id AS agendamento_id,
    c.nome AS cliente_nome,
    c.cpf AS cliente_cpf,
    c.telefone AS cliente_telefone,
    a.nome_veiculo,
    a.marca_veiculo,
    a.data_entrada,
    a.status,
    a.defeito,
    a.obs_age
FROM 
    agendamento a
JOIN 
    cliente c ON a.id_cliente = c.id
WHERE 
    a.status = 'A';  -- mostrar apenas agendamentos ativos


-- View para Vendas
CREATE VIEW vw_vendas AS
SELECT 
    v.id AS venda_id,
    v.data_venda,
    v.valor,
    v.forma_pagamento,
    c.nome AS cliente_nome,
    c.cpf AS cliente_cpf,
    p.nome_pro AS produto_nome,
    dv.quantidade,
    dv.preco_unitario
FROM 
    venda_nfe v
JOIN 
    cliente c ON v.id_cliente = c.id
JOIN 
    detalhes_venda dv ON v.id = dv.id_venda
JOIN 
    estoque p ON dv.cod_prod = p.codigo_pro;


-- View para visualizar os produtos em estoque
CREATE VIEW vw_estoque_produtos AS
SELECT 
    e.id_prod AS produto_id,
    e.codigo_pro AS codigo_produto,
    e.nome_pro AS nome_produto,
    e.quant_pro AS quantidade_em_estoque,
    e.prc_venda AS preco_venda,
    e.estado AS produto_ativo,
    e.data_cad AS data_cadastro,
    e.tipo_emba AS tipo_embalagem
FROM 
    estoque e
WHERE 
    e.estado = TRUE;  -- Excluir produtos inativos


-- View para visualizar agendamentos com data e status
CREATE VIEW vw_agendamentos_status AS
SELECT 
    a.id AS agendamento_id,
    a.data_entrada AS data_entrada,
    a.status AS status_agendamento,
    a.nome_veiculo,
    a.marca_veiculo,
    c.nome AS cliente_nome,
    c.cpf AS cliente_cpf,
    c.telefone AS cliente_telefone,
    a.defeito AS defeito_relatado,
    a.obs_age AS observacoes
FROM 
    agendamento a
JOIN 
    cliente c ON a.id_cliente = c.id
ORDER BY 
    a.data_entrada DESC;  -- Ordenar por data de entrada, mais recentes primeiro


-- View para visualizar as vendas por período (mês)
CREATE VIEW vw_vendas_mes AS
SELECT 
    v.id AS venda_id,
    v.data_venda AS data_venda,
    v.valor AS valor_total,
    v.forma_pagamento,
    v.nome_cliente,
    v.nome_veiculo,
    v.marca_veiculo,
    c.nome AS cliente_nome,
    c.cpf AS cliente_cpf,
    dv.cod_prod AS codigo_produto,
    p.nome_pro AS nome_produto,
    dv.quantidade AS quantidade_vendida,
    dv.preco_unitario AS preco_unitario
FROM 
    venda_nfe v
JOIN 
    cliente c ON v.id_cliente = c.id
JOIN 
    detalhes_venda dv ON v.id = dv.id_venda
JOIN 
    estoque p ON dv.cod_prod = p.codigo_pro
WHERE 
    EXTRACT(MONTH FROM v.data_venda) = EXTRACT(MONTH FROM CURRENT_DATE)  -- Filtra para o mês atual
AND 
    EXTRACT(YEAR FROM v.data_venda) = EXTRACT(YEAR FROM CURRENT_DATE);  -- Filtra para o ano atual


-- View para visualizar clientes com agendamentos futuros
CREATE VIEW vw_clientes_agendados AS
SELECT 
    c.id AS cliente_id,
    c.nome AS cliente_nome,
    c.cpf AS cliente_cpf,
    c.telefone AS cliente_telefone,
    a.id AS agendamento_id,
    a.nome_veiculo,
    a.marca_veiculo,
    a.data_entrada AS data_agendamento
FROM 
    cliente c
JOIN 
    agendamento a ON c.id = a.id_cliente
WHERE 
    a.data_entrada > CURRENT_TIMESTAMP  -- Exclui agendamentos passados
ORDER BY 
    a.data_entrada ASC;  -- Ordena pelo agendamento mais próximo


-- View para visualizar movimentações de estoque
CREATE VIEW vw_movimentacao_estoque AS
SELECT 
    e.codigo_pro AS codigo_produto,
    e.nome_pro AS nome_produto,
    COALESCE(SUM(dv.quantidade), 0) AS quantidade_vendida,
    e.quant_pro - COALESCE(SUM(dv.quantidade), 0) AS quantidade_restante,
    e.prc_venda AS preco_venda,
    e.data_cad AS data_cadastro
FROM 
    estoque e
LEFT JOIN 
    detalhes_venda dv ON e.codigo_pro = dv.cod_prod
GROUP BY 
    e.codigo_pro, e.nome_pro, e.quant_pro, e.prc_venda, e.data_cad
ORDER BY 
    e.nome_pro;


-- View para visualizar clientes que realizaram vendas
CREATE VIEW vw_clientes_com_vendas AS
SELECT 
    v.id AS venda_id,
    v.data_venda AS data_venda,
    c.nome AS cliente_nome,
    c.cpf AS cliente_cpf,
    p.nome_pro AS produto_nome,
    dv.quantidade AS quantidade_vendida,
    dv.preco_unitario AS preco_unitario
FROM 
    venda_nfe v
JOIN 
    cliente c ON v.id_cliente = c.id
JOIN 
    detalhes_venda dv ON v.id = dv.id_venda
JOIN 
    estoque p ON dv.cod_prod = p.codigo_pro
ORDER BY 
    v.data_venda DESC;  -- Ordenar pelas vendas mais recentes


-- View para visualizar o status de pagamento das vendas
CREATE VIEW vw_pagamentos_vendas AS
SELECT 
    v.id AS venda_id,
    v.data_venda AS data_venda,
    v.valor AS valor_total,
    v.forma_pagamento,
    v.nome_cliente,
    v.nome_veiculo,
    v.marca_veiculo,
    CASE
        WHEN v.forma_pagamento = 'À vista' THEN 'Pago'
        ELSE 'A pagar'  -- Pode ser ajustado para integrar com um sistema de controle de pagamentos
    END AS status_pagamento
FROM 
    venda_nfe v
ORDER BY 
    v.data_venda DESC;  -- Ordena pela data da venda mais recente
