package br.fecap.pi.saferide_passageiro;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.Toast;

import br.fecap.pi.saferide_passageiro.R;
import br.fecap.pi.saferide_passageiro.dto.ResponseDTO;
import br.fecap.pi.saferide_passageiro.retrofit.ApiService;
import br.fecap.pi.saferide_passageiro.retrofit.RetrofitClient;
import br.fecap.pi.saferide_passageiro.session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfiguracaoPerfil extends AppCompatActivity {

    private LinearLayout profileHeader, dataSection, optionsSection;
    private LinearLayout dataContent, optionsContent;
    private ImageView arrowData, arrowOptions, profileImage;
    private ImageButton btnBack;
    private Button btnEditar, btnSalvar;
    private EditText editNome, editTelefone, editEmail, editSenha;
    private TextView textCPF, textDataNascimento, txtSair;
    private AlertDialog alertDialog;
    private TextView txtDeletarConta;

    ApiService apiService;
    SessionManager sessionManager;
    Context context = this;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = RetrofitClient.getApiService();
        sessionManager = new SessionManager(context);
        

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuracao_perfil);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        // Inicialize os layouts das caixas de informação
        profileHeader = findViewById(R.id.profile_header);
        dataSection = findViewById(R.id.data_section);
        optionsSection = findViewById(R.id.options_section);

        // Inicialize os layouts dos conteúdos das caixas de informação
        dataContent = findViewById(R.id.data_content);
        optionsContent = findViewById(R.id.options_content);
        profileImage = findViewById(R.id.profile_image);

        // Inicialize as ImageViews das setas
        arrowData = findViewById(R.id.arrow_data);
        arrowOptions = findViewById(R.id.arrow_options);

        // Inicializa o ImageButton btnBack
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Encerra a Activity atual e volta para a anterior
                finish();
            }
        });

        // Inicialize os EditTexts
        editNome = findViewById(R.id.editTextNome);
        editNome.setText(sessionManager.getUserNome());

        editTelefone = findViewById(R.id.editTextTelefone);
        editTelefone.setText(sessionManager.getUserTelefone());

        editEmail = findViewById(R.id.editTextEmail);
        editEmail.setText(sessionManager.getUserEmail());

        editSenha = findViewById(R.id.editTextSenha);
        editSenha.setText(sessionManager.getUserSenha());

        // Inicializa os TextView
        textCPF = findViewById(R.id.textViewCPF);
        textCPF.append("CPF: " + sessionManager.getUserCpf());

        textDataNascimento = findViewById(R.id.textViewDataNascimento);
        textDataNascimento.append("Data de Nascimento: " + sessionManager.getUserDataNascimento());

        txtSair = findViewById(R.id.txtSair);
        txtDeletarConta = findViewById(R.id.txtDeletarConta);

        // Adiciona o OnClickListener ao TextView Deletar Conta
        txtDeletarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeletarContaPopup();
            }
        });

        // Aplica o sublinhado nos TextViews
        applyUnderlineToTextView(textCPF);
        applyUnderlineToTextView(textDataNascimento);

        // Inicialize o botão de Editar
        btnEditar = findViewById(R.id.btnEditar);

        // Inicializa o botao de salvar, e seta ele como invisivel.
        btnSalvar = findViewById(R.id.btnSalvar);
        btnSalvar.setEnabled(false);

        // Torna os EditTexts opacos e não editáveis inicialmente
        setEditTextsEnabled(false);

        // Adiciona o OnClickListener ao botão Editar
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editNome.isEnabled()) {
                    // Se os EditTexts estiverem habilitados, desabilita
                    setEditTextsEnabled(false);
                    btnSalvar.setEnabled(false);
                    btnEditar.setEnabled(true);
                } else {
                    // Se os EditTexts estiverem desabilitados, habilita
                    setEditTextsEnabled(true);
                    btnSalvar.setEnabled(true);
                    btnEditar.setEnabled(false);
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cria uma Intent para iniciar ModificarFoto
                Intent intent = new Intent(ConfiguracaoPerfil.this, ModificarFoto.class);
                startActivity(intent);
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sessionManager.isLoggedIn()) {
                    String nome = editNome.getText().toString();
                    String email = editEmail.getText().toString();
                    String telefone = editTelefone.getText().toString();

                    Call<ResponseDTO> call = apiService.updateUser(
                            sessionManager.getUserId(),
                            sessionManager.getUserCpf(),
                            nome,
                            email,
                            telefone
                    );

                    call.enqueue(new Callback<ResponseDTO>() {
                        @Override
                        public void onResponse(Call<ResponseDTO> call, Response<ResponseDTO> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ResponseDTO res = response.body();
                                Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                setEditTextsEnabled(false);
                                btnSalvar.setEnabled(false);
                                btnEditar.setEnabled(true);
                            }
                            else {
                                Toast.makeText(context, "Erro ao atualizar usuário!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseDTO> call, Throwable t) {
                            Log.d("Login", "Failure ");
                            Log.e("Error", "Erro na requisição: " + t.getMessage());
                        }
                    });
                }
            }
        });
        // Adiciona o OnClickListener ao TextView Sair
        txtSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSairPopup();
            }
        });

        // Defina a visibilidade inicial dos conteúdos
        dataContent.setVisibility(View.GONE);
        optionsContent.setVisibility(View.GONE);

        // Adicione OnClickListeners aos layouts das caixas de informação
        profileHeader.setOnClickListener(v -> {
            // Lógica para expandir/ocultar o conteúdo do cabeçalho do perfil
            // (Se houver conteúdo para expandir/ocultar)
        });

        dataSection.setOnClickListener(v -> {
            // Lógica para expandir/ocultar o conteúdo dos dados cadastrais
            if (dataContent.getVisibility() == View.VISIBLE) {
                dataContent.setVisibility(View.GONE);
                rotateArrow(arrowData, 90f, 0f);
            } else {
                dataContent.setVisibility(View.VISIBLE);
                rotateArrow(arrowData, 0f, 90f);
            }
        });

        optionsSection.setOnClickListener(v -> {
            // Lógica para expandir/ocultar o conteúdo das opções
            if (optionsContent.getVisibility() == View.VISIBLE) {
                optionsContent.setVisibility(View.GONE);
                rotateArrow(arrowOptions, 90f, 0f);
            } else {
                optionsContent.setVisibility(View.VISIBLE);
                rotateArrow(arrowOptions, 0f, 90f);
            }
        });
    }

    private void rotateArrow(ImageView arrow, float fromDegrees, float toDegrees) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(arrow, "rotation", fromDegrees, toDegrees);
        animator.setDuration(200); // Duração da animação em milissegundos
        animator.setInterpolator(new LinearInterpolator()); // Interpolador linear para velocidade constante
        animator.start();
    }

    // Método para habilitar/desabilitar os EditTexts
    private void setEditTextsEnabled(boolean enabled) {
        float alpha = enabled ? 1.0f : 0.5f; // Define a opacidade com base no estado

        editNome.setEnabled(enabled);
        editNome.setAlpha(alpha);

        editTelefone.setEnabled(enabled);
        editTelefone.setAlpha(alpha);

        editEmail.setEnabled(enabled);
        editEmail.setAlpha(alpha);

        editSenha.setEnabled(enabled);
        editSenha.setAlpha(alpha);

        // Habilitar/desabilitar os botões e mudar a opacidade
        btnEditar.setEnabled(!enabled); // Inverte o estado do botão "Editar"
        btnEditar.setAlpha(!enabled ? 1.0f : 0.5f);

        btnSalvar.setEnabled(enabled); // Botão "Salvar" segue o mesmo estado dos EditTexts
        btnSalvar.setAlpha(alpha);
    }

    private void applyUnderlineToTextView(TextView textView) {
        SpannableString spannableString = new SpannableString(textView.getHint());
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        textView.setHint(spannableString);
    }

    // Método para mostrar o popup de confirmação de saída

    private void showSairPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_sair, null);
        builder.setView(dialogView);

        // Encontra os botões dentro do layout do popup
        Button btnSim = dialogView.findViewById(R.id.btn_sim);
        Button btnNao = dialogView.findViewById(R.id.btn_nao);

        // Cria o AlertDialog
        alertDialog = builder.create();

        // Escurece o fundo do popup
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));

        // Define a ação do botão Sim
        btnSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss(); // Fecha o popup
                sessionManager.logoutUser();
                Intent intent = new Intent(ConfiguracaoPerfil.this, TelaLogin.class);
                startActivity(intent);
                finish();
            }
        });

        // Define a ação do botão Não
        btnNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss(); // Fecha o popup
            }
        });

        // Mostra o AlertDialog
        alertDialog.show();
    }
    @SuppressLint("InflateParams")
    private void showDeletarContaPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_deletar_conta, null);
        builder.setView(dialogView);

        // Encontra os botões dentro do layout do popup
        Button btnSim = dialogView.findViewById(R.id.btn_sim);
        Button btnNao = dialogView.findViewById(R.id.btn_nao);

        // Cria o AlertDialog
        alertDialog = builder.create();

        // Escurece o fundo do popup
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));

        // Define a ação do botão Sim
        btnSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss(); // Fecha o popup de confirmação
                // Exibe uma mensagem de conta deletada com sucesso
                AlertDialog.Builder successBuilder = new AlertDialog.Builder(ConfiguracaoPerfil.this);
                successBuilder.setMessage("Conta deletada com sucesso!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finishAffinity(); // Fecha todas as activities
                                System.exit(0); // Fecha o aplicativo
                            }
                        });
                AlertDialog successDialog = successBuilder.create();
                successDialog.show();
            }
        });

        // Define a ação do botão Não
        btnNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss(); // Fecha o popup
            }
        });

        // Mostra o AlertDialog
        alertDialog.show();
    }
}