const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("avaliacao");

//     console.log("Estrutura da tabela [Avaliacao]:", tableDefinition);
// })();

// models/Avaliacao.js
module.exports = (sequelize, DataTypes) => {
  const Avaliacao = sequelize.define('Avaliacao', {
    id_avaliacao: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      autoIncrement: true,
      field: 'id_avaliacao'
    },
    id_usuario: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'usuario',
        key: 'id_usuario'
      }
    },
    id_corrida: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'corrida',
        key: 'id_corrida'
      }
    },
    id_rota: {
      type: DataTypes.BIGINT,
      allowNull: true,
      references: {
        model: 'rota',
        key: 'id_rota'
      }
    },
    id_trecho: {
      type: DataTypes.BIGINT,
      allowNull: true,
      references: {
        model: 'trecho',
        key: 'id_trecho'
      }
    },
    nota: {
      type: DataTypes.INTEGER,
      allowNull: true,
      validate: {
        min: 1,
        max: 5
      }
    },
    comentario: {
      type: DataTypes.STRING(500),
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
    tableName: 'avaliacao',
    timestamps: false
  });

  Avaliacao.associate = (models) => {
    Avaliacao.belongsTo(models.Usuario, {
      foreignKey: 'id_usuario',
      as: 'usuario'
    });
    
    Avaliacao.belongsTo(models.Corrida, {
      foreignKey: 'id_corrida',
      as: 'corrida'
    });
    
    Avaliacao.belongsTo(models.Rota, {
      foreignKey: 'id_rota',
      as: 'rota'
    });
    
    Avaliacao.belongsTo(models.Trecho, {
      foreignKey: 'id_trecho',
      as: 'trecho'
    });
    
    Avaliacao.belongsToMany(models.Feedback, {
      through: 'AvaliacaoFeedback',
      foreignKey: 'id_avaliacao',
      otherKey: 'id_feedback',
      as: 'feedbacks'
    });
  };

  return Avaliacao;
};