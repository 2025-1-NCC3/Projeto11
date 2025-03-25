const express = require('express')
const router = express.Router()
const usuarioController = require('../controllers/usuarioController')
const { extractToken, authMiddleware } = require('../authMiddleware');

router.get('/', usuarioController.getUsuarios)
router.get('/id/:id', usuarioController.getUsuarioById)
router.get('/cpf/:cpf', usuarioController.getUsuarioByCpf)
router.get('/data', extractToken, authMiddleware, usuarioController.getNomeUsuario);
router.get('/pagina-usuario', authMiddleware, (req, res) => {
    // Lógica para lidar com a requisição GET para /pagina-usuario
    res.send('Página do usuário'); 

});
router.post('/add', usuarioController.createUsuario)
router.post('/login', usuarioController.loginUser);
router.delete('/delete', usuarioController.deleteUser)

module.exports = router