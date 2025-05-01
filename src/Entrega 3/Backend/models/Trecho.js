const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("trecho");

//     console.log("Estrutura da tabela [Trecho]:", tableDefinition);
// })();

// models/Trecho.js
module.exports = (sequelize, DataTypes) => {
  const Trecho = sequelize.define('Trecho', {
    id_trecho: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      autoIncrement: true,
      field: 'id_trecho'
    },
    id_rota: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'rota',
        key: 'id_rota'
      }
    },
    id_local_partida: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'localizacao',
        key: 'id_localizacao'
      }
    },
    id_local_destino: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'localizacao',
        key: 'id_localizacao'
      }
    },
    polyline: {
      type: DataTypes.STRING(500),
      allowNull: false
    },
    order_number: {
      type: DataTypes.INTEGER,
      allowNull: false
    },
    descricao: {
      type: DataTypes.STRING(1000),
      allowNull: false
    },
    duracao: {
      type: DataTypes.INTEGER,
      allowNull: false
    },
    distancia: {
      type: DataTypes.FLOAT,
      allowNull: false
    }
  }, {
    tableName: 'trecho',
    timestamps: false
  });

  Trecho.associate = (models) => {
    Trecho.belongsTo(models.Rota, {
      foreignKey: 'id_rota',
      as: 'rota'
    });
    
    Trecho.belongsTo(models.Localizacao, {
      foreignKey: 'id_local_partida',
      as: 'local_partida'
    });
    
    Trecho.belongsTo(models.Localizacao, {
      foreignKey: 'id_local_destino',
      as: 'local_destino'
    });
    
    Trecho.hasMany(models.Avaliacao, {
      foreignKey: 'id_trecho',
      as: 'avaliacoes'
    });
  };

  return Trecho;
};