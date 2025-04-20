package br.com.fecapccp.uber_saferide;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.fecapccp.uber_saferide.dto.ResponseCreateUsuarioDTO;
import br.com.fecapccp.uber_saferide.dto.ResponseDTO;
import br.com.fecapccp.uber_saferide.enums.TipoUsuarioEnum;
import br.com.fecapccp.uber_saferide.models.UsuarioModel;
import br.com.fecapccp.uber_saferide.retrofit.ApiService;
import br.com.fecapccp.uber_saferide.retrofit.RetrofitClient;
import br.com.fecapccp.uber_saferide.session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaCadastro extends AppCompatActivity {

    private EditText etNome, etDataNasci, etCPF, etTelefone, etEmail, etSenha;
    private Button btnCadastrar;
    private TextView txtCadastrarSe; // Adicione esta linha
    ApiService apiService;
    SessionManager sessionManager;
    Context context = this;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro); // Layout da TelaCadastroActivity

        apiService = RetrofitClient.getApiService();
        sessionManager = new SessionManager(context);

        // Inicializa os campos de texto
        etNome = findViewById(R.id.etNome);
        etDataNasci = findViewById(R.id.etDataNasci);
        etCPF = findViewById(R.id.etCPF);
        etTelefone = findViewById(R.id.etTelefone);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);

        // Inicializa o botão
        btnCadastrar = findViewById(R.id.btnCadastrar);

        // Inicializa o TextView
        txtCadastrarSe = findViewById(R.id.txtCadastrar_se); // Adicione esta linha

        etDataNasci.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TelaCadastro.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Formata a data para yyyy-MM-dd
                        String dataFormatada = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        etDataNasci.setText(dataFormatada);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Define o listener para o botão
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recupera o CPF do EditText
                String cpf = etCPF.getText().toString().trim();

                // Verifica se o CPF tem exatamente 11 dígitos
                if (cpf.length() != 11) {
                    // Exibe uma mensagem de erro caso o CPF seja inválido
                    Toast.makeText(context, "O CPF deve conter exatamente 11 dígitos.", Toast.LENGTH_SHORT).show();
                    return; // Impede o envio da requisição se o CPF for inválido
                }

                // Verifica se todos os campos estão preenchidos
                if (camposPreenchidos()) {
                    // Se todos os campos estiverem preenchidos, navega para a próxima tela

                    String nome = etNome.getText().toString();
                    String email = etEmail.getText().toString();
                    String telefone = etTelefone.getText().toString();
                    String dataNascimento = etDataNasci.getText().toString();
                    String senha = etSenha.getText().toString();

                    UsuarioModel usuario = new UsuarioModel();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setTelefone(telefone);
                    usuario.setCpf(cpf);

                    try {
                        usuario.setDataNascimento(new SimpleDateFormat("yyyy-MM-dd").parse(dataNascimento));
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Data de nascimento inválida.", Toast.LENGTH_SHORT).show();
                        return; // Para o cadastro se a data estiver errada
                    }

                    usuario.setTipoUsuario(TipoUsuarioEnum.Passageiro);
                    usuario.setSenha(senha);

                    Call<ResponseCreateUsuarioDTO> call = apiService.createUser(usuario);

                    call.enqueue(new Callback<ResponseCreateUsuarioDTO>() {
                        @Override
                        public void onResponse(Call<ResponseCreateUsuarioDTO> call, Response<ResponseCreateUsuarioDTO> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ResponseCreateUsuarioDTO res = response.body();
                                Toast.makeText(context, res.getMessage(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TelaCadastro.this, TelaLogin.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(context, "Erro ao criar usuário!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseCreateUsuarioDTO> call, Throwable t) {
                            Log.d("Login", "Failure ");
                            Log.e("Error", "Erro na requisição: " + t.getMessage());
                        }
                    });

                    // Redireciona para a tela IniciarViagem
                    Intent intent = new Intent(TelaCadastro.this, IniciarViagem.class);
                    startActivity(intent);
                } else {
                    // Se algum campo não estiver preenchido, exibe uma mensagem de erro
                    Toast.makeText(TelaCadastro.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Define o listener para o TextView
        txtCadastrarSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega para a TelaLogin
                Intent intent = new Intent(TelaCadastro.this, TelaLogin.class);
                startActivity(intent);
            }
        });
    }

    // Método para verificar se todos os campos estão preenchidos
    private boolean camposPreenchidos() {
        return !etNome.getText().toString().trim().isEmpty() &&
                !etDataNasci.getText().toString().trim().isEmpty() &&
                !etCPF.getText().toString().trim().isEmpty() &&
                !etTelefone.getText().toString().trim().isEmpty() &&
                !etEmail.getText().toString().trim().isEmpty() &&
                !etSenha.getText().toString().trim().isEmpty();
    }
}