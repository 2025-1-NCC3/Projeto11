const { descriptografar, criptografar } = require("./utils/descriptografar"); // ajuste o caminho conforme necessário

function criptografiaMiddleware(req, res, next) {
  let rawData = "";

  // Só processa JSON
  if (req.is("application/json")) {
    req.setEncoding("utf8");

    req.on("data", (chunk) => {
      rawData += chunk;
    });

    req.on("end", () => {
      try {
        // Descriptografar e parsear o corpo
        const decrypted = descriptografar(rawData);
        req.body = JSON.parse(decrypted);

        // Sobrescreve o método `res.send` para criptografar a resposta
        const originalSend = res.send.bind(res);
        res.send = (body) => {
          if (typeof body === "object") {
            body = JSON.stringify(body);
          }
          const encrypted = criptografar(body);
          res.set("Content-Type", "application/json");
          return originalSend(encrypted);
        };

        next();
      } catch (err) {
        console.error("Erro no middleware de criptografia:", err);
        res.status(400).send("Erro ao descriptografar ou interpretar JSON");
      }
    });
  } else {
    next();
  }
}

module.exports = criptografiaMiddleware;
