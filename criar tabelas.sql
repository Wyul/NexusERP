create table empresa(
id_mec integer NOT NULL PRIMARY key UNIQUE, 
nom_mec varchar(50) NOT NULL, 
cnpj_mec integer NOT null UNIQUE, 
loc_mec varchar(60) NOT NULL, 
telefone integer NOT NULL
)

create table clientes(
 id_cli integer not null primary key unique,
 nom_cli varchar(50) NOT NULL,
 telefo_cli integer NOT NULL,
 loc_cli varchar(60) NOT NULL,
 sexo varchar(1),
 cnpj_cpf integer NOT null unique,
 incri_cli integer unique,
 email varchar(60)
)

create table funcionarios(
 id_fun integer unique not null primary key,
 nom_fun varchar(50) not null,
 data_fun date unique not null,
 cpf_cnpj_fun integer unique not null,
 loc_fun varchar(50) not null,
 cargo_fun varchar(40) not null,
 id_mec integer references empresa(id_mec) not null
 )

create table servico(
 id_ser integer not null primary key,
 id_veic integer,
 status varchar(60) not null,
 data_enc date not null
)

create table agendamento(
 id_ser integer references servico(id_ser) not null primary key,
 id_fun integer references funcionarios(id_fun) not null,
 servico varchar(100),
 data_entrada date
)

create table estoque(
 id_prod integer primary key not null unique,
 nom_prod varchar(60) not null,
 ref_prod integer not null unique,
 codbarra_prod integer unique,
 ncm_prod integer not null unique,
 id_mec integer references empresa(id_mec) not null
)

create table prod_servico(
 id_prod integer references estoque(id_prod) primary key not null,
 id_ser integer references servico(id_ser) not null,
 valor_produto integer not null,
 quant_produto integer not null
)

create table fornecedor(
 id_for integer unique not null primary key,
 nom_for varchar(60) not null,
 cnpj_for integer unique not null,
 loc_for varchar(40) not null,
 tel_for integer not null,
 insc_for integer unique not null,
 id_mec integer references empresa(id_mec) not null 
)