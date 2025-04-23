const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("solicitacao_corrida");

//     console.log("Estrutura da tabela [SolicitacaoCorrida]:", tableDefinition);
// })();

// models/SolicitacaoCorrida.js
module.exports = (sequelize, DataTypes) => {
  const SolicitacaoCorrida = sequelize.define('SolicitacaoCorrida', {
    id_solicitacao: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      autoIncrement: true,
      field: 'id_solicitacao'
    },
    id_rota: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'rota',
        key: 'id_rota'
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
    status_solicitacao: {
      type: DataTypes.ENUM('Pendente', 'Aceita', 'Cancelada', 'Em Progresso'),
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
    tableName: 'solicitacao_corrida',
    timestamps: false
  });

  SolicitacaoCorrida.associate = (models) => {
    SolicitacaoCorrida.belongsTo(models.Rota, {
      foreignKey: 'id_rota',
      as: 'rota'
    });
    
    SolicitacaoCorrida.belongsTo(models.Passageiro, {
      foreignKey: 'id_passageiro',
      as: 'passageiro'
    });
    
    SolicitacaoCorrida.hasOne(models.Corrida, {
      foreignKey: 'id_solicitacao',
      as: 'corrida'
    });
  };

  return SolicitacaoCorrida;
};