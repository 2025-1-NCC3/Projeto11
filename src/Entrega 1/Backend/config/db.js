const { Sequelize } = require("sequelize");
require("dotenv").config();

// Criando conexão com o banco existente
const sequelize = new Sequelize(
    process.env.DB_NAME,
    process.env.DB_USER,
    process.env.DB_PASSWORD,
    {
        host: process.env.DB_HOST,
        dialect: process.env.DB_DIALECT,
        logging: (...msg) => console.log(msg)// Oculta logs desnecessários
    }
);

(async () => {
    try {
        await sequelize.authenticate();
        console.log("✅ Conectado ao banco de dados com sucesso!");
    } catch (error) {
        console.error("❌ Erro ao conectar ao banco:", error);
    }
})();

module.exports = sequelize;