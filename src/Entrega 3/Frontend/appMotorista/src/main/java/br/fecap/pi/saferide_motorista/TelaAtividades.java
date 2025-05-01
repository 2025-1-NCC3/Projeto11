package br.fecap.pi.saferide_motorista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.fecap.pi.saferide_motorista.R;
import br.fecap.pi.saferide_motorista.adapter.adapter;
import br.fecap.pi.saferide_motorista.model.AtividadeModel;

public class TelaAtividades extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<AtividadeModel> ListaAtividade = new ArrayList<AtividadeModel>();
    private adapter adapter;
    private TextView textRua;
    private TextView textData;
    private TextView textHorario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_atividades);

        recyclerView = findViewById(R.id.recyclerView);
        textRua = findViewById(R.id.textRua);
        textData = findViewById(R.id.textData);
        textHorario = findViewById(R.id.textHorario);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imgPerfil = findViewById(R.id.imgPerfil);
        imgPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(TelaAtividades.this, ConfiguracaoPerfil.class);
            startActivity(intent);
        });

        ImageView imgHome = findViewById(R.id.imgHome);
        imgHome.setOnClickListener(v -> {
            Intent intent = new Intent(TelaAtividades.this, IniciarViagem.class);
            startActivity(intent);
        });

        criarAtividades();

        // Exibir o primeiro item da lista nos TextViews
        if (!ListaAtividade.isEmpty()) {
            AtividadeModel primeiroItem = ListaAtividade.get(0);
            textRua.setText(primeiroItem.getEndereco());
            textData.setText(primeiroItem.getData());
            textHorario.setText(primeiroItem.getHorario());
        }

        // Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new adapter(ListaAtividade);
        recyclerView.setAdapter(adapter);
    }


    public void criarAtividades() {
        AtividadeModel atividade;

        atividade = new AtividadeModel("Rua José Augusto França", "03 de Fev.", "10:10 a.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua das Acácias", "11 de Abr.", "14:30 p.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Av. Brasil", "25 de Dez.", "08:00 a.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua Fernando Pessoa", "01 de Jan.", "09:15 a.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua do Comércio", "20 de Mar.", "13:45 p.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua dos Lírios", "15 de Out.", "16:00 p.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua São Pedro", "07 de Set.", "11:20 a.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Av. Paulista", "19 de Jun.", "17:00 p.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua Getúlio Vargas", "12 de Ago.", "18:30 p.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua das Flores", "22 de Mai.", "07:45 a.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua Dom Pedro II", "05 de Nov.", "20:10 p.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua João XXIII", "03 de Fev.", "12:00 p.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua Antônio Prado", "28 de Jul.", "15:25 p.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua do Sol", "30 de Jan.", "06:30 a.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua Silveira Martins", "10 de Dez.", "22:00 p.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua Bela Vista", "14 de Fev.", "08:45 a.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua das Palmeiras", "06 de Abr.", "19:00 p.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua Castro Alves", "18 de Mar.", "10:00 a.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Rua Marechal Deodoro", "02 de Jun.", "21:45 p.m");
        ListaAtividade.add(atividade);

        atividade = new AtividadeModel("Av. Independência", "09 de Set.", "13:00 p.m");
        ListaAtividade.add(atividade);


    }

}
