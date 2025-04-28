package br.com.fecapccp.uber_saferide;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

import br.com.fecapccp.uber_saferide.dto.ResponseDTO;
import br.com.fecapccp.uber_saferide.retrofit.ApiService;
import br.com.fecapccp.uber_saferide.retrofit.RetrofitClient;
import br.com.fecapccp.uber_saferide.session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfiguracaoPerfil extends AppCompatActivity {

    private LinearLayout profileHeader, dataSection, optionsSection;
    private LinearLayout dataContent, optionsContent, carDataSection, carDataContent;
    private ImageView arrowData, arrowOptions, profileImage, arrowCarData;
    private ImageButton btnBack;
    private Button btnEditar, btnSalvar, btnEditarVeiculo, btnSalvarVeiculo;
    private EditText editNome, editTelefone, editEmail, editSenha, editTextCor, editTextPlaca, editTextModelo, editTextCNH, editTextValCNH;
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
        carDataSection = findViewById(R.id.car_data_section);

        // Inicialize os layouts dos conteúdos das caixas de informação
        dataContent = findViewById(R.id.data_content);
        optionsContent = findViewById(R.id.options_content);
        carDataContent = findViewById(R.id.car_data_content);
        profileImage = findViewById(R.id.profile_image);

        // Inicialize as ImageViews das setas
        arrowData = findViewById(R.id.arrow_data);
        arrowOptions = findViewById(R.id.arrow_options);
        arrowCarData = findViewById(R.id.arrow_car_data);

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

        editTextCor = findViewById(R.id.editTextCor);
        editTextCor.setText(sessionManager.getMotoristaCor());

        editTextPlaca = findViewById(R.id.editTextPlaca);
        editTextPlaca.setText(sessionManager.getMotoristaPlaca());

        editTextModelo = findViewById(R.id.editTextModelo);
        editTextModelo.setText(sessionManager.getMotoristaModelo());

        editTextCNH = findViewById(R.id.editTextCNH);
        editTextCNH.setText(sessionManager.getMotoristaCnh());

        editTextValCNH = findViewById(R.id.editTextValCNH);
        editTextValCNH.setText(sessionManager.getMotoristaValidadeCnh());

        editTextValCNH.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ConfiguracaoPerfil.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String dataFormatada = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        editTextValCNH.setText(dataFormatada);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

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
        btnEditarVeiculo = findViewById(R.id.btnEditarVeiculo);

        // Inicializa o botao de salvar, e seta ele como invisivel.
        btnSalvar = findViewById(R.id.btnSalvar);
        btnSalvar.setEnabled(false);
        btnSalvarVeiculo = findViewById(R.id.btnSalvarVeiculo);

        // Torna os EditTexts opacos e não editáveis inicialmente
        setEditTextsEnabled(false);
        setEditTextsVeiculoEnabled(false);

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

        // Adiciona o OnClickListener ao botão Editar
        btnEditarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextModelo.isEnabled()) {
                    // Se os EditTexts estiverem habilitados, desabilita
                    setEditTextsVeiculoEnabled(false);
                    btnSalvarVeiculo.setEnabled(false);
                    btnEditarVeiculo.setEnabled(true);
                } else {
                    // Se os EditTexts estiverem desabilitados, habilita
                    setEditTextsVeiculoEnabled(true);
                    btnSalvarVeiculo.setEnabled(true);
                    btnEditarVeiculo.setEnabled(false);
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

        btnSalvarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    String modelo = editTextModelo.getText().toString();
                    String cor = editTextCor.getText().toString();
                    String placa = editTextPlaca.getText().toString();
                    String cnh = editTextCNH.getText().toString();
                    String ValCNH = editTextValCNH.getText().toString();

                    Call<ResponseDTO> call = apiService.updateVeiculoMotorista(
                            sessionManager.getMotoristaId(),
                            modelo,
                            cor,
                            placa,
                            cnh,
                            ValCNH
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
                            } else {
                                Toast.makeText(context, "Erro ao atualizar dados do veículo!", Toast.LENGTH_SHORT).show();
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
        carDataContent.setVisibility(View.GONE);
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

        carDataSection.setOnClickListener(v -> {
            // Lógica para expandir/ocultar o conteúdo das opções
            if (carDataContent.getVisibility() == View.VISIBLE) {
                carDataContent.setVisibility(View.GONE);
                rotateArrow(arrowCarData, 90f, 0f);
            } else {
                carDataContent.setVisibility(View.VISIBLE);
                rotateArrow(arrowCarData, 0f, 90f);
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

    // Método para habilitar/desabilitar os EditTexts do Veículo
    private void setEditTextsVeiculoEnabled(boolean enabled) {
        float alpha = enabled ? 1.0f : 0.5f; // Define a opacidade com base no estado

        editTextModelo.setEnabled(enabled);
        editTextModelo.setAlpha(alpha);

        editTextCor.setEnabled(enabled);
        editTextCor.setAlpha(alpha);

        editTextPlaca.setEnabled(enabled);
        editTextPlaca.setAlpha(alpha);

        editTextCNH.setEnabled(enabled);
        editTextCNH.setAlpha(alpha);

        editTextValCNH.setEnabled(enabled);
        editTextValCNH.setAlpha(alpha);

        // Habilitar/desabilitar os botões e mudar a opacidade
        btnEditarVeiculo.setEnabled(!enabled); // Inverte o estado do botão "Editar"
        btnEditarVeiculo.setAlpha(!enabled ? 1.0f : 0.5f);

        btnSalvarVeiculo.setEnabled(enabled); // Botão "Salvar" segue o mesmo estado dos EditTexts
        btnSalvarVeiculo.setAlpha(alpha);
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
                finishAffinity(); // Fecha todas as activities
                System.exit(0); // Fecha o aplicativo
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