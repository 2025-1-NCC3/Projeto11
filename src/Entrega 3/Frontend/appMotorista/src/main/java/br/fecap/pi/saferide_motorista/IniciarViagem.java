package br.fecap.pi.saferide_motorista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import br.fecap.pi.saferide_motorista.R;
import br.fecap.pi.saferide_motorista.adapter.adapter_IniciarViagem;
import br.fecap.pi.saferide_motorista.model.IniciarViagemModel;
import br.fecap.pi.saferide_motorista.session.SessionManager;

public class IniciarViagem extends AppCompatActivity {

    private RecyclerView recyclerViewCorridas;
    private adapter_IniciarViagem adapter;
    private List<IniciarViagemModel> listaCorridas;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_iniciar_viagem);

        recyclerViewCorridas = findViewById(R.id.recyclerViewCorridas);
        recyclerViewCorridas.setLayoutManager(new LinearLayoutManager(this));

        // lista de corridas fictícias
        listaCorridas = new ArrayList<>();
        listaCorridas.add(new IniciarViagemModel("Carlos", "5 min"));
        listaCorridas.add(new IniciarViagemModel("Julia", "7 min"));
        listaCorridas.add(new IniciarViagemModel("Rafael", "3 min"));

        adapter = new adapter_IniciarViagem(this, listaCorridas);
        recyclerViewCorridas.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.telaCorridas), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imgPerfil = findViewById(R.id.imgPerfil);
        imgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IniciarViagem.this, ConfiguracaoPerfil.class);
                startActivity(intent);
            }
        });
        sessionManager = new SessionManager(this);

        String urlFoto = sessionManager.getUserFoto();

        if (urlFoto != null && !urlFoto.isEmpty()) {
            Glide.with(this)
                    .load(urlFoto)
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(imgPerfil);
        } else {
            imgPerfil.setImageResource(R.drawable.default_avatar); // fallback
        }

        ImageView imgHistorico = findViewById(R.id.imgHistorico);
        imgHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IniciarViagem.this, TelaAtividades.class);
                startActivity(intent);
            }
        });

    }
}