const express = require("express");
const router = express.Router();
const usuarioController = require("../controllers/usuarioController");
const { extractToken, authMiddleware } = require("../authMiddleware");
const upload = require("../middlewares/uploadFoto.js");
const { atualizarFoto } = require("../controllers/uploadFotoController");
const Usuario = require("../models/Usuario");

router.get("/", usuarioController.getUsuarios);
router.get("/id/:id", usuarioController.getUsuarioById);
router.get("/cpf/:cpf", usuarioController.getUsuarioByCpf);
router.post("/add", usuarioController.createUsuario);
router.post("/passageiro/login", usuarioController.loginPassageiro);
router.post("/motorista/login", usuarioController.loginMotorista);
router.put("/update", usuarioController.updateUsuario);
router.put("/updateVeiculoMotorista", usuarioController.updateVeiculoMotorista);
router.post("/upload-foto", upload.single("foto"), atualizarFoto);
router.delete("/usuarios/delete", usuarioController.deleteUser);

module.exports = router;
