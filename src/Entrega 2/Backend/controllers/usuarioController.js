const db = require('../config/db')
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const Usuario = require('../models/Usuario');
const Motorista = require('../models/Motorista');
const Passageiro = require('../models/Passageiro');


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
        validade_carteira
    } = req.body; 

    // Verifica se todos os dados obrigatórios foram enviados
    if (tipo_usuario.toLowerCase() === 'motorista') {
        if (!cnh || !validade_carteira) {
            return res.status(400).json({ message: 'Campos obrigatórios para o motorista devem ser preenchidos.' });
        }
    }

    if (!tipo_usuario || !nome || !email || !telefone || !cpf || !data_nascimento || !senha) {
        return res.status(400).json({ message: 'Todos os campos obrigatórios devem ser preenchidos.' });
    }

    if (tipo_usuario.toLowerCase() !== 'motorista' && tipo_usuario.toLowerCase() !== 'passageiro') {
        return res.status(400).json({ message: 'Tipo de usuário inválido.' });
    }
    
    if (cpf.toString().length !== 11) {
        return res.status(400).json({ message: 'O CPF deve conter 11 dígitos.' });
    }

    if (telefone.length < 10 || telefone.length > 11) {
        return res.status(400).json({ message: 'O telefone deve conter entre 10 e 11 dígitos.' });
    }

    try {
        const usuario = await  db.Usuario.findOne({ where: { cpf: cpf.toString(), tipo_usuario: tipo_usuario } });

        if (usuario) {
            return res.status(400).json({ message: 'CPF já cadastrado como: ' + tipo_usuario });
        }
        
        // if (tipo_usuario.toLowerCase() === 'motorista') {
        //     createUsuarioMotorista(usuario);
        // }

        // if (tipo_usuario.toLowerCase() === 'passageiro') {
        //     createUsuarioPassageiro(usuario);
        // }


        // res.status(200).json({ 
        //     message: 'Usuário ' + tipo_usuario + ' cadastrado com sucesso!',
        // });

        bcrypt.hash(senha, 10, async (err, hashedPassword) => {
            if (err) {
                return res.status(500).json({ message: 'Erro ao criar a senha. Tente novamente.' });
            }

            const newUsuario = await db.Usuario.create({
                nome, 
                email,
                telefone, 
                cpf: cpf.trim(),  
                data_nascimento,
                tipo_usuario: tipo_usuario.toLowerCase(),
                senha: hashedPassword,
            });

            res.status(200).json({ 
                message: 'Usuário ' + tipo_usuario + ' cadastrado com sucesso!'
             });
        });
    } catch (error) {
        console.error('Erro ao inserir usuário:', error);
        return res.status(500).json({
            message: 'Erro ao cadastrar o usuário. Tente novamente mais tarde.',
            error: error.message
        });
    }
};

async function createUsuarioMotorista(usuario) {
    await Motorista.create({
        cnh: cnh,
        validade_carteira: validade_carteira,
        id_usuario: usuario.id_usuario
    });
}

async function createUsuarioPassageiro(usuario) {
    await Passageiro.create({
        id_usuario: usuario.id_usuario
    });
}

exports.getUsuarios = async (req, res) => {
    try {
        const usuarios = await db.Usuario.findAll();
        res.status(200).json(usuarios);
    } catch (error) {
        console.error('Erro ao obter usuários:', error);
        res.status(500).json({ message: 'Erro ao obter usuários' });
    }
}

exports.getUsuarioById = async (req, res) => {
    const { id } = req.params;

    try{
        const usuario = await db.Usuario.findByPk(id);
        
        if (usuario) {
            return res.status(200).json(usuario);
        }
        res.status(404).json({message: 'Usuário não encontrado'});
    } catch (error) {
        console.error('Erro ao obter usuário por ID:', error);
        res.status(500).json({ message: 'Erro ao obter usuário por ID' });
    }
}

exports.getUsuarioByCpf = async (req, res) => {
    const { cpf } = req.params;

    try{
        const usuario = await db.Usuario.findOne({where: { cpf: cpf }});
        
        if (usuario) {
            return res.status(200).json(usuario);
        }
        res.status(404).json({message: 'Usuário não encontrado'});
    } catch (error) {
        console.error('Erro ao obter usuário por ID:', error);
        res.status(500).json({ message: 'Erro ao obter usuário por ID' });
    }
}

