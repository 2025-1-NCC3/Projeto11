const db = require("../config/db");
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const Usuario = require("../models/Usuario");
const Motorista = require("../models/Motorista");
const Passageiro = require("../models/Passageiro");
const { descriptografar, criptografar } = require("../utils/descriptografar");

exports.createUsuario = async (req, res) => {
  const {
    nome,
    email,
    telefone,
    cpf,
    data_nascimento,
    tipo_usuario,
    senha,
    cnh,
    validade_carteira,
    placa,
    cor,
    modelo,
  } = req.body;

  console.log("Dados recebidos:");
  console.log("Nome:", nome);
  console.log("Email:", email);
  console.log("Telefone:", telefone);
  console.log("CPF:", cpf);
  console.log("Data Nascimento:", data_nascimento);
  console.log("Tipo de Usuário:", tipo_usuario);
  console.log("Senha:", senha);

  // Conversão da data de nascimento
  let dataNascimentoDate;
  try {
    // Validar formato da data
    if (!/^\d{4}-\d{2}-\d{2}$/.test(data_nascimento)) {
      console.error("Formato de data inválido:", data_nascimento);
      return res
        .status(400)
        .json({ message: "Formato de data inválido. Use YYYY-MM-DD." });
    }

    // Converter para Date
    dataNascimentoDate = new Date(data_nascimento);
    if (isNaN(dataNascimentoDate.getTime())) {
      console.error("Data inválida após conversão:", data_nascimento);
      return res.status(400).json({ message: "Data de nascimento inválida." });
    }
  } catch (error) {
    console.error("Erro ao processar data de nascimento:", error);
    return res
      .status(400)
      .json({ message: "Erro ao processar data de nascimento." });
  }

  // Validação dos campos obrigatórios
  if (
    !tipo_usuario ||
    !nome ||
    !email ||
    !telefone ||
    !cpf ||
    !data_nascimento ||
    !senha
  ) {
    return res
      .status(400)
      .json({ message: "Todos os campos obrigatórios devem ser preenchidos." });
  }

  const tipoUsuarioLower = tipo_usuario;

  // Validações específicas para motorista
  if (tipoUsuarioLower === "Motorista") {
    if (!cnh || !validade_carteira || !placa || !cor || !modelo) {
      return res.status(400).json({
        message: "Campos obrigatórios para o motorista devem ser preenchidos.",
      });
    }
  }

  // Validação do tipo de usuário
  if (tipoUsuarioLower !== "Motorista" && tipoUsuarioLower !== "Passageiro") {
    return res.status(400).json({ message: "Tipo de usuário inválido." });
  }

  // Validação do CPF
  if (cpf.toString().length !== 11) {
    return res.status(400).json({ message: "O CPF deve conter 11 dígitos." });
  }

  // Validação do telefone
  if (telefone.length < 10 || telefone.length > 11) {
    return res
      .status(400)
      .json({ message: "O telefone deve conter entre 10 e 11 dígitos." });
  }

  try {
    // Verifica se usuário já existe
    const usuarioExistente = await db.Usuario.findOne({
      where: {
        cpf: cpf.toString(),
        tipo_usuario: tipoUsuarioLower,
      },
    });

    if (usuarioExistente) {
      return res
        .status(400)
        .json({ message: "CPF já cadastrado como: " + tipoUsuarioLower });
    }

    // Criptografa a senha
    const hashedPassword = await bcrypt.hash(senha, 10);

    // Cria o novo usuário
    const newUsuario = await db.Usuario.create({
      nome,
      email,
      telefone,
      cpf: cpf.trim(),
      data_nascimento: dataNascimentoDate,
      tipo_usuario: tipoUsuarioLower,
      senha: hashedPassword,
    });

    if (tipoUsuarioLower === "Motorista") {
      await createUsuarioMotorista({
        cnh,
        validade_carteira,
        placa,
        cor,
        modelo,
        id_usuario: newUsuario.id_usuario,
      });
    }

    if (tipoUsuarioLower === "Passageiro") {
      await createUsuarioPassageiro(newUsuario.id_usuario);
    }

    res.status(200).json({
      message: "Usuário " + tipoUsuarioLower + " cadastrado com sucesso!",
    });
  } catch (error) {
    console.error("Erro ao inserir usuário:", error);
    return res.status(500).json({
      message: "Erro ao cadastrar o usuário. Tente novamente mais tarde.",
      error: error.message,
    });
  }
};

