const express = require('express')
const router = express.Router()
const avaliacoesController = require('../controllers/avaliacoesController')

router.get('/:idRota', avaliacoesController.obterAvaliacoes)

router.post('/', avaliacoesController.registrarAvaliacao)

module.exports = router