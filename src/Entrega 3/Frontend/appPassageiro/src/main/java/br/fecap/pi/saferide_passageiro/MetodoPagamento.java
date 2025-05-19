package br.fecap.pi.saferide_passageiro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

public class MetodoPagamento extends AppCompatActivity {

    private CardView cardPix, cardCartao, cardDinheiro;
    private String opcaoSelecionada = "";
    private Button btnContinuar;
    private FrameLayout procurandomMotorista;
    private LottieAnimationView animationView;
    private Runnable loadingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_metodo_pagamento);

        cardPix = findViewById(R.id.cardPix);
        cardCartao = findViewById(R.id.cardCartao);
        cardDinheiro = findViewById(R.id.cardDinheiro);
        btnContinuar = findViewById(R.id.btnContinuar);

        procurandomMotorista = findViewById(R.id.procurandomMotorista);
        animationView = findViewById(R.id.animationView);
        procurandomMotorista.bringToFront();

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

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (opcaoSelecionada.isEmpty()) {
                    Toast.makeText(MetodoPagamento.this,
                            "Por favor, selecione um método de pagamento.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    showLoading();

                    if (loadingRunnable != null) {
                        new Handler(Looper.getMainLooper()).removeCallbacks(loadingRunnable);
                    }

                    loadingRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (!isFinishing()) {
                                hideLoading();
                                Intent intent = new Intent(MetodoPagamento.this, ViagemAceitaActivity.class);
                                intent.putExtra("metodo_pagamento", opcaoSelecionada);
                                startActivity(intent);
                                finish();
                            }
                        }
                    };

                    new Handler(Looper.getMainLooper()).postDelayed(loadingRunnable, 7000);
                }
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

    private void showLoading() {
        runOnUiThread(() -> {
            procurandomMotorista.setVisibility(View.VISIBLE);
            animationView.playAnimation();
            procurandomMotorista.setClickable(true);
            btnContinuar.setEnabled(false); // Desabilita o botão durante o loading
        });
    }

    private void hideLoading() {
        runOnUiThread(() -> {
            if (procurandomMotorista.getVisibility() == View.VISIBLE) {
                procurandomMotorista.setVisibility(View.GONE);
                animationView.pauseAnimation();
                procurandomMotorista.setClickable(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (loadingRunnable != null) {
            new Handler(Looper.getMainLooper()).removeCallbacks(loadingRunnable);
        }
        hideLoading();
        super.onDestroy();
    }
}