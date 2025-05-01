const express = require("express");
const router = express.Router();
const usuarioController = require("../controllers/usuarioController");
const { extractToken, authMiddleware } = require("../authMiddleware");

router.get("/", usuarioController.getUsuarios);
router.get("/id/:id", usuarioController.getUsuarioById);
router.get("/cpf/:cpf", usuarioController.getUsuarioByCpf);
router.post("/add", usuarioController.createUsuario);
router.post("/passageiro/login", usuarioController.loginPassageiro);
router.post("/motorista/login", usuarioController.loginMotorista);
router.put("/update", usuarioController.updateUsuario);
router.put("/updateVeiculoMotorista", usuarioController.updateVeiculoMotorista);
router.delete("/delete/:id", usuarioController.deleteUser);

module.exports = router;
