const { Sequelize } = require("sequelize");
require("dotenv").config();

// Configuração otimizada para o Neon
const sequelize = new Sequelize(process.env.DATABASE_URL, {
  dialect: "postgres",
  dialectOptions: {
    ssl: {
      require: true, // Neon exige SSL
      rejectUnauthorized: false // Ignora erros de certificado (seguro para testes)
    }
  },
  logging: (...msg) => console.log(msg) // Mostra logs das queries SQL
});

// Teste de conexão
(async () => {
  try {
    await sequelize.authenticate();
    console.log("✅ Conectado ao Neon.tech com sucesso!");
  } catch (error) {
    console.error("❌ Erro na conexão:", error);
  }
})();

module.exports = sequelize;