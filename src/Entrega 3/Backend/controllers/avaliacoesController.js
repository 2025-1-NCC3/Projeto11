const db = require('../config/db')

exports.obterAvaliacoes = async (req, res) => {
    const { idRota } = req.params;

    try {
        const avaliacoes = await db.Avaliacao.findAll({
            where: { id_rota: idRota },
            include: [
                {
                    model: db.Rota,
                    as: 'rota',
                },
                {
                    model: db.Usuario,
                    as: 'usuario',  
                },
                {
                    model: db.Feedback,
                    as: 'feedbacks',
                    through: {
                        model: db.AvaliacaoFeedback,
                        as: 'avaliacao_feedback'
                    }
                }                
            ]
        });

        if (!avaliacoes || avaliacoes.length === 0) {
            return res.status(404).json({ message: "Nenhuma avaliação encontrada para esta rota." });
        }

        res.status(200).json(avaliacoes);
    } catch (error) {
        console.error("Erro ao obter avaliações:", error);
        res.status(500).json({ error: "Erro ao obter avaliações" });
    }
}

exports.registrarAvaliacao = async (req, res) => {
    const { id_usuario, id_corrida, id_rota, nota, feedbacks } = req.body;
    
    try {
        const novaAvaliacao = await db.Avaliacao.create({
            id_usuario,
            id_corrida,
            id_rota,
            nota
        });
    
        feedbacks.forEach(async (feedback) => {
            const feedbackExistente = await db.Feedback.findOne({
                where: { id_feedback: feedback.id_feedback }
            });

            if (!feedbackExistente) {
                return res.status(400).json({ error: `Feedback inválido: Feedback com ID ${feedback.id_feedback} não encontrado.` });
            }

            const novaAvaliacaoFeedback = await db.AvaliacaoFeedback.create({
                id_avaliacao: novaAvaliacao.id_avaliacao,
                id_feedback: feedback.id_feedback
            });
        });

        // Adiciona os feedbacks à nova avaliação
        novaAvaliacao.dataValues.feedbacks = feedbacks.map(feedback => ({
            id_feedback: feedback.id_feedback
        }));

        res.status(201).json(novaAvaliacao);
    } catch (error) {
        console.error("Erro ao registrar avaliação:", error);
        res.status(500).json({ error: "Erro ao registrar avaliação: " + error.message });
    }
}