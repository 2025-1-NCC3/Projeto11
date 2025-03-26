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

import java.util.List;

import br.com.fecapccp.uber_saferide.dto.ResponseCreateUsuarioDTO;
import br.com.fecapccp.uber_saferide.dto.ResponseLoginUserDTO;
import br.com.fecapccp.uber_saferide.models.UsuarioModel;
import br.com.fecapccp.uber_saferide.retrofit.ApiService;
import br.com.fecapccp.uber_saferide.retrofit.RetrofitClient;
import br.com.fecapccp.uber_saferide.session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TelaLogin extends AppCompatActivity {

    ApiService apiService;
    SessionManager sessionManager;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = RetrofitClient.getApiService();
        sessionManager = new SessionManager(context);

        // Ocultar a barra de status e colocar o app em tela cheia
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtCriarConta = findViewById(R.id.tvCriarConta);
        txtCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaLogin.this, TelaCadastro.class);
                startActivity(intent);
            }
        });

        Button btnIniciarViagem = findViewById(R.id.btnLogin);
        btnIniciarViagem.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Log.d("button", "button clicked");
                Intent intent = new Intent(TelaLogin.this, IniciarViagem.class);
                EditText etEmail = findViewById(R.id.etEmail);
                EditText etSenha = findViewById(R.id.etSenha);
                String email = etEmail.getText().toString();
                String senha = etSenha.getText().toString();

                Log.d("Login", "Attempting login with email: " + email);
                Call<ResponseLoginUserDTO> call = apiService.loginUser(email, senha);

                call.enqueue(new Callback<ResponseLoginUserDTO>() {
                    @Override
                    public void onResponse(Call<ResponseLoginUserDTO> call, Response<ResponseLoginUserDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ResponseLoginUserDTO res = response.body();
                            Log.d("User", "Message: " + res.getMessage() + ", Token: " + res.getToken());
                            sessionManager.createSession(res.getUsuario(), res.getToken());
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(btnIniciarViagem.getContext(), "Usuário ou senha inválidos!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseLoginUserDTO> call, Throwable t) {
                        Log.d("Login", "Failure ");
                        Log.e("Error", "Erro na requisição: " + t.getMessage());
                    }
                });
            }
        });

    }

    public void onInputClick(View view) {
        Toast.makeText(this, "Campo selecionado!", Toast.LENGTH_SHORT).show();
    }

    public void onLoginClick(View view) {
        Toast.makeText(this, "Botão de Login pressionado!", Toast.LENGTH_SHORT).show();
    }
    /*    public void onCriarContaClick(View view) {
        Toast.makeText(this, "Criar Conta clicado!", Toast.LENGTH_SHORT).show();
    }*/
}
