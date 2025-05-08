const express = require("express");
const usuarioRoutes = require("./routes/usuarioRoutes");
const rotasRoutes = require("./routes/rotasRoutes");
const criptografiaMiddleware = require("./CriptografiaMiddleware");
require("dotenv").config();

const app = express();

// Middleware de criptografia deve vir ANTES de qualquer parsing do body
app.use(criptografiaMiddleware);

// Não use express.json() aqui, já que estamos lidando com raw body
// app.use(express.json()); ❌ Comentado

app.use("/usuarios", usuarioRoutes);
app.use("/rotas", rotasRoutes);

app.get("/", (req, res) => {
  res.send("Servidor Funcionando");
});

const port = process.env.PORT || 5000;
app.listen(port, () => {
  console.log(`Servidor rodando na porta ${port}`);
});
