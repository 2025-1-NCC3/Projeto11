package br.fecap.pi.saferide_passageiro;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import br.fecap.pi.saferide_passageiro.R;

public class Feedback extends AppCompatActivity {

    private LinearLayout btnPositivos, layoutPositivos;
    private LinearLayout btnNeutro, layoutNeutros;
    private LinearLayout btnNegativo, layoutNegativos;
    private ImageView imgSetaPositivo, imgSetaNeutro, imgSetaNegativo;

    private boolean isPositivosOpen = false;
    private boolean isNeutrosOpen = false;
    private boolean isNegativosOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Positivos
        btnPositivos = findViewById(R.id.btnPositivos);
        layoutPositivos = findViewById(R.id.layoutPositivos);
        imgSetaPositivo = findViewById(R.id.imgSetaPositivo);
//
        btnPositivos.setOnClickListener(view -> {
            isPositivosOpen = !isPositivosOpen;
            layoutPositivos.setVisibility(isPositivosOpen ? View.VISIBLE : View.GONE);
            imgSetaPositivo.setImageResource(isPositivosOpen ? R.drawable.icon_seta_baixo : R.drawable.icon_seta_esquerda);
        });

        // Neutros
        btnNeutro = findViewById(R.id.btnNeutro);
        layoutNeutros = findViewById(R.id.layoutNeutros);
        imgSetaNeutro = findViewById(R.id.imgSetaNeutro);

        btnNeutro.setOnClickListener(v -> {
            isNeutrosOpen = !isNeutrosOpen;
            layoutNeutros.setVisibility(isNeutrosOpen ? View.VISIBLE : View.GONE);
            imgSetaNeutro.setImageResource(isNeutrosOpen ? R.drawable.icon_seta_baixo : R.drawable.icon_seta_esquerda);
        });

        // Negativos
        btnNegativo = findViewById(R.id.btnNegativo);
        layoutNegativos = findViewById(R.id.layoutNegativos);
        imgSetaNegativo = findViewById(R.id.imgSetaNegativo);

        btnNegativo.setOnClickListener(v -> {
            isNegativosOpen = !isNegativosOpen;
            layoutNegativos.setVisibility(isNegativosOpen ? View.VISIBLE : View.GONE);
            imgSetaNegativo.setImageResource(isNegativosOpen ? R.drawable.icon_seta_baixo : R.drawable.icon_seta_esquerda);
        });
    }
}