const { Usuario } = require("../config/db"); // ✅ CERTO

const atualizarFoto = async (req, res) => {
  try {
    const id = req.body.id; // Vindo via form-data
    console.log("📷 ID recebido para atualização de foto:", id);
    const usuario = await Usuario.findByPk(id);

    if (!usuario) {
      console.log("⚠️ Usuário não encontrado com o ID:", id);
      return res.status(404).json({ error: "Usuário não encontrado" });
    }

    usuario.foto = req.file.filename; // ou req.file.path, se preferir o caminho completo
    await usuario.save();

    return res.json({
      mensagem: "Foto atualizada com sucesso!",
      foto: usuario.foto,
    });
  } catch (err) {
    return res
      .status(500)
      .json({ error: "Erro ao salvar foto", detalhe: err.message });
  }
};

module.exports = { atualizarFoto };
