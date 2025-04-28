package br.com.fecapccp.uber_saferide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.com.fecapccp.uber_saferide.dto.ResponseLoginUserDTO;
import br.com.fecapccp.uber_saferide.models.UsuarioModel;
import br.com.fecapccp.uber_saferide.retrofit.ApiService;
import br.com.fecapccp.uber_saferide.retrofit.RetrofitClient;
import br.com.fecapccp.uber_saferide.session.SessionManager;
import br.com.fecapccp.uber_saferide.utils.CaesarCipher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaLogin extends AppCompatActivity {

    ApiService apiService;
    SessionManager sessionManager;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = RetrofitClient.getApiService();
        sessionManager = new SessionManager(context);


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configuração do botão de criar conta
        TextView txtCriarConta = findViewById(R.id.tvCriarConta);
        txtCriarConta.setOnClickListener(v -> {
            startActivity(new Intent(TelaLogin.this, TelaCadastro.class));
        });

        // Configuração do botão de login
        Button btnIniciarViagem = findViewById(R.id.btnLogin);
        btnIniciarViagem.setOnClickListener(v -> fazerLogin() );
    }

    @SuppressLint("ResourceType")
    private void fazerLogin() {
        Log.d("button", "button clicked");
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etSenha = findViewById(R.id.etSenha);

        // Criptografa as credenciais antes de enviar
        String email = CaesarCipher.encrypt(etEmail.getText().toString(), 3);
        String senha = CaesarCipher.encrypt(etSenha.getText().toString(), 3);

        Log.d("Login", "Attempting login with encrypted email: " + email);
        Call<ResponseLoginUserDTO> call = apiService.loginPassageiro(email, senha);

        call.enqueue(new Callback<ResponseLoginUserDTO>() {
            @Override
            public void onResponse(Call<ResponseLoginUserDTO> call, Response<ResponseLoginUserDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseLoginUserDTO res = response.body();

                    // Verifica se o objeto usuário não é nulo
                    if (res.getUsuario() == null) {
                        Toast.makeText(context, "Erro: Dados do usuário não recebidos", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Descriptografa os dados do usuário recebidos
                    UsuarioModel usuarioDescriptografado = descriptografarUsuario(res.getUsuario());

                    Log.d("User", "Message: " + res.getMessage() + ", Token: " + res.getToken());
                    sessionManager.createSession(usuarioDescriptografado, res.getToken());

                    // Redireciona para a tela principal
                    startActivity(new Intent(TelaLogin.this, IniciarViagem.class));
                    finish();
                } else {
                    Toast.makeText(context, "Usuário ou senha inválidos!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLoginUserDTO> call, Throwable t) {
                Log.d("Login", "Failure ");
                Log.e("Error", "Erro na requisição: " + t.getMessage());
                Toast.makeText(context, "Erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para descriptografar os dados do usuário recebidos
    private UsuarioModel descriptografarUsuario(UsuarioModel usuarioCriptografado) {
        UsuarioModel usuario = new UsuarioModel();
        try {
            // ID não precisa ser descriptografado e nunca é nulo (int primitivo)
            usuario.setIdUsuario(usuarioCriptografado.getIdUsuario());

            // Descriptografa os campos string
            usuario.setNome(usuarioCriptografado.getNome() != null ?
                    CaesarCipher.decrypt(usuarioCriptografado.getNome(), 3) : null);

            usuario.setEmail(usuarioCriptografado.getEmail() != null ?
                    CaesarCipher.decrypt(usuarioCriptografado.getEmail(), 3) : null);

            usuario.setCpf(usuarioCriptografado.getCpf() != null ?
                    CaesarCipher.decrypt(usuarioCriptografado.getCpf(), 3) : null);

            usuario.setTelefone(usuarioCriptografado.getTelefone() != null ?
                    CaesarCipher.decrypt(usuarioCriptografado.getTelefone(), 3) : null);

            // Data de nascimento (já em formato string)
            if (usuarioCriptografado.getDataNascimento() != null) {
                usuario.setDataNascimento(
                        CaesarCipher.decrypt(usuarioCriptografado.getDataNascimento(), 3)
                );
            }

            // Tipo de usuário (enum, não precisa descriptografar)
            usuario.setTipoUsuario(usuarioCriptografado.getTipoUsuario());

        } catch (Exception e) {
            Log.e("DecryptError", "Erro ao descriptografar usuário: " + e.getMessage());
            // Retorna usuário vazio em caso de erro
            return new UsuarioModel();
        }
        return usuario;
    }
}