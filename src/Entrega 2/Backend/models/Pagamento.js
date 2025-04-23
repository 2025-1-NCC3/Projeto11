const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("pagamento");

//     console.log("Estrutura da tabela [Pagamento]:", tableDefinition);
// })();

// models/Pagamento.js
module.exports = (sequelize, DataTypes) => {
  const Pagamento = sequelize.define('Pagamento', {
    id_pagamento: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      autoIncrement: true,
      field: 'id_pagamento'
    },
    id_passageiro: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'passageiro',
        key: 'id_passageiro'
      }
    },
    valor: {
      type: DataTypes.FLOAT,
      allowNull: false
    },
    status_pagamento: {
      type: DataTypes.ENUM('Pendente', 'Concluído', 'Falhou'),
      allowNull: false
    },
    metodo_pagamento: {
      type: DataTypes.ENUM('PIX', 'Cartão', 'Dinheiro'),
      allowNull: true
    },
    criado_em: {
      type: DataTypes.DATE,
      defaultValue: DataTypes.NOW
    },
    atualizado_em: {
      type: DataTypes.DATE,
      defaultValue: DataTypes.NOW
    }
  }, {
    tableName: 'pagamento',
    timestamps: false
  });

  Pagamento.associate = (models) => {
    Pagamento.belongsTo(models.Passageiro, {
      foreignKey: 'id_passageiro',
      as: 'passageiro'
    });
    
    Pagamento.hasOne(models.Corrida, {
      foreignKey: 'id_pagamento',
      as: 'corrida'
    });
  };

  return Pagamento;
};