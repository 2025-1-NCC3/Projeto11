package br.fecap.pi.saferide_passageiro;

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

import java.util.Calendar;
import java.util.Locale;

import br.fecap.pi.saferide_passageiro.R;
import br.fecap.pi.saferide_passageiro.dto.ResponseCreateUsuarioDTO;
import br.fecap.pi.saferide_passageiro.enums.TipoUsuarioEnum;
import br.fecap.pi.saferide_passageiro.models.UsuarioModel;
import br.fecap.pi.saferide_passageiro.retrofit.ApiService;
import br.fecap.pi.saferide_passageiro.retrofit.RetrofitClient;
import br.fecap.pi.saferide_passageiro.session.SessionManager;
import br.fecap.pi.saferide_passageiro.utils.CaesarCipher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaCadastro extends AppCompatActivity {

    private EditText etNome, etDataNasci, etCPF, etTelefone, etEmail, etSenha;
    private Button btnCadastrar;
    private TextView txtCadastrarSe;
    ApiService apiService;
    SessionManager sessionManager;
    Context context = this;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        apiService = RetrofitClient.getApiService();
        sessionManager = new SessionManager(context);

        etNome = findViewById(R.id.etNome);
        etDataNasci = findViewById(R.id.etDataNasci);
        etCPF = findViewById(R.id.etCPF);
        etTelefone = findViewById(R.id.etTelefone);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        txtCadastrarSe = findViewById(R.id.txtCadastrar_se);

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
                String cpf = etCPF.getText().toString().trim();

                if (cpf.length() != 11) {
                    Toast.makeText(context, "O CPF deve conter exatamente 11 dígitos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (camposPreenchidos()) {
                    int shift = 3; // Define o deslocamento da cifra

                    // 1. Criptografa todos os campos (exceto o enum)
                    String nomeCriptografado = CaesarCipher.encrypt(etNome.getText().toString(), shift);
                    String emailCriptografado = CaesarCipher.encrypt(etEmail.getText().toString(), shift);
                    String telefoneCriptografado = CaesarCipher.encrypt(etTelefone.getText().toString(), shift);
                    String senhaCriptografada = CaesarCipher.encrypt(etSenha.getText().toString(), shift);
                    String cpfCriptografado = CaesarCipher.encrypt(cpf, shift);
                    String dataNascimentoCriptografada = CaesarCipher.encrypt(etDataNasci.getText().toString(), shift);

                    // 2. Mantém o enum original (não criptografado)
                    TipoUsuarioEnum tipoUsuario = TipoUsuarioEnum.Passageiro;

                    // 3. Cria o objeto UsuarioModel
                    UsuarioModel usuario = new UsuarioModel();
                    usuario.setNome(nomeCriptografado);
                    usuario.setEmail(emailCriptografado);
                    usuario.setTelefone(telefoneCriptografado);
                    usuario.setSenha(senhaCriptografada);
                    usuario.setCpf(cpfCriptografado);
                    usuario.setTipoUsuario(tipoUsuario);

                    // Envia a data criptografada como string (o back-end fará o parse após descriptografar)
                    usuario.setDataNascimento(dataNascimentoCriptografada); // Alterado para enviar a string criptografada

                    // 4. Envia os dados para a API (todos os campos sensíveis criptografados)
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

                } else {
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