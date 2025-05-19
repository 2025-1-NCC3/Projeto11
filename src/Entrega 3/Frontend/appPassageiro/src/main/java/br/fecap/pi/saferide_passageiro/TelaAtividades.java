package br.fecap.pi.saferide_passageiro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.fecap.pi.saferide_passageiro.R;
import br.fecap.pi.saferide_passageiro.adapter.adapter;
import br.fecap.pi.saferide_passageiro.dto.HistoricoCorridaDTO;
import br.fecap.pi.saferide_passageiro.models.AtividadeModel;
import br.fecap.pi.saferide_passageiro.retrofit.ApiService;
import br.fecap.pi.saferide_passageiro.retrofit.RetrofitClient;
import br.fecap.pi.saferide_passageiro.session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaAtividades extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<AtividadeModel> ListaAtividade = new ArrayList<AtividadeModel>();
    private adapter adapter;
    private TextView textRua;
    private TextView textData;
    private TextView textHorario;
    private ImageView imgPerfil, imgHome, imgEmptyState;
    ApiService apiService;
    SessionManager sessionManager;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_atividades);

        // Inicializações
        apiService = RetrofitClient.getApiService();
        sessionManager = new SessionManager(context);

        // Configuração dos componentes de UI
        recyclerView = findViewById(R.id.recyclerView);
        textRua = findViewById(R.id.textRua);
        textData = findViewById(R.id.textData);
        textHorario = findViewById(R.id.textHorario);
        imgHome = findViewById(R.id.imgHome);
        imgPerfil = findViewById(R.id.imgPerfil);
        imgEmptyState = findViewById(R.id.imgEmptyState);

        // Configuração do RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new adapter(ListaAtividade);
        recyclerView.setAdapter(adapter);

        // Configuração do EdgeToEdge
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Listeners de clique
        imgPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(TelaAtividades.this, ConfiguracaoPerfil.class);
            startActivity(intent);
        });

        imgHome.setOnClickListener(v -> {
            finish();
        });

        // Carrega as atividades
        criarAtividades();
    }

    public void criarAtividades() {
        ApiService apiService = RetrofitClient.getApiService();
        int idPassageiro = sessionManager.getUserId();

        Call<List<HistoricoCorridaDTO>> call = apiService.getHistoricoCorridas(idPassageiro);
        call.enqueue(new Callback<List<HistoricoCorridaDTO>>() {
            @Override
            public void onResponse(Call<List<HistoricoCorridaDTO>> call, Response<List<HistoricoCorridaDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<HistoricoCorridaDTO> corridas = response.body();
                    ListaAtividade.clear();

                    // Adiciona todos os itens à lista
                    for (HistoricoCorridaDTO dto : corridas) {
                        String descricao = dto.getDescricao() != null ? dto.getDescricao() : "Sem descrição";
                        String dataFormatada = formatarData(dto.getData_corrida());
                        String horarioFormatado = formatarHorario(dto.getData_hora_inicio());
                        ListaAtividade.add(new AtividadeModel(descricao, dataFormatada, horarioFormatado));
                    }

                    runOnUiThread(() -> {
                        // Atualiza a RecyclerView com todos os itens
                        adapter.notifyDataSetChanged();

                        // Verifica se há corridas
                        if (!ListaAtividade.isEmpty()) {
                            // Mostra RecyclerView e esconde imagem de empty state
                            recyclerView.setVisibility(View.VISIBLE);
                            imgEmptyState.setVisibility(View.GONE);

                            // Exibe o primeiro item nos TextViews
                            AtividadeModel primeiroItem = ListaAtividade.get(0);
                            textRua.setText(primeiroItem.getEndereco());
                            textData.setText(primeiroItem.getData());
                            textHorario.setText(primeiroItem.getHorario());
                        } else {
                            // Caso não haja corridas
                            recyclerView.setVisibility(View.GONE);
                            imgEmptyState.setVisibility(View.VISIBLE);

                            textRua.setText("Nenhuma corrida encontrada");
                            textData.setText("");
                            textHorario.setText("");
                        }
                    });

                } else {
                    Log.e("API_ERROR", "Erro: " + response.code() + " - " + response.message());
                    runOnUiThread(() -> {
                        recyclerView.setVisibility(View.GONE);
                        imgEmptyState.setVisibility(View.VISIBLE);

                        textRua.setText("Erro ao carregar dados");
                        textData.setText("");
                        textHorario.setText("");
                    });
                }
            }

            @Override
            public void onFailure(Call<List<HistoricoCorridaDTO>> call, Throwable t) {
                Log.e("API_FAILURE", "Falha na requisição: " + t.getMessage(), t);
                runOnUiThread(() -> {
                    recyclerView.setVisibility(View.GONE);
                    imgEmptyState.setVisibility(View.VISIBLE);

                    textRua.setText("Falha na conexão");
                    textData.setText("");
                    textHorario.setText("");
                });
            }
        });
    }

    private String formatarData(String dataISO) {
        if (dataISO == null || dataISO.isEmpty()) {
            return "Data inválida";
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate data = LocalDate.parse(dataISO);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMM.", new Locale("pt", "BR"));
                return data.format(formatter);
            }
        } catch (Exception e) {
            Log.e("FormatarData", "Erro ao formatar data: " + dataISO, e);
        }
        return dataISO;
    }

    private String formatarHorario(String dataHoraISO) {
        if (dataHoraISO == null || dataHoraISO.isEmpty()) {
            return "Horário inválido";
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                OffsetDateTime odt = OffsetDateTime.parse(dataHoraISO);
                LocalTime hora = odt.toLocalTime();
                return hora.format(DateTimeFormatter.ofPattern("HH:mm"));
            }
        } catch (Exception e) {
            Log.e("FormatarHorario", "Erro ao formatar horário: " + dataHoraISO, e);
        }
        return dataHoraISO;
    }
}