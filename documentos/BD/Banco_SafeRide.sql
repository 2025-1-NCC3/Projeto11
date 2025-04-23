
CREATE TABLE usuario (
    id_usuario BIGSERIAL NOT NULL,
    nome VARCHAR(500) NOT NULL,
    email VARCHAR(500) NOT NULL UNIQUE,
    telefone CHAR(11) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    tipo_usuario VARCHAR(10) NOT NULL CHECK (tipo_usuario IN ('Passageiro', 'Motorista')),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    senha VARCHAR(100) NOT NULL,
    CONSTRAINT PK_idusuario PRIMARY KEY (id_usuario)
);

CREATE OR REPLACE FUNCTION update_atualizado_em_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.atualizado_em = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_usuario_atualizado_em
BEFORE UPDATE ON usuario
FOR EACH ROW
EXECUTE FUNCTION update_atualizado_em_column();

CREATE TABLE motorista (
    id_motorista BIGSERIAL NOT NULL,
    cnh VARCHAR(20) NOT NULL UNIQUE,
    validade_carteira DATE NOT NULL,
    avaliacao_media_motorista DECIMAL(2,1) DEFAULT 0.0,
    id_usuario BIGINT NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT PK_idmotorista PRIMARY KEY (id_motorista),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TRIGGER update_motorista_atualizado_em
BEFORE UPDATE ON motorista
FOR EACH ROW
EXECUTE FUNCTION update_atualizado_em_column();

CREATE TABLE passageiro (
    id_passageiro BIGSERIAL NOT NULL,
    id_usuario BIGINT NOT NULL,
    avaliacao_media_passageiro DECIMAL(2,1) DEFAULT 0.0,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT PK_idpassageiro PRIMARY KEY (id_passageiro),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

CREATE TRIGGER update_passageiro_atualizado_em
BEFORE UPDATE ON passageiro
FOR EACH ROW
EXECUTE FUNCTION update_atualizado_em_column();


CREATE TABLE localizacao (
    id_localizacao BIGSERIAL NOT NULL,
    place_id VARCHAR(255) NOT NULL,
    latitude DECIMAL(17,15) NOT NULL,
    longitude DECIMAL(18,15) NOT NULL,
    logradouro VARCHAR(255) NOT NULL,
    bairro VARCHAR(255) NOT NULL,
    cidade VARCHAR(255) NOT NULL,
    estado VARCHAR(255) NOT NULL,
    cep CHAR(8) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    CONSTRAINT PK_idlocalizacao PRIMARY KEY (id_localizacao)
);


CREATE TABLE dispositivo (
    id_dispositivo BIGSERIAL NOT NULL,
    id_localizacao BIGINT NOT NULL,
    id_usuario BIGINT NOT NULL,
    tipo_dispositivo VARCHAR(500) NOT NULL,
    sistema_operacional VARCHAR(100),
    modelo VARCHAR(200) NOT NULL,
    CONSTRAINT PK_id_dispositivo PRIMARY KEY (id_dispositivo),
    FOREIGN KEY (id_localizacao) REFERENCES localizacao (id_localizacao),
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE CASCADE
);


CREATE TABLE carro (
    id_carro BIGSERIAL NOT NULL,
    id_motorista BIGINT NOT NULL,
    marca VARCHAR(200) NOT NULL,
    placa VARCHAR(10) NOT NULL UNIQUE,
    modelo VARCHAR(200) NOT NULL,
    cor VARCHAR(100) NOT NULL,
    ano INTEGER NOT NULL,
    CONSTRAINT PK_idcarro PRIMARY KEY (id_carro),
    FOREIGN KEY (id_motorista) REFERENCES motorista(id_motorista) ON DELETE CASCADE
);


CREATE TABLE rota (
    id_rota BIGSERIAL NOT NULL,
    maps_token VARCHAR(1000) NOT NULL, 
    polyline TEXT NOT NULL, 
    id_local_partida BIGINT NOT NULL,
    id_local_destino BIGINT NOT NULL,
    descricao VARCHAR(1000) NOT NULL,
    duracao INTEGER NOT NULL, 
    distancia REAL NOT NULL,
    CONSTRAINT PK_idrota PRIMARY KEY (id_rota),
    FOREIGN KEY (id_local_partida) REFERENCES localizacao(id_localizacao),
    FOREIGN KEY (id_local_destino) REFERENCES localizacao(id_localizacao)
);

CREATE TABLE trecho (
    id_trecho BIGSERIAL NOT NULL,
    id_rota BIGINT NOT NULL,
    id_local_partida BIGINT NOT NULL,
    id_local_destino BIGINT NOT NULL,
    polyline TEXT NOT NULL,
    order_number INTEGER NOT NULL,
    descricao VARCHAR(1000) NOT NULL,
    duracao INTEGER NOT NULL, 
    distancia REAL NOT NULL,
    CONSTRAINT PK_id_trecho PRIMARY KEY (id_trecho),
    FOREIGN KEY (id_rota) REFERENCES rota (id_rota),
    FOREIGN KEY (id_local_partida) REFERENCES localizacao(id_localizacao),
    FOREIGN KEY (id_local_destino) REFERENCES localizacao(id_localizacao)
);

CREATE TABLE solicitacao_corrida (
    id_solicitacao BIGSERIAL NOT NULL,
    id_rota BIGINT NOT NULL,
    id_passageiro BIGINT NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status_solicitacao VARCHAR(20) CHECK (status_solicitacao IN ('Pendente', 'Aceita', 'Cancelada', 'Em Progresso')),
    CONSTRAINT PK_idsolicitacao PRIMARY KEY (id_solicitacao),
    FOREIGN KEY (id_rota) REFERENCES rota(id_rota),
    FOREIGN KEY (id_passageiro) REFERENCES passageiro(id_passageiro) 
);

CREATE TRIGGER update_solicitacao_corrida_atualizado_em
BEFORE UPDATE ON solicitacao_corrida
FOR EACH ROW
EXECUTE FUNCTION update_atualizado_em_column();

CREATE TABLE pagamento (
    id_pagamento BIGSERIAL NOT NULL,
    id_passageiro BIGINT NOT NULL,
    valor REAL NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status_pagamento VARCHAR(20) NOT NULL CHECK (status_pagamento IN ('Pendente', 'Concluído', 'Falhou')),
    metodo_pagamento VARCHAR(20) CHECK (metodo_pagamento IN ('PIX', 'Cartão', 'Dinheiro')),
    CONSTRAINT PK_idpagamento PRIMARY KEY (id_pagamento),
    FOREIGN KEY (id_passageiro) REFERENCES passageiro(id_passageiro)
);

CREATE TRIGGER update_pagamento_atualizado_em
BEFORE UPDATE ON pagamento
FOR EACH ROW
EXECUTE FUNCTION update_atualizado_em_column();

CREATE TABLE corrida (
    id_corrida BIGSERIAL NOT NULL,
    id_rota BIGINT NOT NULL,
    data_corrida DATE NOT NULL,
    data_hora_inicio TIMESTAMP NOT NULL,
    data_hora_fim TIMESTAMP,
    preco DECIMAL(10,2) NOT NULL,
    status_corrida VARCHAR(20) NOT NULL CHECK (status_corrida IN ('Concluída', 'Em andamento', 'Cancelada')),
    id_motorista BIGINT NOT NULL,
    id_passageiro BIGINT NOT NULL,
    id_solicitacao BIGINT NOT NULL,
    id_pagamento BIGINT NOT NULL,
    CONSTRAINT PK_idcorrida PRIMARY KEY (id_corrida),
    FOREIGN KEY (id_motorista) REFERENCES motorista (id_motorista),
    FOREIGN KEY (id_passageiro) REFERENCES passageiro (id_passageiro),
    FOREIGN KEY (id_solicitacao) REFERENCES solicitacao_corrida (id_solicitacao),
    FOREIGN KEY (id_pagamento) REFERENCES pagamento (id_pagamento),
    FOREIGN KEY (id_rota) REFERENCES rota (id_rota)
);

CREATE TABLE avaliacao (
    id_avaliacao BIGSERIAL NOT NULL,
    id_usuario BIGINT NOT NULL,
    id_corrida BIGINT NOT NULL,
    id_rota BIGINT,
    id_trecho BIGINT,
    nota INTEGER CHECK (nota BETWEEN 1 AND 5),
    comentario VARCHAR(500),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT PK_idavaliacao PRIMARY KEY (id_avaliacao),
    CONSTRAINT CHK_avaliacao_tipo CHECK (
        (id_rota IS NOT NULL AND id_trecho IS NULL) OR 
        (id_rota IS NULL AND id_trecho IS NOT NULL)
    ),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_corrida) REFERENCES corrida(id_corrida),
    FOREIGN KEY (id_rota) REFERENCES rota(id_rota),
    FOREIGN KEY (id_trecho) REFERENCES trecho(id_trecho)
);

CREATE TRIGGER update_avaliacao_atualizado_em
BEFORE UPDATE ON avaliacao
FOR EACH ROW
EXECUTE FUNCTION update_atualizado_em_column();

CREATE TABLE feedback (
    id_feedback BIGSERIAL NOT NULL,
    categoria VARCHAR(10) NOT NULL CHECK (categoria IN ('Positivo', 'Negativo', 'Neutro')),
    descricao VARCHAR(255) NOT NULL,
    CONSTRAINT PK_idfeedback PRIMARY KEY (id_feedback)
);

CREATE TABLE avaliacao_feedback (
    id_avaliacao BIGINT NOT NULL,
    id_feedback BIGINT NOT NULL,
    CONSTRAINT PK_idavaliacaofeedback PRIMARY KEY (id_avaliacao, id_feedback),
    FOREIGN KEY (id_avaliacao) REFERENCES avaliacao(id_avaliacao),
    FOREIGN KEY (id_feedback) REFERENCES feedback(id_feedback)
);

CREATE TABLE area_de_risco (
    id_areaderisco BIGSERIAL NOT NULL,
    id_localizacao BIGINT NOT NULL,
    classificacao VARCHAR(10) CHECK (classificacao IN ('Alto', 'Médio', 'Baixo')),
    CONSTRAINT PK_idareaderisco PRIMARY KEY (id_areaderisco),
    FOREIGN KEY (id_localizacao) REFERENCES localizacao(id_localizacao)
);