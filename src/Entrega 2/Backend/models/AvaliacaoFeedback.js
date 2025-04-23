const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("avaliacao_feedback");

//     console.log("Estrutura da tabela [AvaliacaoFeedback]:", tableDefinition);
// })();

// models/AvaliacaoFeedback.js
module.exports = (sequelize, DataTypes) => {
  const AvaliacaoFeedback = sequelize.define('AvaliacaoFeedback', {
    id_avaliacao: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      allowNull: false,
      references: {
        model: 'avaliacao',
        key: 'id_avaliacao'
      }
    },
    id_feedback: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      allowNull: false,
      references: {
        model: 'feedback',
        key: 'id_feedback'
      }
    }
  }, {
    tableName: 'avaliacao_feedback',
    timestamps: false
  });

  return AvaliacaoFeedback;
};