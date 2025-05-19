const { or } = require('sequelize');
const db = require('../config/db')
const axios = require('axios')

const precoPorKm = process.env.DEFAULT_PRICE_PER_KM; // Preço por km, pode ser configurado no .env

exports.historicoCorridasMotorista = async (req, res) => {
    const { idMotorista } = req.params;
    const idMotoristaInt = parseInt(idMotorista, 10);

    try {
        const corridas = await db.Corrida.findAll({
            where: { 
                status_corrida: 'Concluída',
                id_motorista: idMotoristaInt
            },
            include: [
                {
                    model: db.Passageiro,
                    as: 'passageiro'
                },
                {
                    model: db.Motorista,
                    as: 'motorista'
                },
                {
                    model: db.Pagamento,
                    as: 'pagamento'
                },
                {
                    model: db.Rota,
                    as: 'rota'
                }
            ]
        });

        res.status(200).json(corridas);
    } catch (error) {
        console.error("Erro ao obter histórico de corridas:", error);
        res.status(500).json({ error: "Erro ao obter histórico de corridas" });
    }
}

exports.historicoCorridasPassageiro = async (req, res) => {
  const { idPassageiro } = req.params;
  const idPassageiroInt = parseInt(idPassageiro, 10);

  try {
    const corridas = await db.Corrida.findAll({
      where: {
        status_corrida: "Concluída",
        id_passageiro: idPassageiroInt,
      },
      attributes: ["data_corrida", "data_hora_inicio"],
      include: [
        {
          model: db.Rota,
          as: "rota",
          attributes: ["descricao"],
        },
      ],
    });

    // Mapear o resultado para um JSON "achatado"
    const corridasAchatas = corridas.map((corrida) => {
      return {
        data_corrida: corrida.data_corrida,
        data_hora_inicio: corrida.data_hora_inicio,
        descricao: corrida.rota ? corrida.rota.descricao : null,
      };
    });

    res.status(200).json(corridasAchatas);
  } catch (error) {
    console.error("Erro ao obter histórico de corridas:", error);
    res.status(500).json({ error: "Erro ao obter histórico de corridas" });
  }
};

exports.criarCorrida = async (req, res) => {
    const { id_rota, id_solicitacao, id_passageiro, id_motorista, metodo_pagamento } = req.body;

    try {
        // Verifica se a rota existe
        const rota = await db.Rota.findByPk(id_rota);
        if (!rota) {
            return res.status(404).json({ error: "Rota não encontrada" });
        }

        // Verifica se a solicitação existe 
        const solicitacao = await db.SolicitacaoCorrida.findByPk(id_solicitacao);
        if (!solicitacao) {
            return res.status(404).json({ error: "Solicitação não encontrada" });
        }

        // Verifica se o passageiro existe
        const passageiro = await db.Passageiro.findByPk(id_passageiro);
        if (!passageiro) {
            return res.status(404).json({ error: "Passageiro não encontrado" });
        }

        // Verifica se o motorista existe
        const motorista = await db.Motorista.findByPk(id_motorista);
        if (!motorista) {
            return res.status(404).json({ error: "Motorista não encontrado" });
        }

        const precoCorrida = (rota.distancia / 1000) * precoPorKm;

        const novoPagamento = await db.Pagamento.create({
            id_passageiro,
            valor: precoCorrida,
            status_pagamento: 'Pendente',
            metodo_pagamento: metodo_pagamento,
        });

        const novaCorrida = await db.Corrida.create({
            id_rota,
            preco: precoCorrida,
            data_corrida: Date.now(),
            data_hora_inicio: Date.now(),
            status_corrida: 'Em andamento',
            id_pagamento: novoPagamento.id_pagamento,
            id_solicitacao,
            id_passageiro,
            id_motorista
        });

        res.status(201).json(novaCorrida);
    } catch (error) {
        console.error("Erro ao criar corrida:", error);
        res.status(500).json({ error: "Erro ao criar corrida" });
    }
}

exports.atualizarCorrida = async (req, res) => {
    const { idCorrida } = req.params;
    const { status_corrida } = req.body;

    try {
        // Verifica se a corrida existe
        const corrida = await db.Corrida.findByPk(idCorrida);

        if (!corrida) {
            return res.status(404).json({ error: "Corrida não encontrada" });
        }

        const pagamento = await db.Pagamento.findByPk(corrida.id_pagamento);
        
        if (!pagamento) {
            return res.status(404).json({ error: "Pagamento não encontrado" });
        }

        switch (status_corrida) {
            case "Concluida":
                await pagamento.update({
                    status_pagamento: 'Concluído',
                });
                break;
            case "Cancelada":
                await pagamento.update({
                    status_pagamento: 'Falhou',
                });
                break;
            default:
                break;
        }

        await corrida.update({
            status_corrida: status_corrida,
        });

        res.status(200).json(corrida);
    } catch (error) {
        console.error("Erro ao atualizar corrida:", error);
        res.status(500).json({ error: "Erro ao atualizar corrida" });
    }
}