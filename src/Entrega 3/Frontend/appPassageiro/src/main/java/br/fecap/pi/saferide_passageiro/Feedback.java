package br.fecap.pi.saferide_passageiro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import br.fecap.pi.saferide_passageiro.dto.AvaliacoesRotaResponseDTO;
import br.fecap.pi.saferide_passageiro.dto.RegistrarAvaliacaoRequestDTO;
import br.fecap.pi.saferide_passageiro.dto.RegistrarAvaliacaoResponseDTO;
import br.fecap.pi.saferide_passageiro.models.FeedbackModel;
import br.fecap.pi.saferide_passageiro.models.RotaModel;
import br.fecap.pi.saferide_passageiro.retrofit.ApiService;
import br.fecap.pi.saferide_passageiro.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Feedback extends AppCompatActivity {

    private LinearLayout btnPositivos, layoutPositivos;
    private LinearLayout btnNeutro, layoutNeutros;
    private LinearLayout btnNegativo, layoutNegativos;
    private ImageView imgSetaPositivo, imgSetaNeutro, imgSetaNegativo;
    private Button btnFinalizar;
    private ScrollView scrollView;

    // Listas para armazenar os checkboxes
    private Map<Integer, CheckBox> checkboxesPositivos = new HashMap<>();
    private Map<Integer, CheckBox> checkboxesNeutros = new HashMap<>();
    private Map<Integer, CheckBox> checkboxesNegativos = new HashMap<>();

    private boolean isPositivosOpen = false;
    private boolean isNeutrosOpen = false;
    private boolean isNegativosOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Recuperar estado salvo, se existir
        if (savedInstanceState != null) {
            isPositivosOpen = savedInstanceState.getBoolean("isPositivosOpen", false);
            isNeutrosOpen = savedInstanceState.getBoolean("isNeutrosOpen", false);
            isNegativosOpen = savedInstanceState.getBoolean("isNegativosOpen", false);
        }

        // Referência ao ScrollView
        scrollView = findViewById(R.id.scrollView);

        // Positivos
        btnPositivos = findViewById(R.id.btnPositivos);
        layoutPositivos = findViewById(R.id.layoutPositivos);
        imgSetaPositivo = findViewById(R.id.imgSetaPositivo);

        // Inicializar checkboxes positivos
        checkboxesPositivos.put(3, findViewById(R.id.checkPositivo1));
        checkboxesPositivos.put(1, findViewById(R.id.checkPositivo2));
        checkboxesPositivos.put(2, findViewById(R.id.checkPositivo3));

        btnPositivos.setOnClickListener(view -> {
            isPositivosOpen = !isPositivosOpen;
            layoutPositivos.setVisibility(isPositivosOpen ? View.VISIBLE : View.GONE);
            imgSetaPositivo.setImageResource(isPositivosOpen ? R.drawable.icon_seta_baixo : R.drawable.icon_seta_esquerda);

            // Scroll para o conteúdo recém-expandido
            if (isPositivosOpen) {
                scrollToView(layoutPositivos);
            }
        });

        // Neutros
        btnNeutro = findViewById(R.id.btnNeutro);
        layoutNeutros = findViewById(R.id.layoutNeutros);
        imgSetaNeutro = findViewById(R.id.imgSetaNeutro);

        // Inicializar checkboxes neutros
        checkboxesNeutros.put(4, findViewById(R.id.checkNeutro1));
        checkboxesNeutros.put(5, findViewById(R.id.checkNeutro2));
        checkboxesNeutros.put(6, findViewById(R.id.checkNeutro3));

        btnNeutro.setOnClickListener(v -> {
            isNeutrosOpen = !isNeutrosOpen;
            layoutNeutros.setVisibility(isNeutrosOpen ? View.VISIBLE : View.GONE);
            imgSetaNeutro.setImageResource(isNeutrosOpen ? R.drawable.icon_seta_baixo : R.drawable.icon_seta_esquerda);

            // Scroll para o conteúdo recém-expandido
            if (isNeutrosOpen) {
                scrollToView(layoutNeutros);
            }
        });

        // Negativos
        btnNegativo = findViewById(R.id.btnNegativo);
        layoutNegativos = findViewById(R.id.layoutNegativos);
        imgSetaNegativo = findViewById(R.id.imgSetaNegativo);

        // Inicializar checkboxes negativos
        checkboxesNegativos.put(7, findViewById(R.id.checkNegativo1));
        checkboxesNegativos.put(8, findViewById(R.id.checkNegativo2));
        checkboxesNegativos.put(9, findViewById(R.id.checkNegativo3));
        checkboxesNegativos.put(10, findViewById(R.id.checkNegativo4));

        btnNegativo.setOnClickListener(v -> {
            isNegativosOpen = !isNegativosOpen;
            layoutNegativos.setVisibility(isNegativosOpen ? View.VISIBLE : View.GONE);
            imgSetaNegativo.setImageResource(isNegativosOpen ? R.drawable.icon_seta_baixo : R.drawable.icon_seta_esquerda);

            // Scroll para o conteúdo recém-expandido
            if (isNegativosOpen) {
                scrollToView(layoutNegativos);
            }
        });

        // Configurar listeners para todos os checkboxes
        configurarCheckBoxListeners();

        btnFinalizar = findViewById(R.id.btnFinalizar);
        // Inicialmente desabilitar o botão
        btnFinalizar.setEnabled(false);
        btnFinalizar.setAlpha(0.5f); // Visual feedback que está desabilitado

        // Aplicar o estado inicial dos layouts
        layoutPositivos.setVisibility(isPositivosOpen ? View.VISIBLE : View.GONE);
        imgSetaPositivo.setImageResource(isPositivosOpen ? R.drawable.icon_seta_baixo : R.drawable.icon_seta_esquerda);

        layoutNeutros.setVisibility(isNeutrosOpen ? View.VISIBLE : View.GONE);
        imgSetaNeutro.setImageResource(isNeutrosOpen ? R.drawable.icon_seta_baixo : R.drawable.icon_seta_esquerda);

        layoutNegativos.setVisibility(isNegativosOpen ? View.VISIBLE : View.GONE);
        imgSetaNegativo.setImageResource(isNegativosOpen ? R.drawable.icon_seta_baixo : R.drawable.icon_seta_esquerda);

        verificarSelecoes();

        btnFinalizar.setOnClickListener(v -> {
            if (!calcularFeedbacksSelecionados().isEmpty()) {
                Intent pastIntent = getIntent();

                RegistrarAvaliacaoRequestDTO body = new RegistrarAvaliacaoRequestDTO(
                    pastIntent.getIntExtra("USER_ID", 1),
                    11,
                    pastIntent.getIntExtra("ROTA_ID", 0),
                    calcularNotaAvaliacao(calcularFeedbacksSelecionados()),
                    calcularFeedbacksSelecionados()
                );

                Log.d("REQUEST BODY REGISTRAR AVALIACAO", body.toString());

                ApiService apiService = RetrofitClient.getApiService();
                Call<RegistrarAvaliacaoResponseDTO> call = apiService.registrarAvaliacao(body);

                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<RegistrarAvaliacaoResponseDTO> call, Response<RegistrarAvaliacaoResponseDTO> response) {
                        if (response.isSuccessful()) {
                            Log.d("AVALIACOES", "Avaliacao Registrada!");
                        } else {
                            Log.d("AVALIACOES", "Erro ao registrar avaliação: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<RegistrarAvaliacaoResponseDTO> call, Throwable t) {}
                });

                Intent intent = new Intent(Feedback.this, IniciarViagem.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Selecione pelo menos uma opção", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Configurar listeners para todos os checkboxes
    private void configurarCheckBoxListeners() {
        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> verificarSelecoes();

        checkboxesPositivos.forEach((key, value) -> {
            value.setOnCheckedChangeListener(listener);
        });

        checkboxesNeutros.forEach((key, value) -> {
            value.setOnCheckedChangeListener(listener);
        });

        checkboxesNegativos.forEach((key, value) -> {
            value.setOnCheckedChangeListener(listener);
        });
    }
    private void verificarSelecoes() {
        boolean peloMenosUma = !calcularFeedbacksSelecionados().isEmpty();

        // Habilitar ou desabilitar o botão com base nas seleções
        btnFinalizar.setEnabled(peloMenosUma);
        btnFinalizar.setAlpha(peloMenosUma ? 1.0f : 0.5f);
    }

    // Verifica se pelo menos uma opção de qualquer categoria foi selecionada
    private List<FeedbackModel> calcularFeedbacksSelecionados() {

        List<FeedbackModel> feedbacks = new ArrayList<>();

        checkboxesPositivos.forEach((key, value) -> {
            if (value.isChecked()) {
                feedbacks.add(new FeedbackModel(key, "Positivo"));
            }
        });

        checkboxesNeutros.forEach((key, value) -> {
            if (value.isChecked()) {
                feedbacks.add(new FeedbackModel(key, "Neutro"));
            }
        });

        checkboxesNegativos.forEach((key, value) -> {
            if (value.isChecked()) {
                feedbacks.add(new FeedbackModel(key, "Negativo"));
            }
        });

        return feedbacks;
    }

    // Método para fazer scroll até uma view específica
    private void scrollToView(final View view) {
        scrollView.post(() -> {
            scrollView.smoothScrollTo(0, view.getTop());
        });
    }

    // Salvar o estado de expansão dos layouts
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isPositivosOpen", isPositivosOpen);
        outState.putBoolean("isNeutrosOpen", isNeutrosOpen);
        outState.putBoolean("isNegativosOpen", isNegativosOpen);

        // Também poderíamos salvar o estado dos checkboxes aqui se necessário
    }

    private int calcularNotaAvaliacao(List<FeedbackModel> feedbacks) {
        if (feedbacks.isEmpty()) return 3; // Nota neutra se não houver feedbacks

        int feedbacksNegativos = 0;
        int feedbacksPositivos = 0;
        int feedbacksNeutros = 0;

        for (FeedbackModel f : feedbacks) {
            switch (f.getCategoria().toLowerCase()) {
                case "positivo":
                    feedbacksPositivos++;
                    break;
                case "negativo":
                    feedbacksNegativos++;
                    break;
                case "neutro":
                    feedbacksNeutros++;
                    break;
            }
        }

        // Cálculo da nota (1 a 5)
        // Positivos valem +1, neutros 0, negativos -1
        int pontuacao = feedbacksPositivos - feedbacksNegativos;
        int totalFeedbacks = feedbacksPositivos + feedbacksNeutros + feedbacksNegativos;

        // Normalizar para escala de 1 a 5
        float notaNormalizada = ((float) pontuacao / totalFeedbacks) * 2 + 3;

        // Garantir que está entre 1 e 5
        return Math.max(1, Math.min(5, Math.round(notaNormalizada)));
    }
}
