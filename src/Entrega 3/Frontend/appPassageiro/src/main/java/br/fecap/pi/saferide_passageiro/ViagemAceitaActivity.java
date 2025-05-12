package br.fecap.pi.saferide_passageiro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViagemAceitaActivity extends AppCompatActivity {

    private Button btnTViagemAceitaSet;
    private Button btnCancelarViagem;
    private Button btnMudarViagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_viagem_aceita);

        btnTViagemAceitaSet = findViewById(R.id.btnTViagemAceitaSet);
        btnCancelarViagem = findViewById(R.id.btnCancelarViagem);
        btnMudarViagem = findViewById(R.id.btnMudarViagem);


        btnTViagemAceitaSet.setOnClickListener(view -> {
            Intent intent = new Intent(this, MetodoPagamento.class);
            startActivity(intent);
        });

        btnCancelarViagem.setOnClickListener(view -> {
            finish();
        });

        btnMudarViagem.setOnClickListener(view -> {
            Intent intent = new Intent(this, IniciarViagem.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
