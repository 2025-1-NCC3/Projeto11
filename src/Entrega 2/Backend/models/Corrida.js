const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("corrida");

//     console.log("Estrutura da tabela [Corrida]:", tableDefinition);
// })();

// models/Corrida.js
module.exports = (sequelize, DataTypes) => {
  const Corrida = sequelize.define('Corrida', {
    id_corrida: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      autoIncrement: true,
      field: 'id_corrida'
    },
    id_rota: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'rota',
        key: 'id_rota'
      }
    },
    data_corrida: {
      type: DataTypes.DATEONLY,
      allowNull: false
    },
    data_hora_inicio: {
      type: DataTypes.DATE,
      allowNull: false
    },
    data_hora_fim: {
      type: DataTypes.DATE,
      allowNull: true
    },
    preco: {
      type: DataTypes.DECIMAL(10, 2),
      allowNull: false
    },
    status_corrida: {
      type: DataTypes.ENUM('Concluída', 'Em andamento', 'Cancelada'),
      allowNull: false
    },
    id_motorista: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'motorista',
        key: 'id_motorista'
      }
    },
    id_passageiro: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'passageiro',
        key: 'id_passageiro'
      }
    },
    id_solicitacao: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'solicitacao_corrida',
        key: 'id_solicitacao'
      }
    },
    id_pagamento: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'pagamento',
        key: 'id_pagamento'
      }
    }
  }, {
    tableName: 'corrida',
    timestamps: false
  });

  Corrida.associate = (models) => {
    Corrida.belongsTo(models.Rota, {
      foreignKey: 'id_rota',
      as: 'rota'
    });
    
    Corrida.belongsTo(models.Motorista, {
      foreignKey: 'id_motorista',
      as: 'motorista'
    });
    
    Corrida.belongsTo(models.Passageiro, {
      foreignKey: 'id_passageiro',
      as: 'passageiro'
    });
    
    Corrida.belongsTo(models.SolicitacaoCorrida, {
      foreignKey: 'id_solicitacao',
      as: 'solicitacao'
    });
    
    Corrida.belongsTo(models.Pagamento, {
      foreignKey: 'id_pagamento',
      as: 'pagamento'
    });
    
    Corrida.hasMany(models.Avaliacao, {
      foreignKey: 'id_corrida',
      as: 'avaliacoes'
    });
  };

  return Corrida;
};