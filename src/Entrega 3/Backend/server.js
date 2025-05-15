const express = require("express");
const usuarioRoutes = require("./routes/usuarioRoutes");
const rotasRoutes = require("./routes/rotasRoutes");
const avaliacoesRoutes = require("./routes/avaliacoesRoutes");
const feedbacksRoutes = require("./routes/feedbacksRoutes");
const solicitacoesRoutes = require("./routes/solicitacoesRoutes");
const corridasRoutes = require("./routes/corridasRoutes");
const criptografiaMiddleware = require("./CriptografiaMiddleware");
require("dotenv").config();

const app = express();

// Middleware de criptografia deve vir ANTES de qualquer parsing do body
// app.use(criptografiaMiddleware);

// Não use express.json() aqui, já que estamos lidando com raw body
app.use(express.json()); // ❌ Comentado

// Torna a pasta "uploads" acessível publicamente via URL
const path = require("path"); // já deve estar instalado com o Node.js
app.use("/uploads", express.static(path.join(__dirname, "uploads")));

app.use("/usuarios", usuarioRoutes);
app.use("/rotas", rotasRoutes);
app.use("/corridas", corridasRoutes);
app.use("/solicitacoes", solicitacoesRoutes);
app.use("/feedbacks", feedbacksRoutes);
app.use("/avaliacoes", avaliacoesRoutes);

app.get("/", (req, res) => {
  res.send("Servidor Funcionando");
});

const port = process.env.PORT || 5000;
app.listen(port, () => {
  console.log(`Servidor rodando na porta ${port}`);
});
