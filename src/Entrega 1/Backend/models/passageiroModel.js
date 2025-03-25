const { DataTypes } = require("sequelize");
const sequelize = require("../config/db");

(async () => {
    const queryInterface = sequelize.getQueryInterface();
    const tableDefinition = await queryInterface.describeTable("passageiro");

    console.log("Estrutura da tabela [Passageiro]:", tableDefinition);
})();

const PassageiroModel = sequelize.define("Passageiro", {
    id_passageiro: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    id_usuario: {
        type: DataTypes.INTEGER,
        allowNull: false,
        foreignKey: true,
        unique: true
    },
    avaliacao_media_passageiro: {
        type: DataTypes.DECIMAL(2, 1),
        defaultValue: 0.0,
        allowNull: true,
    },
}, {
    tableName: "passageiro",
    timestamps: false, // Se já existir no banco, evita adicionar `createdAt` e `updatedAt`
});

module.exports = PassageiroModel;