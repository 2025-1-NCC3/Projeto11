const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("area_de_risco");

//     console.log("Estrutura da tabela [AreaDeRisco]:", tableDefinition);
// })();

// models/AreaDeRisco.js
module.exports = (sequelize, DataTypes) => {
  const AreaDeRisco = sequelize.define('AreaDeRisco', {
    id_areaderisco: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      autoIncrement: true,
      field: 'id_areaderisco'
    },
    id_localizacao: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'localizacao',
        key: 'id_localizacao'
      }
    },
    classificacao: {
      type: DataTypes.ENUM('Alto', 'Médio', 'Baixo'),
      allowNull: true
    }
  }, {
    tableName: 'area_de_risco',
    timestamps: false
  });

  AreaDeRisco.associate = (models) => {
    AreaDeRisco.belongsTo(models.Localizacao, {
      foreignKey: 'id_localizacao',
      as: 'localizacao'
    });
  };

  return AreaDeRisco;
};