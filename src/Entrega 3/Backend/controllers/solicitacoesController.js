const { where } = require('sequelize');
const db = require('../config/db')
const axios = require('axios')

exports.obterSolicitacoes = async (req, res) => {
    try {
        const solicitacoes = await db.SolicitacaoCorrida.findAll({
            include: [
                {
                    model: db.Passageiro,
                    as: 'passageiro',
                    attributes: ['id_passageiro', 'avaliacao_media_passageiro'],
                    include: [
                        {
                            model: db.Usuario,
                            as: 'usuario',
                            attributes: ['nome', 'email', 'foto']
                        }
                    ]  
                },
                {
                    model: db.Rota,
                    as: 'rota',
                    attributes: ['id_rota', 'descricao', 'duracao', 'distancia'],
                    include: [
                        {
                            model: db.Localizacao,
                            as: 'local_partida',
                            attributes: ['id_localizacao', 'logradouro', 'bairro', 'cidade', 'estado', 'cep', 'pais'],
                        },
                        {
                            model: db.Localizacao,
                            as: 'local_destino',
                            attributes: ['id_localizacao', 'logradouro', 'bairro', 'cidade', 'estado', 'cep', 'pais']
                        }
                    ]
                }
            ]
        });

        const response = solicitacoes.map(solicitacao => {
            return {
                id_solicitacao: solicitacao.id_solicitacao,
                status_solicitacao: solicitacao.status_solicitacao,
                passageiro: {
                    id_passageiro: solicitacao.passageiro.id_passageiro,
                    nome: solicitacao.passageiro.usuario.nome,
                    email: solicitacao.passageiro.usuario.email,
                    foto: solicitacao.passageiro.usuario.foto,
                    avaliacao_media_passageiro: solicitacao.passageiro.avaliacao_media_passageiro
                },
                rota: {
                    id_rota: solicitacao.rota.id_rota,
                    descricao: solicitacao.rota.descricao,
                    duracao: solicitacao.rota.duracao,
                    distancia: solicitacao.rota.distancia,
                    local_partida: {
                        id_localizacao: solicitacao.rota.local_partida.id_localizacao,
                        logradouro: solicitacao.rota.local_partida.logradouro,
                        bairro: solicitacao.rota.local_partida.bairro,
                        cidade: solicitacao.rota.local_partida.cidade,
                        estado: solicitacao.rota.local_partida.estado,
                        cep: solicitacao.rota.local_partida.cep,
                        pais: solicitacao.rota.local_partida.pais
                    },
                    local_destino: {
                        id_localizacao: solicitacao.rota.local_destino.id_localizacao,
                        logradouro: solicitacao.rota.local_destino.logradouro,
                        bairro: solicitacao.rota.local_destino.bairro,
                        cidade: solicitacao.rota.local_destino.cidade,
                        estado: solicitacao.rota.local_destino.estado,
                        cep: solicitacao.rota.local_destino.cep,
                        pais: solicitacao.rota.local_destino.pais
                    }
                }
            }
        });

        res.status(200).json(response);
    } catch (error) {
        console.error("Erro ao obter solicitações:", error);
        res.status(500).json({ error: "Erro ao obter solicitações" });
    }
}

exports.obterSolicitacoesPendentes = async (req, res) => {
    try {
        const solicitacoes = await db.SolicitacaoCorrida.findAll({
            where: {
                status_solicitacao: 'Pendente'
            },
            include: [
                {
                    model: db.Passageiro,
                    as: 'passageiro',
                    attributes: ['id_passageiro', 'avaliacao_media_passageiro'],
                    include: [
                        {
                            model: db.Usuario,
                            as: 'usuario',
                            attributes: ['nome', 'email', 'foto']
                        }
                    ]  
                },
                {
                    model: db.Rota,
                    as: 'rota',
                    attributes: ['id_rota', 'descricao', 'duracao', 'distancia'],
                    include: [
                        {
                            model: db.Localizacao,
                            as: 'local_partida',
                            attributes: ['id_localizacao', 'logradouro', 'bairro', 'cidade', 'estado', 'cep', 'pais'],
                        },
                        {
                            model: db.Localizacao,
                            as: 'local_destino',
                            attributes: ['id_localizacao', 'logradouro', 'bairro', 'cidade', 'estado', 'cep', 'pais']
                        }
                    ]
                }
            ]
        });

        const response = solicitacoes.map(solicitacao => {
            return {
                id_solicitacao: solicitacao.id_solicitacao,
                status_solicitacao: solicitacao.status_solicitacao,
                passageiro: {
                    id_passageiro: solicitacao.passageiro.id_passageiro,
                    nome: solicitacao.passageiro.usuario.nome,
                    email: solicitacao.passageiro.usuario.email,
                    foto: solicitacao.passageiro.usuario.foto,
                    avaliacao_media_passageiro: solicitacao.passageiro.avaliacao_media_passageiro
                },
                rota: {
                    id_rota: solicitacao.rota.id_rota,
                    descricao: solicitacao.rota.descricao,
                    duracao: solicitacao.rota.duracao,
                    distancia: solicitacao.rota.distancia,
                    local_partida: {
                        id_localizacao: solicitacao.rota.local_partida.id_localizacao,
                        logradouro: solicitacao.rota.local_partida.logradouro,
                        bairro: solicitacao.rota.local_partida.bairro,
                        cidade: solicitacao.rota.local_partida.cidade,
                        estado: solicitacao.rota.local_partida.estado,
                        cep: solicitacao.rota.local_partida.cep,
                        pais: solicitacao.rota.local_partida.pais
                    },
                    local_destino: {
                        id_localizacao: solicitacao.rota.local_destino.id_localizacao,
                        logradouro: solicitacao.rota.local_destino.logradouro,
                        bairro: solicitacao.rota.local_destino.bairro,
                        cidade: solicitacao.rota.local_destino.cidade,
                        estado: solicitacao.rota.local_destino.estado,
                        cep: solicitacao.rota.local_destino.cep,
                        pais: solicitacao.rota.local_destino.pais
                    }
                }
            }
        });

        res.status(200).json(response);
    } catch (error) {
        console.error("Erro ao obter solicitações:", error);
        res.status(500).json({ error: "Erro ao obter solicitações" });
    }
}

exports.criarSolicitacao = async (req, res) => {
    const { id_passageiro, id_rota } = req.body;

    try {
        // Verifica se o passageiro existe
        const passageiro = await db.Passageiro.findOne({
            where: { id_usuario: id_passageiro }
        });

        if (!passageiro) {
            return res.status(404).json({ error: "Passageiro não encontrado" });
        }

        const novaSolicitacao = await db.SolicitacaoCorrida.create({
            id_passageiro,
            id_rota,
            status_solicitacao: 'Pendente'
        });

        res.status(201).json(novaSolicitacao);
    } catch (error) {
        console.error("Erro ao criar solicitação:", error);
        res.status(500).json({ error: "Erro ao criar solicitação" });
    }
}

exports.atualizarSolicitacao = async (req, res) => {
    const { idSolicitacao } = req.params;
    const { status_solicitacao } = req.body;

    try {
        const solicitacao = await db.SolicitacaoCorrida.findByPk(idSolicitacao);

        if (!solicitacao) {
            return res.status(404).json({ error: "Solicitação não encontrada" });
        }

        await solicitacao.update({
            status_solicitacao
        });

        res.status(200).json(solicitacao);
    } catch (error) {
        console.error("Erro ao atualizar solicitação:", error);
        res.status(500).json({ error: "Erro ao atualizar solicitação" });
    }
}