async function createUsuarioPassageiro(id_usuario) {
  await db.Passageiro.create({
    id_usuario: id_usuario,
  });
}

async function createUsuarioMotorista(usuario) {
  try {
    const newMotorista = await db.Motorista.create({
      cnh: usuario.cnh,
      validade_carteira: usuario.validade_carteira,
      placa: usuario.placa,
      cor: usuario.cor,
      modelo: usuario.modelo,
      id_usuario: usuario.id_usuario,
    });

    if (!newMotorista) {
      console.log("Erro ao cadastrar novo motorista");
    }
  } catch (exception) {
    console.error("Erro ao criar motorista:", exception);
  }
}

exports.getUsuarios = async (req, res) => {
  try {
    const usuarios = await db.Usuario.findAll();
    res.status(200).json(usuarios);
  } catch (error) {
    console.error("Erro ao obter usuários:", error);
    res.status(500).json({ message: "Erro ao obter usuários" });
  }
};

exports.getUsuarioById = async (req, res) => {
  const { id } = req.params;

  try {
    const usuario = await db.Usuario.findByPk(id);

    if (usuario) {
      return res.status(200).json(usuario);
    }
    res.status(404).json({ message: "Usuário não encontrado" });
  } catch (error) {
    console.error("Erro ao obter usuário por ID:", error);
    res.status(500).json({ message: "Erro ao obter usuário por ID" });
  }
};

exports.getUsuarioByCpf = async (req, res) => {
  const { cpf } = req.params;

  try {
    const usuario = await db.Usuario.findOne({ where: { cpf: cpf } });

    if (usuario) {
      return res.status(200).json(usuario);
    }
    res.status(404).json({ message: "Usuário não encontrado" });
  } catch (error) {
    console.error("Erro ao obter usuário por ID:", error);
    res.status(500).json({ message: "Erro ao obter usuário por ID" });
  }
};

exports.updateUsuario = async (req, res) => {
  const { id_usuario, cpf, nome, email, telefone, senha } = req.body;

  if (!cpf || !id_usuario) {
    return res.status(400).json({ message: "CPF é obrigatório" });
  }

  try {
    const usuario = await db.Usuario.findOne({
      where: { id_usuario },
      limit: 1,
    });

    if (!usuario) {
      return res.status(404).json({ message: "Usuário não encontrado" });
    }

    if (nome) {
      usuario.nome = nome;
    }

    if (email) {
      usuario.email = email;
    }

    if (telefone) {
      usuario.telefone = telefone;
    }

    // if (senha) {
    //     const samePassword = bcrypt.compare(senha, usuario.senha);

    //     if (!samePassword) {
    //         bcrypt.hash(senha, 10, async (err, hashedPassword) => {
    //             if (err) {
    //                 return res.status(500).json({ message: 'Erro ao criar a senha. Tente novamente.' });
    //             }

    //             usuario.senha = hashedPassword;
    //         });
    //     }
    // }

    await usuario.save();

    return res.status(200).json({ message: "Usuário atualizado com sucesso" });
  } catch (error) {
    console.error("Erro ao atualizar usuário:", error);
    return res.status(500).json({ message: "Erro ao atualizar usuário" });
  }
};

