const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("carro");

//     console.log("Estrutura da tabela [Carro]:", tableDefinition);
// })();

// models/Carro.js
module.exports = (sequelize, DataTypes) => {
  const Carro = sequelize.define('Carro', {
    id_carro: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      autoIncrement: true,
      field: 'id_carro'
    },
    id_motorista: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'motorista',
        key: 'id_motorista'
      }
    },
    marca: {
      type: DataTypes.STRING(200),
      allowNull: false
    },
    placa: {
      type: DataTypes.STRING(10),
      allowNull: false,
      unique: true
    },
    modelo: {
      type: DataTypes.STRING(200),
      allowNull: false
    },
    cor: {
      type: DataTypes.STRING(100),
      allowNull: false
    },
    ano: {
      type: DataTypes.INTEGER,
      allowNull: false
    }
  }, {
    tableName: 'carro',
    timestamps: false
  });

  Carro.associate = (models) => {
    Carro.belongsTo(models.Motorista, {
      foreignKey: 'id_motorista',
      as: 'motorista'
    });
  };

  return Carro;
};