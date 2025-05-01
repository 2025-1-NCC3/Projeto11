const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("localizacao");

//     console.log("Estrutura da tabela [Localizacao]:", tableDefinition);
// })();

// models/Localizacao.js
module.exports = (sequelize, DataTypes) => {
  const Localizacao = sequelize.define('Localizacao', {
    id_localizacao: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      autoIncrement: true,
      field: 'id_localizacao'
    },
    place_id: {
      type: DataTypes.STRING(300),
      allowNull: false
    },
    latitude: {
      type: DataTypes.DECIMAL(10, 8),
      allowNull: false
    },
    longitude: {
      type: DataTypes.DECIMAL(11, 8),
      allowNull: false
    },
    logradouro: {
      type: DataTypes.STRING(300),
      allowNull: false
    },
    bairro: {
      type: DataTypes.STRING(300),
      allowNull: false
    },
    cidade: {
      type: DataTypes.STRING(300),
      allowNull: false
    },
    estado: {
      type: DataTypes.STRING(300),
      allowNull: false
    },
    cep: {
      type: DataTypes.CHAR(8),
      allowNull: false
    },
    pais: {
      type: DataTypes.STRING(250),
      allowNull: false
    }
  }, {
    tableName: 'localizacao',
    timestamps: false
  });

  Localizacao.associate = (models) => {
    Localizacao.hasMany(models.Dispositivo, {
      foreignKey: 'id_localizacao',
      as: 'dispositivos'
    });
    
    Localizacao.hasMany(models.Rota, {
      foreignKey: 'id_local_partida',
      as: 'rotasPartida'
    });
    
    Localizacao.hasMany(models.Rota, {
      foreignKey: 'id_local_destino',
      as: 'rotasDestino'
    });
    
    Localizacao.hasMany(models.Trecho, {
      foreignKey: 'id_local_partida',
      as: 'trechosPartida'
    });
    
    Localizacao.hasMany(models.Trecho, {
      foreignKey: 'id_local_destino',
      as: 'trechosDestino'
    });
    
    Localizacao.hasMany(models.AreaDeRisco, {
      foreignKey: 'id_localizacao',
      as: 'areasDeRisco'
    });
  };

  return Localizacao;
};