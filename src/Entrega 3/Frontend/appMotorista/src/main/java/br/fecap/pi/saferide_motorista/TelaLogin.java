package br.fecap.pi.saferide_motorista;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.fecap.pi.saferide_motorista.R;
import br.fecap.pi.saferide_motorista.dto.LoginRequestDTO;
import br.fecap.pi.saferide_motorista.dto.ResponseLoginUserDTO;
import br.fecap.pi.saferide_motorista.retrofit.ApiService;
import br.fecap.pi.saferide_motorista.retrofit.RetrofitClient;
import br.fecap.pi.saferide_motorista.session.SessionManager;
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

        TextView txtCriarConta = findViewById(R.id.tvCriarConta);
        txtCriarConta.setOnClickListener(v -> {
            Intent intent = new Intent(TelaLogin.this, TelaCadastro.class);
            startActivity(intent);
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

                LoginRequestDTO loginRequest = new LoginRequestDTO(email, senha);
                Call<ResponseLoginUserDTO> call = apiService.loginMotorista(loginRequest);

                call.enqueue(new Callback<ResponseLoginUserDTO>() {
                    @Override
                    public void onResponse(Call<ResponseLoginUserDTO> call, Response<ResponseLoginUserDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ResponseLoginUserDTO res = response.body();
                            Log.d("User", "Message: " + res.getMessage() + ", Token: " + res.getToken());
                            sessionManager.createSession(res.getUsuario(), res.getToken());
                            sessionManager.saveMotorista(res.getMotorista());
                            startActivity(intent);
                            finish();
                        } else {
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
}
