const { DataTypes } = require("sequelize");
const sequelize = require("../config/db");

(async () => {
    const queryInterface = sequelize.getQueryInterface();
    const tableDefinition = await queryInterface.describeTable("motorista");

    console.log("Estrutura da tabela [Motorista]:", tableDefinition);
})();

const MotoristaModel = sequelize.define("Motorista", {
    id_motorista: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true
    },
    cnh: {
        type: DataTypes.STRING,
        allowNull: false
    },
    validade_carteira: {
        type: DataTypes.DATE,
        allowNull: false
    },
    avaliacao_media_motorista: {
        type: DataTypes.DECIMAL(2, 1),
        defaultValue: 0.0,
        allowNull: true,
    },
    id_usuario: {
        type: DataTypes.INTEGER,
        allowNull: false,
        foreignKey: true,
        unique: true
    },
}, {
    tableName: "motorista",
    timestamps: false, // Se já existir no banco, evita adicionar `createdAt` e `updatedAt`
});

module.exports = MotoristaModel;