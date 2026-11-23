create database patinhas;
use patinhas;

create table Animal(
	idAnimal int AUTO_INCREMENT PRIMARY KEY,
  nome varchar(100) NOT NULL,
  especie varchar(50) NOT NULL,
  raca varchar(100) NOT NULL,
  idadeAproximada int,
  sexo VARCHAR(10) NOT NULL,
  porte VARCHAR(20),
  status VARCHAR(20) NOT NULL DEFAULT 'Disponivel',
  foto VARCHAR(255)
);

create table Endereco(
  idEndereco int AUTO_INCREMENT PRIMARY KEY,
  rua varchar(100) NOT NULL,
  numero varchar(10),
  bairro varchar(100) NOT NULL,
  cidade varchar(100) NOT NULL,
  estado varchar(100) NOT NULL,
  cep char(8) NOT NULL
);

create table Pessoa(
  idPessoa int AUTO_INCREMENT PRIMARY KEY,
  idEndereco int NOT NULL,
  nome varchar(100) NOT NULL,
  cpf char(11) NOT NULL UNIQUE, 
  telefone varchar(15) NOT NULL, 
  email varchar(100) NOT NULL,

  foreign key(idEndereco) references Endereco(idEndereco)
);

create table Cliente(
  idPessoa int PRIMARY KEY,
  dataNascimento date NOT NULL, 
  tipoMoradia varchar(100) NOT NULL,
  
  foreign key(idPessoa) references Pessoa(idPessoa)
);

create table Funcionario(
  idPessoa int PRIMARY KEY,
  cargo varchar(100) NOT NULL,
  matricula int NOT NULL,
  
  foreign key(idPessoa) references Pessoa(idPessoa)
);

CREATE TABLE Usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    idPessoa INT NOT NULL UNIQUE,
    login VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('CLIENTE', 'FUNCIONARIO') NOT NULL,

    FOREIGN KEY (idPessoa)
        REFERENCES Pessoa(idPessoa)
);

create table Adocao(
  idAdocao int AUTO_INCREMENT PRIMARY KEY,
  idAnimal int NOT NULL,
  idCliente int NOT NULL,
  dataAdocao date NOT NULL,

  foreign key(idAnimal) references Animal(idAnimal),
  foreign key(idCliente) references Cliente(idPessoa)
);

CREATE TABLE IF NOT EXISTS Solicitacao (
    idSolicitacao INT AUTO_INCREMENT PRIMARY KEY,
    idCliente INT NOT NULL,
    idAnimal INT NOT NULL,
    dataSolicitacao DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'Pendente',

    FOREIGN KEY (idCliente) REFERENCES Cliente(idPessoa),
    FOREIGN KEY (idAnimal) REFERENCES Animal(idAnimal)
);

-- Animais disponíveis
select *
from animal
where status = 'Disponível';

-- Quantidade de animais disponíveis
select status, count(*) as quantidade
from Animal 
group by status;

-- Quantidade de animais disponíveis por espécie
select especie, count(*) as quantidade
from Animal 
group by especie;

-- Clientes e seu endereço
select
p.nome,
p.cpf,
p.telefone,
p.email,
e.rua,
e.numero,
e.bairro,
e.cidade,
e.estado,
e.cep
from Pessoa p
inner join Cliente c 
  on p.idPessoa = c.idPessoa
inner join Endereco e 
  on p.idEndereco = e.idEndereco;

-- Funcionários por cargo
select
p.nome,
f.matricula,
f.cargo
from Pessoa p
inner join Funcionario f 
  on p.idPessoa = f.idPessoa
order by cargo;

-- Quantidade geral de adoções realizadas
select count(*) as quantidadeGeral
from Adocao;

-- Relação entre clientes e animais
select 
a.nome as animal,
p.nome as cliente,
ad.dataAdocao
from Adocao ad
inner join Animal a 
  on ad.idAnimal = a.idAnimal
inner join Cliente c 
  on ad.idCliente = c.idPessoa
inner join Pessoa p 
  on c.idPessoa = p.idPessoa;
