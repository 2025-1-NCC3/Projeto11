package br.com.fecapccp.uber_saferide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TelaCadastroVeiculo extends AppCompatActivity {

    private EditText etModelo, etCor, etPlaca, etCNH;
    private Button btnCadastrarVeiculo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro_veiculo);

        // Inicializa os campos de texto
        etModelo = findViewById(R.id.etModelo);
        etCor = findViewById(R.id.etCor);
        etPlaca = findViewById(R.id.etPlaca);
        etCNH = findViewById(R.id.etCNH);

        // Inicializa o botão
        btnCadastrarVeiculo = findViewById(R.id.btnCadastrarVeiculo);

        // Define o listener para o botão
        btnCadastrarVeiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica se todos os campos estão preenchidos
                if (camposPreenchidos()) {
                    // Se todos os campos estiverem preenchidos, navega para a próxima tela
                    Intent intent = new Intent(TelaCadastroVeiculo.this, IniciarViagem.class);
                    startActivity(intent);
                } else {
                    // Se algum campo não estiver preenchido, exibe uma mensagem de erro
                    Toast.makeText(TelaCadastroVeiculo.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para verificar se todos os campos estão preenchidos
    private boolean camposPreenchidos() {
        return !etModelo.getText().toString().trim().isEmpty() &&
                !etCor.getText().toString().trim().isEmpty() &&
                !etPlaca.getText().toString().trim().isEmpty() &&
                !etCNH.getText().toString().trim().isEmpty();
    }
}