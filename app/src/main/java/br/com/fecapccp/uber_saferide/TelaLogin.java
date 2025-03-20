package br.com.fecapccp.uber_saferide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TelaLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void onInputClick(View view) {
        Toast.makeText(this, "Campo selecionado!", Toast.LENGTH_SHORT).show();
    }

    public void onLoginClick(View view) {
        Toast.makeText(this, "Botão de Login pressionado!", Toast.LENGTH_SHORT).show();
    }

    public void onCriarContaClick(View view) {
        Toast.makeText(this, "Criar Conta clicado!", Toast.LENGTH_SHORT).show();
    }
}