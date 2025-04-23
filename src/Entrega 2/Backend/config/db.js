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
        logging: (...msg) => console.log(msg)
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

const db = {
    sequelize,
    Sequelize,
    Usuario: require('../models/Usuario')(sequelize, Sequelize),
    Motorista: require('../models/Motorista')(sequelize, Sequelize),
    Passageiro: require('../models/Passageiro')(sequelize, Sequelize),
    Localizacao: require('../models/Localizacao')(sequelize, Sequelize),
    Dispositivo: require('../models/Dispositivo')(sequelize, Sequelize),
    Carro: require('../models/Carro')(sequelize, Sequelize),
    Rota: require('../models/Rota')(sequelize, Sequelize),
    Trecho: require('../models/Trecho')(sequelize, Sequelize),
    SolicitacaoCorrida: require('../models/SolicitacaoCorrida')(sequelize, Sequelize),
    Pagamento: require('../models/Pagamento')(sequelize, Sequelize),
    Corrida: require('../models/Corrida')(sequelize, Sequelize),
    Avaliacao: require('../models/Avaliacao')(sequelize, Sequelize),
    Feedback: require('../models/Feedback')(sequelize, Sequelize),
    AvaliacaoFeedback: require('../models/AvaliacaoFeedback')(sequelize, Sequelize),
    AreaDeRisco: require('../models/AvaliacaoFeedback')(sequelize, Sequelize)
};

Object.keys(db).forEach(modelName => {
    if (db[modelName].associate) {
      db[modelName].associate(db);
    }

    // (async () => {
    //     const queryInterface = sequelize.getQueryInterface();
    //     const tableDefinition = await queryInterface.describeTable(db[modelName].tableName);
    //     console.log(`Estrutura da tabela [${modelName}]:`, tableDefinition);
    
    // })();
  });

module.exports = db;
module.exports.sequelize = sequelize;