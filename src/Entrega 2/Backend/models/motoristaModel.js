const { DataTypes } = require("sequelize");
const sequelize = require("../config/db");
const UsuarioModel = require("./usuarioModel"); // Importe o UsuarioModel corretamente

const MotoristaModel = sequelize.define(
  "Motorista",
  {
    id_motorista: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: true,
    },
    cnh: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    validade_carteira: {
      type: DataTypes.DATE,
      allowNull: false,
    },
    avaliacao_media_motorista: {
      type: DataTypes.DECIMAL(2, 1),
      defaultValue: 0.0,
      allowNull: true,
    },
    placa: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    cor: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    modelo: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    id_usuario: {
      type: DataTypes.INTEGER,
      allowNull: false,
      unique: true,
      references: {
        model: "usuarios", // nome da tabela referenciada
        key: "id_usuario",
      },
    },
  },
  {
    tableName: "motorista",
    timestamps: false,
  }
);

// Estabelecendo a relação de "pertence a" (belongsTo) entre Motorista e Usuario
MotoristaModel.belongsTo(UsuarioModel, {
  foreignKey: "id_usuario",
  as: "usuario", // Defina a associação como "usuario"
});

module.exports = MotoristaModel;
