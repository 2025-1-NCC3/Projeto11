const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("passageiro");

//     console.log("Estrutura da tabela [Passageiro]:", tableDefinition);
// })();

// models/Passageiro.js
module.exports = (sequelize, DataTypes) => {
    const Passageiro = sequelize.define('Passageiro', {
      id_passageiro: {
        type: DataTypes.BIGINT,
        primaryKey: true,
        autoIncrement: true,
        field: 'id_passageiro'
      },
      id_usuario: {
        type: DataTypes.BIGINT,
        allowNull: false,
        references: {
          model: 'usuario',
          key: 'id_usuario'
        }
      },
      avaliacao_media_passageiro: {
        type: DataTypes.DECIMAL(2, 1),
        allowNull: false
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
      tableName: 'passageiro',
      timestamps: false
    });
  
    Passageiro.associate = (models) => {
      Passageiro.belongsTo(models.Usuario, {
        foreignKey: 'id_usuario',
        as: 'usuario'
      });
      
      Passageiro.hasMany(models.SolicitacaoCorrida, {
        foreignKey: 'id_passageiro',
        as: 'solicitacoes'
      });
      
      Passageiro.hasMany(models.Pagamento, {
        foreignKey: 'id_passageiro',
        as: 'pagamentos'
      });
      
      Passageiro.hasMany(models.Corrida, {
        foreignKey: 'id_passageiro',
        as: 'corridas'
      });
    };
  
    return Passageiro;
  };