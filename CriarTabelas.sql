create table mecanica(
id_mec integer(2) NOT NULL PRIMARY key UNIQUE, 
nom_mec varchar(50) NOT NULL, 
cnpj_mec integer(14) NOT null UNIQUE, 
loc_mec varchar(60) NOT NULL, 
telefone integer(13) NOT NULL,
id_pesquisa integer(4) REFERENCES parceria_convenio(id_pesquisa)
)

CREATE TABLE parceria_convenio(
id_pesquisa integer(4) NOT NULL,
nome_parceiro varchar(50) UNIQUE NOT NULL,
tipo_parceria varchar(100) NOT NULL,
id_forn integer(10) NOT NULL,
id_cli integer(2) REFERENCES cliente(id_cli) NOT NULL
)

create table clientes(
 id_cli integer(2) not null primary key unique,
 nom_cli varchar(50) NOT NULL,
 telefo_cli integer(8) NOT NULL,
 loc_cli varchar(60) NOT NULL,
 sexo varchar(1),
 cnpj_cpf integer(14) NOT null unique,
 incri_cli integer(9) unique,
 email varchar(60),
 cnpj_mec integer(14) references cliente(cnpj_mec) not null
)

create table funcionario(
 id_fun integer(2) unique not null primary key,
 nom_fun varchar(50) not null,
 data_fun date(8) unique not null,
 cpf_cnpj_fun integer(14) unique not null,
 loc_fun varchar(50) not null,
 cargo_fun varchar(40) not null,
 id_mec integer(2) references mecanica(id_mec) not NULL,
 cnpj_mec integer(14) references mecanica(cnpj_mec) not null
 id_usuario integer(10) REFERENCES login(id_usuario) NOT NULL
 )

create table servico(
 id_ser integer(4) not null primary key,
 id_veic integer(4),
 status varchar(60) not null,
 data_enc date(8) not null
)

create table agendamento(
 id_fun integer(2) references funcionario(id_fun) not null,
 id_ser integer(4) references servico(id_ser) not null primary key,
 servico varchar(100) NOT null,
 data_entrada date(8) NOT null,
 id_cli integer(2) references cliente(id_cli) not NULL,
 id_agendamento integer(4) PRIMARY KEY NOT null
)

create table estoque(
 id_prod integer(2) primary key not null unique,
 nom_prod varchar(60) not null,
 ref_prod integer(5) not null unique,
 codbarra_prod integer(13) unique,
 ncm_prod integer(8) not null unique,
 id_mec integer(2) references mecanica(id_mec) not null,
 cnpj_mec integer(14) references mecanica(cnpj_mec),
 id_compra integer(4) REFERENCES pedido_compra(id_compra) NOT NULL,
 id_garantia integer(4) REFERENCES garantia_produto(id_garantia) NOT NULL
)

create table prod_servico(
 id_prod integer(4) references estoque(id_prod) primary key not null,
 id_ser integer(2) references servico(id_ser) not null,
 valor_produto integer(8) not null,
 quant_produto integer(2) not null
)

create table fornecedor(
 id_for integer(2) unique not null primary key,
 nom_for varchar(60) not null,
 cnpj_for integer(14) unique not null,
 loc_for varchar(40) not null,
 tel_for integer(13) not null,
 insc_for integer(9) unique not null,
 id_mec integer(2) references mecanica(id_mec) not null,
 cnpj_mec integer(2) references mecanica(cnpj_mec)
)

create table logistica(
id_forn integer(2) references fornecedor(id_forn) primary key not NULL,
id_prod integer(2) references estoque(id_prod) primary key not null,
id_tran integer(4) primary key not null unique,
cnpj_tran integer(14) not null unique,
ie_tran integer(9) unique,
cep_tran integer(10) not null,
nom_tran integer(10) not null
)

CREATE TABLE veiculo_estoque(
id_veiculo integer(4) PRIMARY KEY NOT NULL unique,
marca_veiculo varchar(20) NOT NULL,
modelo_veiculo varchar(50) NOT NULL,
situacao_veiculo varchar(100) NOT NULL,
fun_servico_carro integer(2) NOT NULL,
id_agen_veicu integer(4) NOT NULL,
id_servico_car integer(4) NOT NULL
)

