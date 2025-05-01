const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("rota");

//     console.log("Estrutura da tabela [Rota]:", tableDefinition);
// })();

// models/Rota.js
module.exports = (sequelize, DataTypes) => {
  const Rota = sequelize.define('Rota', {
    id_rota: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      autoIncrement: true,
      field: 'id_rota'
    },
    maps_token: {
      type: DataTypes.STRING(1000),
      allowNull: false
    },
    polyline: {
      type: DataTypes.STRING(500),
      allowNull: false
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
    tableName: 'rota',
    timestamps: false
  });

  Rota.associate = (models) => {
    Rota.belongsTo(models.Localizacao, {
      foreignKey: 'id_local_partida',
      as: 'local_partida'
    });
    
    Rota.belongsTo(models.Localizacao, {
      foreignKey: 'id_local_destino',
      as: 'local_destino'
    });
    
    Rota.hasMany(models.Trecho, {
      foreignKey: 'id_rota',
      as: 'trechos'
    });
    
    Rota.hasMany(models.SolicitacaoCorrida, {
      foreignKey: 'id_rota',
      as: 'solicitacoes'
    });
    
    Rota.hasMany(models.Corrida, {
      foreignKey: 'id_rota',
      as: 'corridas'
    });
    
    Rota.hasMany(models.Avaliacao, {
      foreignKey: 'id_rota',
      as: 'avaliacoes'
    });
  };

  return Rota;
};