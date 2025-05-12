package br.fecap.pi.saferide_passageiro;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class TempoViagemActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tempo_viagem);

        // Adicionando o listener para ajustar os Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializando o mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Habilitar controles do mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Recuperando a trajetória (linha/rota) da atividade anterior (IniciarViagem)
        List<LatLng> polyline = getIntent().getParcelableArrayListExtra("rota"); // Supondo que a rota foi passada via Intent

        if (polyline != null && !polyline.isEmpty()) {
            // Adicionando a linha de trajeto no mapa
            mMap.addPolyline(new PolylineOptions().addAll(polyline));

            // Ajustando o mapa para o trajeto (centralizar e dar zoom)
            LatLng startPoint = polyline.get(0);
            LatLng endPoint = polyline.get(polyline.size() - 1);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 12)); // Ajuste de zoom conforme necessário

            Log.d("TempoViagemActivity", "Trajetória exibida no mapa.");
        } else {
            Log.d("TempoViagemActivity", "Nenhuma trajetória recebida.");
        }
    }
}
