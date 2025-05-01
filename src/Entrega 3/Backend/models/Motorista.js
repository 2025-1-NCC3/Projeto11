const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("motorista");

//     console.log("Estrutura da tabela [Motorista]:", tableDefinition);
// })();

// models/Motorista.js
module.exports = (sequelize, DataTypes) => {
    const Motorista = sequelize.define('Motorista', {
      id_motorista: {
        type: DataTypes.BIGINT,
        primaryKey: true,
        autoIncrement: true,
        field: 'id_motorista'
      },
      cnh: {
        type: DataTypes.STRING(20),
        allowNull: false,
        unique: true
      },
      validade_carteira: {
        type: DataTypes.DATEONLY,
        allowNull: false
      },
      avaliacao_media_motorista: {
        type: DataTypes.DECIMAL(2, 1),
        defaultValue: 0.0,
        allowNull: false
      },
      id_usuario: {
        type: DataTypes.BIGINT,
        allowNull: false,
        references: {
          model: 'usuario',
          key: 'id_usuario'
        }
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
      tableName: 'motorista',
      timestamps: false
    });
  
    Motorista.associate = (models) => {
      Motorista.belongsTo(models.Usuario, {
        foreignKey: 'id_usuario',
        as: 'usuario'
      });
      
      Motorista.hasMany(models.Carro, {
        foreignKey: 'id_motorista',
        as: 'carros'
      });
      
      Motorista.hasMany(models.Corrida, {
        foreignKey: 'id_motorista',
        as: 'corridas'
      });
    };
  
    return Motorista;
  };