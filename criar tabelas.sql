create table mecanica(
id_mec integer(2) NOT NULL PRIMARY key UNIQUE, 
nom_mec varchar(50) NOT NULL, 
cnpj_mec integer(14) NOT null UNIQUE, 
loc_mec varchar(60) NOT NULL, 
telefone integer(13) NOT NULL
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
 cnpj_mec integer(2) references cliente(cnpj_mec) not null
)

create table funcionarios(
 id_fun integer(2) unique not null primary key,
 nom_fun varchar(50) not null,
 data_fun date(8) unique not null,
 cpf_cnpj_fun integer(14) unique not null,
 loc_fun varchar(50) not null,
 cargo_fun varchar(40) not null,
 id_mec integer(2) references mecanica(id_mec) not null
 )

create table servico(
 id_ser integer(4) not null primary key,
 id_veic integer(4),
 status varchar(60) not null,
 data_enc date(8) not null
)

create table agendamento(
 id_ser integer(4) references servico(id_ser) not null primary key,
 id_fun integer(2) references funcionarios(id_fun) not null,
 servico varchar(100),
 data_entrada date(8),
 id_cli integer(2) references cliente(id_cli) not null
)

create table estoque(
 id_prod integer(2) primary key not null unique,
 nom_prod varchar(60) not null,
 ref_prod integer(5) not null unique,
 codbarra_prod integer(13) unique,
 ncm_prod integer(8) not null unique,
 id_mec integer(2) references mecanica(id_mec) not null,
 cnpj_mec integer(14) references mecanica(cnpj_mec)
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
id_tran integer(4) primary key not null unique,
cnpj_tran integer(14) not null unique,
ie_tran integer(9) unique,
cep_tran integer(10) not null,
nom_tran integer(10) not null,
id_prod integer(2) references estoque(id_prod) primary key not null,
id_forn integer(2) references fornecedor(id_forn) primary key not null
)