CREATE TABLE login(
id_usuario integer(10) UNIQUE NOT NULL PRIMARY KEY 
nome varchar(50) NOT NULL,
senha_usuario varchar(20) NOT NULL,
email varchar(60) NOT NULL,
cep integer(10) NOT NULL,
id_mec_filial integer(2)
)

CREATE TABLE log_erro(
id_erro integer(4) NOT NULL,
data_erro date(8) NOT NULL,
descricao_erro varchar(255) NOT NULL,
local_erro varchar(50) NOT NULL,
id_fun integer(2) REFERENCES funcionario(id_fun) NOT NULL,
usuario varchar(50) NOT NULL
)

CREATE TABLE pedido_compra(
id_compra integer(4) NOT NULL PRIMARY KEY,
emissao date(8) NOT NULL,
previsao_entrega date(8) NOT NULL,
data_faturamento date(8) NOT NULL,
fornecedor varchar(50) NOT NULL,
vendedor varchar(50) NOT NULL,
comprador varchar(50) NOT NULL,
condicao varchar(50) NOT NULL,
cod_item integer(4) NOT NULL,
id_fun integer(2) REFERENCES funcionario(id_fun) NOT NULL
)

CREATE TABLE garantia_produto(
id_garantia integer(4) PRIMARY KEY NOT NULL,
id_prod integer(2) NOT NULL,
tempo_garantia integer(10) NOT NULL,
descri varchar(100) NOT NULL,
procedimento_garantia varchar(100) NOT NULL,
id_garantia_ser integer(4) NOT NULL
)

CREATE TABLE ferramenta(
id_ferramenta integer(4) PRIMARY KEY NOT NULL,
nome_ferramenta varchar(255) NOT NULL,
data_compra date(8) NOT NULL,
situacao_ferramenta integer(10) NOT NULL,
estoque_id_ferramenta integer(2) NOT NULL
)

CREATE TABLE caixa(
id_mov_caixa integer(4) PRIMARY KEY NOT NULL,
tipo_movimento varchar(10) NOT NULL,
forma_pagamento integer(10) NOT NULL,
descricao varchar(100),
id_pagamento integer(4) REFERENCES financeiro(id_pagamento) NOT NULL,
id_mec integer(2) REFERENCES mecanica(id_mec) NOT NULL,
cnpj_mec integer(14) REFERENCES mecaninca(cnpj_mec) NOT NULL
)

CREATE TABLE financeiro(
id_pagamento integer(4) PRIMARY KEY NOT NULL,
forma_pag integer(10) NOT NULL,
data_vencimento date(8) NOT NULL,
data_emissao integer(10) NOT NULL,
id_empresa integer(4) REFERENCES contas_pagar(id_empresa) NOT NULL
)

CREATE TABLE contas_pagar(
id_empresa integer(4) PRIMARY KEY NOT NULL,
documento integer(10) NOT NULL,
nome_dev varchar(50) NOT NULL,
status varchar(20) NOT NULL,
emissao date(8) NOT NULL,
vencimento date(8) NOT NULL,
valor integer(12) NOT NULL,
saldo integer(13) NOT NULL,
tipo varchar(50) NOT NULL,
razao_social varchar(50),
id_cli integer(2) REFERENCES cliente(id_cli) NOT NULL,
id_veiculo_receber integer(4) NOT NULL,
nf_venda integer(4) NOT NULL,
ser_pagar integer(4) NOT NULL
)

CREATE TABLE vendas(
id_venda integer(4) PRIMARY KEY NOT NULL,
cnpj_cliente integer(13) NOT NULL,
cep integer(7) NOT NULL,
endereco integer(10) NOT NULL,
numero integer(5),
cfop integer(4) NOT NULL,
data_emissao date(8) NOT NULL,
vendedor varchar(50),
modelo integer(2) NOT NULL,
data_autori date(8) NOT NULL,
produto_ven varchar(60) NOT NULL,
id_fun integer(2) REFERENCES funcionario(id_fun) NOT NULL
)

CREATE TABLE avaliacao_cliente(
id_avaliacao integer(4) NOT NULL,
id_ser integer(4) NOT NULL,
nota_avaliacao integer(10) NOT NULL,
comentario_avaliacao varchar(100) NOT NULL,
data_avaliacao date(8) NOT NULL,
id_ser integer(4) REFERENCES servico(id_ser) NOT NULL,
id_mec_ava integer(2) NOT NULL,
cnpj_ava integer(14) NOT NULL
)
