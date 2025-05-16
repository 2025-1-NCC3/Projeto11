package br.fecap.pi.saferide_passageiro;

import android.os.Bundle;
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
import java.util.List;

public class Feedback extends AppCompatActivity {

    private LinearLayout btnPositivos, layoutPositivos;
    private LinearLayout btnNeutro, layoutNeutros;
    private LinearLayout btnNegativo, layoutNegativos;
    private ImageView imgSetaPositivo, imgSetaNeutro, imgSetaNegativo;
    private Button btnFinalizar;
    private ScrollView scrollView;

    // Listas para armazenar os checkboxes
    private List<CheckBox> checkboxesPositivos = new ArrayList<>();
    private List<CheckBox> checkboxesNeutros = new ArrayList<>();
    private List<CheckBox> checkboxesNegativos = new ArrayList<>();

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
        checkboxesPositivos.add(findViewById(R.id.checkPositivo1));
        checkboxesPositivos.add(findViewById(R.id.checkPositivo2));
        checkboxesPositivos.add(findViewById(R.id.checkPositivo3));
        checkboxesPositivos.add(findViewById(R.id.checkPositivo4));

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
        checkboxesNeutros.add(findViewById(R.id.checkNeutro1));
        checkboxesNeutros.add(findViewById(R.id.checkNeutro2));
        checkboxesNeutros.add(findViewById(R.id.checkNeutro3));

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
        checkboxesNegativos.add(findViewById(R.id.checkNegativo1));
        checkboxesNegativos.add(findViewById(R.id.checkNegativo2));
        checkboxesNegativos.add(findViewById(R.id.checkNegativo3));
        checkboxesNegativos.add(findViewById(R.id.checkNegativo4));
        checkboxesNegativos.add(findViewById(R.id.checkNegativo5));

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

        btnFinalizar.setOnClickListener(v -> {
            // Verificar novamente se pelo menos uma opção foi selecionada
            if (peloMenosUmaOpcaoSelecionada()) {
                finish(); // Encerra a tela atual
            } else {
                // Mostrar mensagem informando que pelo menos uma opção deve ser selecionada
                Toast.makeText(this, "Selecione pelo menos uma opção", Toast.LENGTH_SHORT).show();
            }
        });

        // Aplicar o estado inicial dos layouts
        layoutPositivos.setVisibility(isPositivosOpen ? View.VISIBLE : View.GONE);
        imgSetaPositivo.setImageResource(isPositivosOpen ? R.drawable.icon_seta_baixo : R.drawable.icon_seta_esquerda);

        layoutNeutros.setVisibility(isNeutrosOpen ? View.VISIBLE : View.GONE);
        imgSetaNeutro.setImageResource(isNeutrosOpen ? R.drawable.icon_seta_baixo : R.drawable.icon_seta_esquerda);

        layoutNegativos.setVisibility(isNegativosOpen ? View.VISIBLE : View.GONE);
        imgSetaNegativo.setImageResource(isNegativosOpen ? R.drawable.icon_seta_baixo : R.drawable.icon_seta_esquerda);

        // Verificar se já existem seleções (caso de restauração de estado)
        verificarSelecoes();
    }

    // Configurar listeners para todos os checkboxes
    private void configurarCheckBoxListeners() {
        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> verificarSelecoes();

        // Adicionar listener a cada checkbox positivo
        for (CheckBox checkbox : checkboxesPositivos) {
            checkbox.setOnCheckedChangeListener(listener);
        }

        // Adicionar listener a cada checkbox neutro
        for (CheckBox checkbox : checkboxesNeutros) {
            checkbox.setOnCheckedChangeListener(listener);
        }

        // Adicionar listener a cada checkbox negativo
        for (CheckBox checkbox : checkboxesNegativos) {
            checkbox.setOnCheckedChangeListener(listener);
        }
    }

    // Método para verificar se pelo menos uma opção foi selecionada
    private void verificarSelecoes() {
        boolean peloMenosUma = peloMenosUmaOpcaoSelecionada();

        // Habilitar ou desabilitar o botão com base nas seleções
        btnFinalizar.setEnabled(peloMenosUma);
        btnFinalizar.setAlpha(peloMenosUma ? 1.0f : 0.5f);
    }

    // Verifica se pelo menos uma opção de qualquer categoria foi selecionada
    private boolean peloMenosUmaOpcaoSelecionada() {
        // Verificar checkboxes positivos
        for (CheckBox checkbox : checkboxesPositivos) {
            if (checkbox.isChecked()) {
                return true;
            }
        }

        // Verificar checkboxes neutros
        for (CheckBox checkbox : checkboxesNeutros) {
            if (checkbox.isChecked()) {
                return true;
            }
        }

        // Verificar checkboxes negativos
        for (CheckBox checkbox : checkboxesNegativos) {
            if (checkbox.isChecked()) {
                return true;
            }
        }

        return false;
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
}
