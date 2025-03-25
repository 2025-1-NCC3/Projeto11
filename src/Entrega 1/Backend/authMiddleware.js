// authMiddleware.js
const jwt = require('jsonwebtoken');

const extractToken = (req, res, next) => {
    const bearerHeader = req.headers.authorization;
  
    if (typeof bearerHeader !== 'undefined') {
      const bearer = bearerHeader.split(' ');
      const bearerToken = bearer[1];
      req.token = bearerToken;
      next();
    } else {
      res.sendStatus(403);
    }
  }

const authMiddleware = (req, res, next) => {
  // 1. Obter o token do header da requisição
  const token = req.token || req.headers.authorization; // Usar req.token se disponível

  // 2. Verificar se o token foi fornecido
  if (!token) {
    return res.status(401).json({ message: 'Token não fornecido' });
  }

  try {
    // 3. Decodificar o token
    const decoded = jwt.verify(token, process.env.JWT_SECRET); // Substitua 'seu_segredo' pelo segredo do JWT
    console.log(decoded)
    // 4. Armazenar os dados do usuário na requisição
    req.user = decoded;

    // 5. Chamar a próxima função (rota protegida)
    next();
  } catch (error) {
    // 6. Lidar com erro de token inválido
    return res.status(401).json({ message: 'Token inválido' });
  }
};

module.exports = { extractToken, authMiddleware };