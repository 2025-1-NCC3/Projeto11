const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("feedback");

//     console.log("Estrutura da tabela [Feedback]:", tableDefinition);
// })();

// models/Feedback.js
module.exports = (sequelize, DataTypes) => {
  const Feedback = sequelize.define('Feedback', {
    id_feedback: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      autoIncrement: true,
      field: 'id_feedback'
    },
    categoria: {
      type: DataTypes.ENUM('Positivo', 'Negativo', 'Neutro'),
      allowNull: false
    },
    descricao: {
      type: DataTypes.STRING(255),
      allowNull: false
    }
  }, {
    tableName: 'feedback',
    timestamps: false
  });

  Feedback.associate = (models) => {
    Feedback.belongsToMany(models.Avaliacao, {
      through: 'AvaliacaoFeedback',
      foreignKey: 'id_feedback',
      otherKey: 'id_avaliacao',
      as: 'avaliacoes'
    });
  };

  return Feedback;
};