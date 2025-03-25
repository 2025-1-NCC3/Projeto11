create database saferide; 
use saferide; 
  
create table usuario( 
	id_usuario int auto_increment not null, 
    nome varchar(500) not null, 
    email varchar(500) not null, 
    telefone char(11) not null, 
    cpf int not null unique, 
    data_nascimento date not null, 
    tipo_usuario enum("Passageiro", "Motorista") not null, 
    data_cadastro date not null, 
    senha varchar(100) not null, 
    constraint PK_idusuario primary key(id_usuario) 
); 

create table dispositivo( 
	id_dispositivo int auto_increment not null, 
    tipo_dispositivo varchar(500) not null, 
    localizacao varchar(500), 
    sistema_operacional varchar(100), 
    modelo varchar(200) not null, 
	id_usuario int not null, 
    constraint PK_iddispositivo primary key(id_dispositivo), 
    foreign key(id_usuario) references usuario(id_usuario) 
); 

create table motorista ( 
    id_motorista int auto_increment not null, 
    cnh varchar(20) not null unique, 
    validade_carteira date not null, 
    avaliacao_media_motorista decimal(2,1) not null, 
    id_usuario int not null, 
    constraint PK_idmotorista primary key(id_motorista), 
    foreign key (id_usuario) references usuario(idUsuario) 
); 

create table passageiro ( 
    id_passageiro int auto_increment not null, 
    id_usuario int not null, 
    avaliacao_media_passageiro decimal(2, 1) not null, 
    constraint PK_idpassageiro primary key(id_passageiro), 
    foreign key(id_usuario) references usuario(id_usuario) 
); 

create table carro ( 
    id_carro int auto_increment not null, 
    marca varchar(200) not null, 
    placa varchar(10) not null unique, 
    modelo varchar(200) not null, 
    cor varchar(100) not null, 
    ano year not null, 
    idMotorista int not null, 
    constraint PK_idcarro primary key(id_carro), 
    foreign key(idMotorista) references motorista(idMotorista) 
); 

create table rota ( 
	id_rota int auto_increment not null, 
    descricao varchar(1000) not null, 
    duracao time not null, 
    distancia float not null, 
    constraint PK_idrota primary key(id_rota) 
);

create table solicitacao( 
	id_solicitacao int auto_increment not null,
    id_rota int not null,
    id_passageiro int not null,
    data_hora datetime not null,
    status_solicitacao enum("Pendente", "Aceita", "Cancelada"),
    constraint PK_idsolicitacao primary key(id_solicitacao),
    foreign key(id_rota) references rota(id_rota),
    foreign key(id_passageiro) references passageiro(id_passageiro)
); 

create table pagamento(
	id_pagamento int auto_increment not null,
    id_passageiro int not null,
    valor float not null,
    data_hora datetime not null,
    status_pagamento enum("Pendente", "Concluído, Falhou") not null,
    metodo_pagamento enum("PIX", "Cartão", "Dinheiro"),
    constraint PK_idpagamento primary key(id_pagamento),
    foreign key(id_passageiro) references passageiro(id_passageiro)
);

create table corrida ( 
    id_corrida int auto_increment not null, 
    data_corrida date not null, 
    dataHoraInicio datetime not null, 
    dataHoraFim datetime, 
    preco decimal(10, 2) not null, 
    status_corrida enum("Concluída", "Em andamento", "Cancelada") not null, 
    ponto_partida varchar(500) not null, 
    porto_destino varchar(500) not null, 
    id_motorista int not null, 
    id_passageiro int not null,
    id_solicitacao int not null,
    id_pagamento int not null,
    constraint PK_idcorrida primary key(id_corrida), 
    foreign key(id_motorista) references motorista(id_motorista), 
    foreign key(id_passageiro) references passageiro(id_passageiro),
    foreign key(id_solicitacao) references solicitacao(id_solicitacao),
    foreign key(id_pagamento) references pagamento(id_pagamento)
);    

create table localizacao ( 
    id_localizacao int auto_increment not null, 
    latitude float not null, 
    longitude float not null, 
    registro_data_hora datetime not null, 
    bairro varchar(300) not null, 
    cidade varchar(300) not null, 
    estado varchar(300) not null, 
    pais varchar(250) not null, 
    constraint PK_idlocalizacao primary key(id_localizacao) 
); 

create table avaliacao ( 
    id_avaliacao int auto_increment not null,
    id_passageiro int not null,
    id_motorista int not null,
    id_corrida int not null,
    id_rota int not null,
    nota int check (nota between 1 and 5), 
    data_hora datetime not null,
    constraint PK_idavaliacao primary key(id_avaliacao),
    foreign key(id_passageiro) references passageiro(id_passageiro),
    foreign key(id_motorista) references motorista(id_motorista),
    foreign key(id_corrida) references corrida(id_corrida),
    foreign key(id_rota) references rota(id_rota)
); 

create table areaderisco ( 
    id_areaderisco int auto_increment not null,
    id_localizacao int not null,
    classificacao enum("Alto", "Médio", "Baixo"),
    constraint PK_idareaderisco primary key(id_areaderisco),
    foreign key(id_localizacao) references localizacao(id_localizacao)
);


#tabelas N:N
create table corrida_rota( 
    id_corrida int not null, 
    id_rota int not null, 
	constraint PK_idcorridarota primary key(id_corrida, id_rota), 
    foreign key(id_corrida) references corrida(id_corrida), 
    foreign key(id_rota) references rota(id_rota) 
);

create table rota_localizacao ( 
	id_rota int not null, 
    id_localizacao int not null, 
    constraint PK_idrotalocalizacao primary key(id_rota, id_localizacao), 
    foreign key(id_rota) references rota(id_rota), 
    foreign key(id_localizacao) references localizacao(id_localizacao) 
);

create table motorista_solicitacao(
	id_solicitacao int not null,
    id_motorista int not null,
    constraint PK_idmotoristasolicitacao primary key(id_solicitacao, id_motorista),
    foreign key(id_solicitacao) references solicitacao(id_solicitacao),
    foreign key(id_motorista) references motorista(id_motorista)
);

create table avaliacao_localizacao(
	id_avaliacao int not null,
    id_localizacao int not null,
    constraint PK_idavaliacaolocalizacao primary key(id_avaliacao, id_localizacao),
    foreign key(id_avaliacao) references avaliacao(id_avaliacao),
    foreign key(id_localizacao) references localizacao(id_localizacao)
);