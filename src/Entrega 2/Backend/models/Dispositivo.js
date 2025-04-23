const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("dispositivo");

//     console.log("Estrutura da tabela [Dispositivo]:", tableDefinition);
// })();

// models/Dispositivo.js
module.exports = (sequelize, DataTypes) => {
  const Dispositivo = sequelize.define('Dispositivo', {
    id_dispositivo: {
      type: DataTypes.BIGINT,
      primaryKey: true,
      autoIncrement: true,
      field: 'id_dispositivo'
    },
    id_localizacao: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'localizacao',
        key: 'id_localizacao'
      }
    },
    id_usuario: {
      type: DataTypes.BIGINT,
      allowNull: false,
      references: {
        model: 'usuario',
        key: 'id_usuario'
      }
    },
    tipo_dispositivo: {
      type: DataTypes.STRING(500),
      allowNull: false
    },
    sistema_operacional: {
      type: DataTypes.STRING(100),
      allowNull: true
    },
    modelo: {
      type: DataTypes.STRING(200),
      allowNull: false
    }
  }, {
    tableName: 'dispositivo',
    timestamps: false
  });

  Dispositivo.associate = (models) => {
    Dispositivo.belongsTo(models.Localizacao, {
      foreignKey: 'id_localizacao',
      as: 'localizacao'
    });
    
    Dispositivo.belongsTo(models.Usuario, {
      foreignKey: 'id_usuario',
      as: 'usuario'
    });
  };

  return Dispositivo;
};