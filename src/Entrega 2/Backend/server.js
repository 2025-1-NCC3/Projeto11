const express = require('express');
const usuarioRoutes = require('./routes/usuarioRoutes');
const rotasRoutes = require('./routes/rotasRoutes');
const app = express();
// const cors = require('cors');
// const path = require('path');
require('dotenv').config();

// Middleware para processar dados no formato JSON
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Configuração do CORS
// app.use(cors({
//     origin: 'http://localhost:3000', // Permite solicitações do frontend
//     optionsSuccessStatus: 200
// }));

// Porta do servidor
const port = process.env.PORT || 5000;
app.listen(port, () => {
    console.log(`Servidor rodando na porta ${port}`);
});

app.get('/', (req, res) => {
    res.send('Servidor Funcionando')
})

// Configurar o CORS para a rota de login (antes da rota POST)
// app.options('/usuarios/login', cors({
//     origin: 'http://localhost:3000', 
//     methods: ['POST', 'GET', 'DELETE'], 
//     allowedHeaders: ['Content-Type', 'Authorization'] 
// }));

app.use('/usuarios', usuarioRoutes);
app.use('/rotas', rotasRoutes);
// app.use('/uploads', express.static(path.join(__dirname, 'uploads')));