create database JavaBanks;
use JavaBanks;

create table empresas(
idempresa int primary key auto_increment,
nome varchar(100) not null,
cnpj varchar(14) not null,
email varchar(100) not null,
senha varchar(100) not null,
saldo double default 0.0,
taxas double default 0.0
);

create table clientes(
empresa int,
idcliente int auto_increment PRIMARY KEY,
nome varchar(100) not null,
cpf varchar(11) not null,
email varchar(100) not null,
senha varchar(100) not null,
saldo double default 0.0,
foreign key (empresa) references empresas(idempresa)
);



select * from clientes;
select * from empresas;