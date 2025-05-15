const express = require('express')
const router = express.Router()
const solicitacoesController = require('../controllers/solicitacoesController')

router.get('/', solicitacoesController.obterSolicitacoes)
router.get('/pendentes', solicitacoesController.obterSolicitacoesPendentes)

router.post('/', solicitacoesController.criarSolicitacao)

router.put('/:idSolicitacao', solicitacoesController.atualizarSolicitacao)

module.exports = router