exports.updateUsuario = async (req, res) => {
    const { id_usuario, cpf, nome, email, telefone, senha } = req.body;

    if (!cpf || !id_usuario) {
        return res.status(400).json({ message: 'CPF é obrigatório' });
    }

    try {
        const usuario = await db.Usuario.findOne({ where: { id_usuario }, limit: 1 });

        if (!usuario) {
            return res.status(404).json({ message: 'Usuário não encontrado' });
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

        return res.status(200).json({ message: 'Usuário atualizado com sucesso' });
    } catch (error) {
        console.error('Erro ao atualizar usuário:', error);
        return res.status(500).json({ message: 'Erro ao atualizar usuário' });
    }
}

exports.deleteUser = async (req, res) => {
    const { id } = req.params; // ID agora vem da URL!

    if (!id) {
        return res.status(400).json({ message: 'ID é obrigatório' });
    }

    const t = await sequelize.transaction(); // Transação

    try {
        const usuario = await db.Usuario.findOne({ where: { id }, transaction: t });

        if (!usuario) {
            await t.rollback();
            return res.status(404).json({ message: 'Usuário não encontrado' });
        }

        if (usuario.tipo_usuario === 'motorista') {
            await Motorista.destroy({ 
                where: { id_usuario: id }, 
                transaction: t // Associado à transação
            });
        }

        if (usuario.tipo_usuario === 'passageiro') { // Corrigi typo ("passageiro")
            await Passageiro.destroy({ 
                where: { id_usuario: id }, 
                transaction: t 
            });
        }

        await Usuario.destroy({ 
            where: { id }, 
            transaction: t 
        });

        await t.commit(); // Confirma tudo

        return res.status(204).send(); // 204 = Sucesso sem conteúdo

    } catch (error) {
        await t.rollback();
        console.error('Erro detalhado:', error);
        return res.status(500).json({ 
            message: 'Erro interno',
            error: error.message // Envia detalhes do erro para debug
        });
    }
};


exports.loginUser = async (req, res) => {
    console.log("Início da função loginUser");
    const { email, senha } = req.body;

    console.log(req.body)
    
    try {
        const usuario = await db.Usuario.findOne({ where: { email: email } });
        
        if (!usuario) {
            return res.status(404).json({ message: 'Usuário não encontrado. Verifique suas credenciais.' });
        }

        if (!senha || !usuario.senha) {
            return res.status(400).json({ message: 'Senha inválida' });
        }
        
        // Comparar as senhas 
        bcrypt.compare(senha, usuario.senha, (err, result) => {
            if (err) {
                console.error('Erro ao comparar senhas:', err);
                
                // Adicionar headers CORS na resposta
                res.set({
                    'Access-Control-Allow-Origin': 'http://localhost:3000',
                    'Access-Control-Allow-Methods': 'POST',
                    'Access-Control-Allow-Headers': 'Content-Type, Authorization'
                });
                
                return res.status(500).json({ message: 'Erro ao realizar o login. Tente novamente.' });
            } 
            
            if (!result) {
                
                // Adicionar headers CORS na resposta
                res.set({
                    'Access-Control-Allow-Origin': 'http://localhost:3000',
                    'Access-Control-Allow-Methods': 'POST',
                    'Access-Control-Allow-Headers': 'Content-Type, Authorization'
                });
                
                return res.status(401).json({ message: 'Senha incorreta. Tente novamente.' });
            }

            const token = generateToken(usuario);
            
            // Adicionar headers CORS na resposta
            res.set({
                'Access-Control-Allow-Origin': 'http://localhost:3000',
                'Access-Control-Allow-Methods': 'POST',
                'Access-Control-Allow-Headers': 'Content-Type, Authorization'
            });
            
            // Se o login for bem-sucedido
            return res.status(200).json({ 
                message: 'Login bem-sucedido!', 
                usuario: usuario,
                token: token // Inclui o token na resposta
            });
        });
        
    } catch (err) {
        console.error('Erro ao verificar o email:', err);
        
        // Adicionar headers CORS na resposta
        res.set({
            'Access-Control-Allow-Origin': 'http://localhost:3000',
            'Access-Control-Allow-Methods': 'POST',
            'Access-Control-Allow-Headers': 'Content-Type, Authorization'
        });
        
        return res.status(500).json({ message: 'Erro ao realizar o login. Tente novamente.' });
    }
}

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
            return res.status(401).json({ message: 'Token não fornecido' });
        }
  
      const decoded = jwt.verify(token, process.env.JWT_SECRET);   
  
      const cpf = decoded.id; // Obtém o CPF do token
  
      const query = 'SELECT * FROM usuario WHERE CPF = ?';
      const [rows] = await db.query(query, [cpf]);
  
      if (rows.length === 0) {
        return res.status(404).json({ message: 'Usuário não encontrado' });
      }
  
      const nomeUsuario = rows[0].nome_usuario;
      res.status(200).json(rows);

      console.log(rows);
      
  
    } catch (error) {
      console.error('Erro ao obter dados do usuário:', error);
      res.status(500).json({ message: 'Erro ao obter dados do usuário' });
    }
  };
  
exports.deleteUser = async (req, res) => {
    
}