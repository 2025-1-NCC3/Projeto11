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
    private RatingBar starBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_viagem_aceita);

        btnTViagemAceitaSet = findViewById(R.id.btnTViagemAceitaSet);
        btnCancelarViagem = findViewById(R.id.btnCancelarViagem);
        btnMudarViagem = findViewById(R.id.btnMudarViagem);
        starBar = findViewById(R.id.starBar);

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

        starBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int estrelasSelecionadas = (int) rating;
                Toast.makeText(ViagemAceitaActivity.this,
                        "Você avaliou com " + estrelasSelecionadas + " estrela(s)",
                        Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
