package br.com.fecapccp.uber_saferide;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;

import br.com.fecapccp.uber_saferide.adapter.PlaceAutoSuggestAdapter;

public class IniciarViagem extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btnComecar2;
    private AutoCompleteTextView etPartida;
    private AutoCompleteTextView etDestino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_iniciar_viagem);

        // Inicializar o fragmento do mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Obter a API key do AndroidManifest
        String apiKey = "";
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            apiKey = applicationInfo.metaData.getString("MAPS_API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (!Places.isInitialized()) {
            assert apiKey != null;
            Places.initialize(getApplicationContext(), apiKey);
        }

        PlacesClient placesClient = Places.createClient(this);

        btnComecar2 = findViewById(R.id.btnComecar2);
        etPartida = findViewById(R.id.etPartida);
        etDestino = findViewById(R.id.etDestino);

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        PlaceAutoSuggestAdapter adapter = new PlaceAutoSuggestAdapter(this, placesClient);
        etPartida.setAdapter(adapter);
        etPartida.setThreshold(1);
        etDestino.setAdapter(adapter);
        etDestino.setThreshold(1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etPartida.setOnItemClickListener((parent, view, position, id) -> {
            String origin = (String) parent.getItemAtPosition(position);
            // Aqui você pode converter o nome para coordenadas e mover o mapa
        });

        etDestino.setOnItemClickListener((parent, view, position, id) -> {
            String destination = (String) parent.getItemAtPosition(position);
            // Aqui também
        });

        btnComecar2.setOnClickListener(view -> {
            Intent intent = new Intent(this, ViagemAceitaActivity.class);
            startActivity(intent);
        });

        ImageView imgPerfil = findViewById(R.id.imgPerfil);
        imgPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(IniciarViagem.this, ConfiguracaoPerfil.class);
            startActivity(intent);
        });

        ImageView imgHistorico = findViewById(R.id.imgHistorico);
        imgHistorico.setOnClickListener(v -> {
            Intent intent = new Intent(IniciarViagem.this, TelaAtividades.class);
            startActivity(intent);
        });
    }

    // Mapa carregado
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Verificar permissão de localização
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        // Exibir marcador padrão (São Paulo, por exemplo)
        LatLng saoPaulo = new LatLng(-23.5505, -46.6333);
        mMap.addMarker(new MarkerOptions().position(saoPaulo).title("São Paulo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPaulo, 12));
    }

    // Resultado da permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }
}
