package br.fecap.pi.saferide_passageiro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.fecap.pi.saferide_passageiro.R;

public class MainActivity extends AppCompatActivity {
    private Button btnComecar;
    private boolean doubleBackToExitPressedOnce = false; // variável para controlar o duplo clique

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

         btnComecar = findViewById(R.id.btnComecar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnComecar.setOnClickListener(view ->{
            Intent intent = new Intent(this, IniciarViagem.class);
            startActivity(intent);

        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed(); // Sai do app normalmente
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000); // Reseta em 2 segundos
    }
}