exports.updateVeiculoMotorista = async (req, res) => {
  const { id_motorista, modelo, cor, placa, cnh, validade_carteira } = req.body;

  if (!id_motorista) {
    return res.status(400).json({ message: "ID do motorista é obrigatório" });
  }

  try {
    const motorista = await db.Motorista.findOne({
      where: { id_motorista },
      limit: 1,
    });

    if (!motorista) {
      return res.status(404).json({ message: "Motorista não encontrado" });
    }

    // Atualizar somente os campos enviados
    if (modelo) motorista.modelo = modelo;
    if (cor) motorista.cor = cor;
    if (placa) motorista.placa = placa;
    if (cnh) motorista.cnh = cnh;
    if (validade_carteira) motorista.validade_carteira = validade_carteira;

    await motorista.save();

    return res
      .status(200)
      .json({ message: "Dados do veículo atualizados com sucesso" });
  } catch (error) {
    console.error("Erro ao atualizar dados do veículo:", error);
    return res
      .status(500)
      .json({ message: "Erro ao atualizar dados do veículo" });
  }
};

exports.deleteUser = async (req, res) => {
  const { id } = req.body;

  // Verifica se o ID foi enviado no corpo
  if (!id) {
    console.log("ID não fornecido no corpo da requisição."); // Log de erro
    return res.status(400).json({ message: "ID é obrigatório" });
  }

  // Verifica se o ID é um número válido
  if (isNaN(id)) {
    console.log(`ID fornecido é inválido: ${id}`); // Log de erro
    return res.status(400).json({ message: "ID inválido" });
  }

  const t = await db.transaction(); // Inicia uma transação

  try {
    console.log(`Tentando buscar usuário com ID: ${id}`); // Log de progresso
    const usuario = await db.Usuario.findOne({ where: { id }, transaction: t });

    if (!usuario) {
      console.log(`Usuário não encontrado com ID: ${id}`); // Log de erro
      await t.rollback();
      return res.status(404).json({ message: "Usuário não encontrado" });
    }

    console.log(`Usuário encontrado: ${usuario.nome}`); // Log de sucesso

    // Se o usuário for um motorista, deleta os dados do motorista
    if (usuario.tipo_usuario === "Motorista") {
      console.log(`Deletando dados do motorista com ID: ${id}`);
      await db.Motorista.destroy({
        where: { id_usuario: id },
        transaction: t, // Associado à transação
      });
    }

    // Se o usuário for um passageiro, deleta os dados do passageiro
    if (usuario.tipo_usuario === "Passageiro") {
      console.log(`Deletando dados do passageiro com ID: ${id}`);
      await db.Passageiro.destroy({
        where: { id_usuario: id },
        transaction: t,
      });
    }

    // Deleta o usuário da tabela de usuários
    console.log(`Deletando usuário com ID: ${id}`);
    await db.Usuario.destroy({
      where: { id },
      transaction: t,
    });

    await t.commit(); // Confirma a transação
    console.log(`Usuário com ID: ${id} deletado com sucesso.`); // Log de sucesso

    return res.status(204).send(); // No Content - Sucesso sem conteúdo
  } catch (error) {
    await t.rollback(); // Em caso de erro, faz rollback
    console.error("Erro ao excluir usuário:", error); // Log de erro

    return res.status(500).json({
      message: "Erro interno ao tentar excluir o usuário",
      error: error.message, // Mensagem de erro mais detalhada para debug
    });
  }
};


