package br.fecap.pi.saferide_motorista;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.fecap.pi.saferide_motorista.dto.CreateUserRequestDTO;
import br.fecap.pi.saferide_motorista.dto.ResponseCreateUsuarioDTO;
import br.fecap.pi.saferide_motorista.models.MotoristaModel;
import br.fecap.pi.saferide_motorista.models.UsuarioModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import br.fecap.pi.saferide_motorista.retrofit.ApiService;
import br.fecap.pi.saferide_motorista.retrofit.RetrofitClient;

public class TelaCadastroVeiculo extends AppCompatActivity {

    private EditText etModelo, etCor, etPlaca, etCNH, etValCNH;
    private Button btnCadastrarVeiculo;
    private UsuarioModel usuario;
    private ApiService apiService;
    private SimpleDateFormat sdf;  // Agora reutilizável

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro_veiculo);

        apiService = RetrofitClient.getApiService();
        usuario = (UsuarioModel) getIntent().getSerializableExtra("usuario");

        // Inicializa o SimpleDateFormat reutilizável
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        etModelo = findViewById(R.id.etModelo);
        etCor = findViewById(R.id.etCor);
        etPlaca = findViewById(R.id.etPlaca);
        etCNH = findViewById(R.id.etCNH);
        etValCNH = findViewById(R.id.etValCNH);

        etValCNH.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TelaCadastroVeiculo.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String dataFormatada = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                        etValCNH.setText(dataFormatada);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        btnCadastrarVeiculo = findViewById(R.id.btnCadastrarVeiculo);
        btnCadastrarVeiculo.setOnClickListener(v -> {
            if (camposPreenchidos()) {
                String modelo = etModelo.getText().toString();
                String cor = etCor.getText().toString();
                String placa = etPlaca.getText().toString();
                String cnh = etCNH.getText().toString();
                String valCNH = etValCNH.getText().toString();

                MotoristaModel motorista = new MotoristaModel();
                motorista.setModelo(modelo);
                motorista.setCor(cor);
                motorista.setPlaca(placa);
                motorista.setCnh(cnh);

                try {
                    Date validadeCarteira = sdf.parse(valCNH);  // Usa o sdf reaproveitado
                    motorista.setValidadeCarteira(validadeCarteira);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(TelaCadastroVeiculo.this, "Data de validade inválida.", Toast.LENGTH_SHORT).show();
                    return;
                }

                motorista.setIdUsuario(usuario.getIdUsuario());

                CreateUserRequestDTO request = new CreateUserRequestDTO(
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTelefone(),
                        usuario.getCpf(),
                        usuario.getDataNascimento(),
                        "Motorista",
                        usuario.getSenha(),
                        cnh,
                        valCNH,
                        placa,
                        cor,
                        modelo
                );

                Call<ResponseCreateUsuarioDTO> call = apiService.createUser(request);

                call.enqueue(new Callback<ResponseCreateUsuarioDTO>() {
                    @Override
                    public void onResponse(Call<ResponseCreateUsuarioDTO> call, Response<ResponseCreateUsuarioDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ResponseCreateUsuarioDTO res = response.body();
                            Toast.makeText(TelaCadastroVeiculo.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TelaCadastroVeiculo.this, TelaLogin.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(TelaCadastroVeiculo.this, "Erro ao cadastrar motorista.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseCreateUsuarioDTO> call, Throwable t) {
                        Log.e("Cadastro", "Falha na requisição: " + t.getMessage());
                        Toast.makeText(TelaCadastroVeiculo.this, "Erro na conexão.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(TelaCadastroVeiculo.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean camposPreenchidos() {
        return !etModelo.getText().toString().trim().isEmpty() &&
                !etCor.getText().toString().trim().isEmpty() &&
                !etPlaca.getText().toString().trim().isEmpty() &&
                !etCNH.getText().toString().trim().isEmpty() &&
                !etValCNH.getText().toString().trim().isEmpty();
    }
}
