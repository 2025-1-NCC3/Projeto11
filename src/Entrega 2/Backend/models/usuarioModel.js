const { DataTypes } = require("sequelize");
const sequelize = require("../config/db");

(async () => {
    const queryInterface = sequelize.getQueryInterface();
    const tableDefinition = await queryInterface.describeTable("usuario");

    console.log("Estrutura da tabela [Usuario]:", tableDefinition);
})();

const UsuarioModel = sequelize.define("Usuario", {
    id_usuario: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    nome: {
        type: DataTypes.STRING,
        allowNull: false
    },
    email: {
        type: DataTypes.STRING,
        allowNull: false
    },
    telefone: {
        type: DataTypes.STRING,
        allowNull: false
    },
    cpf: {
        type: DataTypes.STRING(11),
        allowNull: false,
        unique: true
    },
    data_nascimento: {
        type: DataTypes.DATE,
        allowNull: false
    },
    tipo_usuario: {
        type: DataTypes.STRING,
        allowNull: false
    },
    senha: {
        type: DataTypes.STRING,
        allowNull: false
    }
}, {
    tableName: "usuario",
    timestamps: false, // Se já existir no banco, evita adicionar `createdAt` e `updatedAt`
});

module.exports = UsuarioModel;