exports.loginPassageiro = async (req, res) => {
  console.log("🔐 [PASSAGEIRO] Iniciando login...");
  const { email, senha } = req.body;

  if (!email || !senha) {
    console.log("❌ [PASSAGEIRO] Email ou senha não fornecidos");
    return res.status(400).json({ message: "Email e senha são obrigatórios." });
  }

  try {
    const usuario = await db.Usuario.findOne({ where: { email: email } });

    if (!usuario) {
      console.log("❌ [PASSAGEIRO] Usuário não encontrado");
      return res.status(404).json({ message: "Usuário não encontrado." });
    }

    const motorista = await db.Motorista.findOne({
      where: { id_usuario: usuario.id_usuario },
    });

    if (motorista) {
      console.log("❌ [PASSAGEIRO] Usuário é um motorista");
      return res
        .status(403)
        .json({ message: "Este usuário não é um passageiro." });
    }

    const senhaValida = await bcrypt.compare(senha, usuario.senha);
    if (!senhaValida) {
      console.log("❌ [PASSAGEIRO] Senha incorreta");
      return res.status(401).json({ message: "Credenciais inválidas." });
    }

    // Prepara os dados para retorno
    const usuarioParaFront = {
      id_usuario: usuario.id_usuario,
      nome: usuario.nome,
      email: usuario.email,
      cpf: usuario.cpf || null,
      tipo_usuario: usuario.tipo_usuario,
      telefone: usuario.telefone || null,
      data_nascimento: usuario.data_nascimento
        ? new Date(usuario.data_nascimento).toISOString().split("T")[0]
        : null,
    };

    const token = generateToken(usuario);
    console.log("✅ [PASSAGEIRO] Login bem-sucedido!");

    return res.status(200).json({
      message: "Login bem-sucedido!",
      usuario: usuarioParaFront,
      token: token,
    });
  } catch (err) {
    console.error("🔥 [PASSAGEIRO] Erro no login:", err);
    return res.status(500).json({
      message: "Erro interno no servidor.",
      error: err.message,
    });
  }
};

exports.loginMotorista = async (req, res) => {
  console.log("🔐 [MOTORISTA] Iniciando login...");
  const { email, senha } = req.body;
  console.log("📨 [MOTORISTA] Dados recebidos:", req.body);

  try {
    const usuario = await db.Usuario.findOne({ where: { email: email } });

    if (!usuario) {
      console.log("❌ [MOTORISTA] Usuário não encontrado");
      return res.status(404).json({ message: "Usuário não encontrado." });
    }

    const motorista = await db.Motorista.findOne({
      where: { id_usuario: usuario.id_usuario },
    });

    if (!motorista) {
      console.log("❌ [MOTORISTA] Este usuário não é um motorista");
      return res
        .status(403)
        .json({ message: "Este usuário não é um motorista." });
    }

    bcrypt.compare(senha, usuario.senha, (err, result) => {
      if (err || !result) {
        console.log("❌ [MOTORISTA] Senha incorreta");
        return res.status(401).json({ message: "Senha incorreta." });
      }

      const token = generateToken(usuario);
      console.log("✅ [MOTORISTA] Login bem-sucedido!");

      return res.status(200).json({
        message: "Login de motorista bem-sucedido!",
        usuario: usuario,
        motorista: motorista,
        token: token,
      });
    });
  } catch (err) {
    console.error("🔥 [MOTORISTA] Erro no login do motorista:", err);
    return res.status(500).json({ message: "Erro interno no servidor." });
  }
};

function generateToken(user) {
  // 1. Definir a payload do token (dados do usuário)
  const payload = {
    id: user.cpf, // CPF do usuário
    nome: user.nome, // Nome do usuário
  };

  // 2. Definir o segredo do token (armazenar em .env ou variáveis de ambiente)
  const secret = process.env.JWT_SECRET;

  // 3. Definir as opções do token (ex: tempo de expiração)
  const options = {
    //   expiresIn: '1h', // Token expira em 1 hora
    // ... outras opções
  };

  // 4. Gerar o token
  const token = jwt.sign(payload, secret, options);

  return token;
}

exports.getNomeUsuario = async (req, res) => {
  try {
    const token = req.token || req.headers.authorization; // Usar req.token se disponível

    if (!token) {
      return res.status(401).json({ message: "Token não fornecido" });
    }

    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    const cpf = decoded.id; // Obtém o CPF do token

    // const query = "SELECT * FROM usuario WHERE CPF = ?";
    const usuario = await db.Usuario.findOne({ where: { cpf: cpf } });

    if (!usuario) {
      return res.status(404).json({ message: "Usuário não encontrado" });
    }

    const nomeUsuario = usuario.nome;
    res.status(200).json(nomeUsuario);

    console.log(rows);
  } catch (error) {
    console.error("Erro ao obter dados do usuário:", error);
    res.status(500).json({ message: "Erro ao obter dados do usuário" });
  }
};