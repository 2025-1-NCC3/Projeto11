const { DataTypes } = require("sequelize");
const sequelize = require("../config/db");

// (async () => {
//     const queryInterface = sequelize.getQueryInterface();
//     const tableDefinition = await queryInterface.describeTable("usuario");

//     console.log("Estrutura da tabela [Usuario]:", tableDefinition);
// })();

// models/Usuario.js
module.exports = (sequelize, DataTypes) => {
    const Usuario = sequelize.define('Usuario', {
      id_usuario: {
        type: DataTypes.BIGINT,
        primaryKey: true,
        autoIncrement: true,
        field: 'id_usuario'
      },
      nome: {
        type: DataTypes.STRING(500),
        allowNull: false
      },
      email: {
        type: DataTypes.STRING(500),
        allowNull: false,
        unique: true
      },
      telefone: {
        type: DataTypes.CHAR(11),
        allowNull: false
      },
      cpf: {
        type: DataTypes.CHAR(11),
        allowNull: false,
        unique: true
      },
      data_nascimento: {
        type: DataTypes.DATEONLY,
        allowNull: false
      },
      tipo_usuario: {
        type: DataTypes.ENUM('Passageiro', 'Motorista'),
        allowNull: false
      },
      senha: {
        type: DataTypes.STRING(100),
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
      tableName: 'usuario',
      timestamps: false // Já temos os campos personalizados criado_em e atualizado_em
    });
  
    Usuario.associate = (models) => {
      Usuario.hasOne(models.Motorista, {
        foreignKey: 'id_usuario',
        as: 'motorista'
      });
      
      Usuario.hasOne(models.Passageiro, {
        foreignKey: 'id_usuario',
        as: 'passageiro'
      });
      
      Usuario.hasMany(models.Dispositivo, {
        foreignKey: 'id_usuario',
        as: 'dispositivos'
      });
      
      Usuario.hasMany(models.Avaliacao, {
        foreignKey: 'id_usuario',
        as: 'avaliacoes'
      });
    };
  
    return Usuario;
  };