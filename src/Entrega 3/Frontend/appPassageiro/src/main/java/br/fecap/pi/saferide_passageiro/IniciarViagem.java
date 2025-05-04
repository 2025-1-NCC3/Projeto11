package br.fecap.pi.saferide_passageiro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import br.fecap.pi.saferide_passageiro.R;
import br.fecap.pi.saferide_passageiro.adapter.PlaceAutoSuggestAdapter;
import br.fecap.pi.saferide_passageiro.dto.CalcularRotaRequestDTO;
import br.fecap.pi.saferide_passageiro.dto.CalcularRotaResponseDTO;
import br.fecap.pi.saferide_passageiro.models.LocalizacaoModel;
import br.fecap.pi.saferide_passageiro.retrofit.ApiService;
import br.fecap.pi.saferide_passageiro.retrofit.RetrofitClient;
import br.fecap.pi.saferide_passageiro.utils.MapRoutes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IniciarViagem extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btnComecar2;
    private AutoCompleteTextView etPartida;
    private AutoCompleteTextView etDestino;
    ApiService apiService;
    private LocalizacaoModel origemSelecionada;
    private LocalizacaoModel destinoSelecionado;
    private String routePolyline;
    private CalcularRotaResponseDTO routeResponse;
    private  ImageView imgHistorico,imgPerfil;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = RetrofitClient.getApiService();

        setContentView(R.layout.activity_iniciar_viagem);
        //local de findViewById
        btnComecar2 = findViewById(R.id.btnComecar2);
        etPartida = findViewById(R.id.etPartida);
        etDestino = findViewById(R.id.etDestino);
        imgHistorico = findViewById(R.id.imgHistorico);
        imgPerfil = findViewById(R.id.imgPerfil);


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


        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        PlaceAutoSuggestAdapter adapter = new PlaceAutoSuggestAdapter(this, placesClient);
        etPartida.setAdapter(adapter);
        etPartida.setThreshold(2);
        etDestino.setAdapter(adapter);
        etDestino.setThreshold(2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etPartida.setOnItemClickListener((parent, view, position, id) -> {
            String placeId = adapter.getPlaceId(position);

            // Obter detalhes do local usando o Places API
            final FetchPlaceRequest request = FetchPlaceRequest.newInstance(
                    placeId, Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                Place place = response.getPlace();
                LatLng latLng = place.getLatLng();

                if (latLng != null) {
                    // Armazenar dados do local de origem
                    origemSelecionada = new LocalizacaoModel(latLng.latitude, latLng.longitude);

                    // Atualizar o mapa
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(place.getName()
                            ));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                    Log.d("IniciarViagem", "Origem selecionada: " + place.getName() +
                            " (" + latLng.latitude + ", " + latLng.longitude + ")");
                }
            }).addOnFailureListener((exception) -> {
                Log.e("IniciarViagem", "Erro ao buscar detalhes do local: " + exception.getMessage());
            });
        });

        etDestino.setOnItemClickListener((parent, view, position, id) -> {
            String placeId = adapter.getPlaceId(position);

            Log.d("PLACE", "onCreate: " + placeId);

            // Obter detalhes do local usando o Places API
            final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(
                    placeId, Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

            placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
                Place place = response.getPlace();
                LatLng latLng = place.getLatLng();

                if (latLng != null) {
                    // Armazenar dados do local de destino
                    destinoSelecionado = new LocalizacaoModel(latLng.latitude, latLng.longitude);

                    // Adicionar marcador de destino (sem limpar o mapa para manter o marcador de origem)
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(place.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                            ));

                    // Ajustar zoom para mostrar ambos os marcadores
                    if (origemSelecionada.getLatitude() != 0 && origemSelecionada.getLongitude() != 0) {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(new LatLng(origemSelecionada.getLatitude(), origemSelecionada.getLongitude()));
                        builder.include(latLng);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                                builder.build(), 300));
                    } else {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    }

                    Log.d("IniciarViagem", "Destino selecionado: " + place.getName() +
                            " (" + latLng.latitude + ", " + latLng.longitude + ")");

                    calcularRota();
                }
            }).addOnFailureListener((exception) -> {
                Log.e("IniciarViagem", "Erro ao buscar detalhes do local: " + exception.getMessage());
            });

        });

        btnComecar2.setOnClickListener(view -> {
            Intent intent = new Intent(IniciarViagem.this, ViagemAceitaActivity.class);
            startActivity(intent);
        });

        imgPerfil.setOnClickListener(view -> {
            Intent intent = new Intent(IniciarViagem.this, ConfiguracaoPerfil.class);
            startActivity(intent);
        });

        imgHistorico.setOnClickListener(view ->{
            Intent intent = new Intent(this, TelaAtividades.class);
            startActivity(intent);

        });

    }

    private void calcularRota() {
        if (origemSelecionada.getLatitude() == 0 || destinoSelecionado.getLatitude() == 0) {
            Toast.makeText(this, "Por favor, selecione origem e destino", Toast.LENGTH_SHORT).show();
            return;
        }

        CalcularRotaRequestDTO routeRequest = new CalcularRotaRequestDTO(origemSelecionada, destinoSelecionado);
        final CalcularRotaResponseDTO[] routeResponse = new CalcularRotaResponseDTO[1];
        Call<CalcularRotaResponseDTO> call = apiService.calcularRota(routeRequest);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CalcularRotaResponseDTO> call, Response<CalcularRotaResponseDTO> response) {
                if (response.isSuccessful()) {
                    // Processar resposta de sucesso
                    // Você pode armazenar a resposta em um Bundle e passar para a próxima Activity
                    CalcularRotaResponseDTO calcularRotaResponseDTO = response.body();
                    Log.d("RESPONSE API", "onResponse: " + response.body());
                    desenharPolyline(calcularRotaResponseDTO.getPolyline());
                } else {
                    // Tratar erro na resposta
                    Toast.makeText(IniciarViagem.this,
                            "Erro ao calcular rota: " + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CalcularRotaResponseDTO> call, Throwable t) {
                // Tratar falha na comunicação
                Toast.makeText(IniciarViagem.this,
                        "Falha na comunicação: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void desenharPolyline(String encodedPolyline) {
        // Verificar se o mapa e a polyline estão disponíveis
        if (mMap == null || encodedPolyline == null || encodedPolyline.isEmpty()) {
            return;
        }

        // Decodificar a polyline
        List<LatLng> points = MapRoutes.decodePolyline(encodedPolyline);

        if (points.isEmpty()) {
            return;
        }

        // Criar opções para a polyline
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(points)
                .width(10)  // Largura da linha
                .color(Color.BLUE)  // Cor da linha
                .geodesic(true);
        // Seguir a curvatura da Terra

        // Adicionar a polyline ao mapa
        mMap.addPolyline(polylineOptions);

        // Ajustar a câmera para mostrar a rota completa
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng ponto : points) {
            builder.include(ponto);
        }
        LatLngBounds bounds = builder.build();

        // Adicionar padding em torno da rota
        int padding = 100; // em pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        // Mover a câmera suavemente
        mMap.animateCamera(cu);
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
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed(); // Sai do app normalmente
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000); // Reseta em 2 segundos
    }
}