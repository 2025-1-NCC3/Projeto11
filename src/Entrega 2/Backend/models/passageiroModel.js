const { DataTypes } = require("sequelize");
const sequelize = require("../config/db");
const UsuarioModel = require("./usuarioModel"); // Importe o UsuarioModel corretamente

const PassageiroModel = sequelize.define(
  "Passageiro",
  {
    id_passageiro: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: true,
    },
    id_usuario: {
      type: DataTypes.INTEGER,
      allowNull: false,
      foreignKey: true,
      unique: true,
    },
    avaliacao_media_passageiro: {
      type: DataTypes.DECIMAL(2, 1),
      defaultValue: 0.0,
      allowNull: true,
    },
  },
  {
    tableName: "passageiro",
    timestamps: false,
  }
);

// Estabelecendo a relação de "pertence a" (belongsTo) entre Passageiro e Usuario
PassageiroModel.belongsTo(UsuarioModel, {
  foreignKey: "id_usuario",
  as: "usuario", // Defina a associação como "usuario"
});

module.exports = PassageiroModel;
