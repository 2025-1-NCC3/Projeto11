package br.com.fecapccp.uber_saferide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TelaCadastro extends AppCompatActivity {

    private EditText etNome, etDataNasci, etCPF, etTelefone, etEmail, etSenha;
    private Button btnCadastrar;
    private TextView txtEntrar; // Adicione esta linha

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro); // Layout da TelaCadastroActivity

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
        txtEntrar = findViewById(R.id.txtEntrar); // Adicione esta linha

        // Define o listener para o botão
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica se todos os campos estão preenchidos
                if (camposPreenchidos()) {
                    // Se todos os campos estiverem preenchidos, navega para a próxima tela
                    Intent intent = new Intent(TelaCadastro.this, TelaCadastroVeiculo.class);
                    startActivity(intent);
                } else {
                    // Se algum campo não estiver preenchido, exibe uma mensagem de erro
                    Toast.makeText(TelaCadastro.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Define o listener para o TextView
        txtEntrar.setOnClickListener(new View.OnClickListener() {
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