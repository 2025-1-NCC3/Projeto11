const { deleteUser } = require('./controllers/usuarioController');

deleteUser({ body: { id: 7 } }, { 
    status: (code) => ({ json: (msg) => console.log(`Status: ${code}`, msg) }) 
});