package br.fecap.pi.saferide_motorista;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.fecap.pi.saferide_motorista.R;
import br.fecap.pi.saferide_motorista.models.UsuarioModel;

public class TelaCadastro extends AppCompatActivity {

    private EditText etNome, etDataNasci, etTelefone, etEmail, etSenha, etCPF;
    private Button btnCadastrar;
    private TextView txtEntrar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        etNome = findViewById(R.id.etNome);
        etDataNasci = findViewById(R.id.etDataNasci);
        etTelefone = findViewById(R.id.etTelefone);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        txtEntrar = findViewById(R.id.txtEntrar);
        etCPF = findViewById(R.id.etCPF);

        // Configura o DatePicker para a data de nascimento
        etDataNasci.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TelaCadastro.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String dataFormatada = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        etDataNasci.setText(dataFormatada);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Botão para cadastrar e enviar para a TelaCadastroVeiculo
        btnCadastrar.setOnClickListener(v -> {

            if (camposPreenchidos()) {
                String nome = etNome.getText().toString();
                String email = etEmail.getText().toString();
                String telefone = etTelefone.getText().toString();
                String dataNascimento = etDataNasci.getText().toString();
                String senha = etSenha.getText().toString();
                String cpf = etCPF.getText().toString();

                UsuarioModel usuario = new UsuarioModel();
                usuario.setNome(nome);
                usuario.setEmail(email);
                usuario.setTelefone(telefone);
                usuario.setSenha(senha);
                usuario.setCpf(cpf);

                try {
                    usuario.setDataNascimento(new SimpleDateFormat("yyyy-MM-dd").parse(dataNascimento));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(TelaCadastro.this, "Data de nascimento inválida.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Envia os dados para a TelaCadastroVeiculo
                Intent intent = new Intent(TelaCadastro.this, TelaCadastroVeiculo.class);
                intent.putExtra("usuario", usuario); // O modelo deve ser Serializable ou Parcelable
                startActivity(intent);
            } else {
                Toast.makeText(TelaCadastro.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            }
        });

        txtEntrar.setOnClickListener(v -> {
            // Redireciona para a tela de login
            Intent intent = new Intent(TelaCadastro.this, TelaLogin.class);
            startActivity(intent);
        });
    }

    // Verifica se todos os campos obrigatórios estão preenchidos
    private boolean camposPreenchidos() {
        return !etNome.getText().toString().trim().isEmpty() &&
                !etDataNasci.getText().toString().trim().isEmpty() &&
                !etTelefone.getText().toString().trim().isEmpty() &&
                !etEmail.getText().toString().trim().isEmpty() &&
                !etSenha.getText().toString().trim().isEmpty();
    }
}
