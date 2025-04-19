package br.com.fecapccp.uber_saferide;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MetodoPagamento extends AppCompatActivity {

    private CardView cardPix, cardCartao, cardDinheiro;
    private String opcaoSelecionada = "";
    private Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_metodo_pagamento);

        cardPix = findViewById(R.id.cardPix);
        cardCartao = findViewById(R.id.cardCartao);
        cardDinheiro = findViewById(R.id.cardDinheiro);
        btnContinuar = findViewById(R.id.btnContinuar);

        cardPix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarOpcao(cardPix);
            }
        });

        cardCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarOpcao(cardCartao);
            }
        });

        cardDinheiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarOpcao(cardDinheiro);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void selecionarOpcao(CardView selecionado) {
        cardPix.setCardBackgroundColor(Color.WHITE);
        cardCartao.setCardBackgroundColor(Color.WHITE);
        cardDinheiro.setCardBackgroundColor(Color.WHITE);

        selecionado.setCardBackgroundColor(Color.parseColor("#7FF2F2F2"));

        if (selecionado == cardPix) {
            opcaoSelecionada = "PIX";
        } else if (selecionado == cardCartao) {
            opcaoSelecionada = "Cartão";
        } else if (selecionado == cardDinheiro) {
            opcaoSelecionada = "Dinheiro";
        }
    }
}