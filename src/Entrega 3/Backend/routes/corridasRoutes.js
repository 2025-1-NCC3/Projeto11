const express = require('express')
const router = express.Router()
const corridasController = require('../controllers/corridasController')

router.get('/motorista/:idUsuario', corridasController.historicoCorridasMotorista)
router.get('/passageiro/:idUsuario', corridasController.historicoCorridasPassageiro)

router.post('/', corridasController.criarCorrida)

router.put('/:idCorrida', corridasController.atualizarCorrida)

module.exports = router