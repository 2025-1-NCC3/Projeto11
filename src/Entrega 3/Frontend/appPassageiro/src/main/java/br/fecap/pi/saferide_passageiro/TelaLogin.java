package br.fecap.pi.saferide_passageiro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

import br.fecap.pi.saferide_passageiro.dto.LoginRequestDTO;
import br.fecap.pi.saferide_passageiro.dto.ResponseLoginUserDTO;
import br.fecap.pi.saferide_passageiro.models.UsuarioModel;
import br.fecap.pi.saferide_passageiro.retrofit.ApiService;
import br.fecap.pi.saferide_passageiro.retrofit.RetrofitClient;
import br.fecap.pi.saferide_passageiro.session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaLogin extends AppCompatActivity {

    private LottieAnimationView animationView;
    private FrameLayout loadingLayout;
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

        loadingLayout = findViewById(R.id.loadingLayout);
        animationView = findViewById(R.id.animationView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtCriarConta = findViewById(R.id.tvCriarConta);
        txtCriarConta.setOnClickListener(v -> {
            startActivity(new Intent(TelaLogin.this, TelaCadastro.class));
        });

        Button btnIniciarViagem = findViewById(R.id.btnLogin);
        btnIniciarViagem.setOnClickListener(v -> fazerLogin());
    }

    @SuppressLint("ResourceType")
    private void fazerLogin() {
        showLoading();

        Log.d("button", "button clicked");
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etSenha = findViewById(R.id.etSenha);

        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();

        LoginRequestDTO loginRequest = new LoginRequestDTO(email, senha);
        Call<ResponseLoginUserDTO> call = apiService.loginPassageiro(loginRequest);

        call.enqueue(new Callback<ResponseLoginUserDTO>() {
            @Override
            public void onResponse(Call<ResponseLoginUserDTO> call, Response<ResponseLoginUserDTO> response) {
                hideLoading();

                if (response.isSuccessful() && response.body() != null) {
                    handleLoginSuccess(response.body());
                } else {
                    Toast.makeText(context, "Usuário ou senha inválidos!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLoginUserDTO> call, Throwable t) {
                hideLoading();
                Log.e("LoginError", "Erro na requisição: " + t.getMessage());
                Toast.makeText(context, "Erro de conexão", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLoginSuccess(ResponseLoginUserDTO response) {
        if (response.getUsuario() == null) {
            Toast.makeText(context, "Erro: Dados do usuário não recebidos", Toast.LENGTH_SHORT).show();
            return;
        }

        sessionManager.createSession(response.getUsuario(), response.getToken());
        startActivity(new Intent(TelaLogin.this, IniciarViagem.class));
        finish();
    }

    private void showLoading() {
        runOnUiThread(() -> {
            loadingLayout.setVisibility(View.VISIBLE);
            animationView.playAnimation();
            loadingLayout.setClickable(true);
        });
    }

    private void hideLoading() {
        runOnUiThread(() -> {
            if (loadingLayout.getVisibility() == View.VISIBLE) {
                loadingLayout.setVisibility(View.GONE);
                animationView.pauseAnimation();
                loadingLayout.setClickable(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }
}