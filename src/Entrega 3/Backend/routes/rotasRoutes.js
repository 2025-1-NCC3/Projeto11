const express = require('express')
const router = express.Router()
const rotasController = require('../controllers/rotasController')

router.get('/trechos/:id_rota', rotasController.obterTrechosPorIdRota)
router.get('/detalhes/:latitude/:longitude', rotasController.obterDetalhesLocal)

router.post('/calcular-rota', rotasController.calcularRota)
// router.get('/rotas/:id', rotasController.obterRotasPorId)

module.exports = router