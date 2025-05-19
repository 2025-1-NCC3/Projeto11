package br.fecap.pi.saferide_passageiro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.fecap.pi.saferide_passageiro.adapter.PlaceAutoSuggestAdapter;
import br.fecap.pi.saferide_passageiro.adapter.AdapterRotas;
import br.fecap.pi.saferide_passageiro.dto.AvaliacoesRotaResponseDTO;
import br.fecap.pi.saferide_passageiro.dto.CalcularRotaRequestDTO;
import br.fecap.pi.saferide_passageiro.dto.CalcularRotaResponseDTO;
import br.fecap.pi.saferide_passageiro.models.LocalizacaoModel;
import br.fecap.pi.saferide_passageiro.models.RotaModel;
import br.fecap.pi.saferide_passageiro.retrofit.ApiService;
import br.fecap.pi.saferide_passageiro.retrofit.RetrofitClient;
import br.fecap.pi.saferide_passageiro.session.SessionManager;
import br.fecap.pi.saferide_passageiro.utils.AvaliacaoUtils;
import br.fecap.pi.saferide_passageiro.utils.MapRoutes;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
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

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = RetrofitClient.getApiService();

        setContentView(R.layout.activity_iniciar_viagem);
        //local de findViewById
//        btnComecar2 = findViewById(R.id.btnComecar2);
        etPartida = findViewById(R.id.etPartida);
        etDestino = findViewById(R.id.etDestino);
        imgHistorico = findViewById(R.id.imgHistorico);
        imgPerfil = findViewById(R.id.imgPerfil);

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
            String textoSelecionado = parent.getItemAtPosition(position).toString();

            // Inflar o layout do popup
            LayoutInflater inflater = LayoutInflater.from(IniciarViagem.this);
            View popupView = inflater.inflate(R.layout.popup_avaliacao_completo, null);

            // Encontrar o campo txtPontoPartida dentro do popup
            TextView txtPontoPartida = popupView.findViewById(R.id.txtPontoPartida);
            txtPontoPartida.setText(textoSelecionado);

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

//                    if (origemSelecionada != null && destinoSelecionado != null) {
//                        mostrarPopupRecycleView(responseCalcularRota);
//                    }
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

        imgPerfil.setOnClickListener(view -> {
            Intent intent = new Intent(IniciarViagem.this, ConfiguracaoPerfil.class);
            startActivity(intent);
        });

        imgHistorico.setOnClickListener(view ->{
            Intent intent = new Intent(this, TelaAtividades.class);
            startActivity(intent);

        });

    }

    private void mostrarPopupRecycleView(ArrayList<RotaModel> rotas) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TranslucentDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.popup_recycleview, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Controlar o tamanho do popup
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }

        // Pegar o RecyclerView do layout
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obter referência para o EditText etPartida da Activity principal e torná-lo efetivamente final
        EditText etPartida = findViewById(R.id.etPartida);
        final String textoPartida = (etPartida != null) ? etPartida.getText().toString() : "";

        // Obter referência para o EditText etDestino da Activity principal e torná-lo efetivamente final
        EditText etDestinoPrincipal = findViewById(R.id.etDestino);
        final String textoDestinoPrincipal = (etDestinoPrincipal != null) ? etDestinoPrincipal.getText().toString() : "";

        // Criar lista de dados (listaRotas inicia com etDestino - lógica de pasted_content.txt)
        List<RotaModel> listaRotas = new ArrayList<>(rotas);
//        listaRotas.add(new RotaModel(textoDestinoPrincipal, 5)); // Usa textoDeEtDestino
//        listaRotas.add(new RotaModel("Av. Brasil", 4));
//        listaRotas.add(new RotaModel("Rua da Paz", 3));
//        listaRotas.add(new RotaModel("Japão Liberdade", 4));

        // Criar e setar o adapter
        AdapterRotas adapter = new AdapterRotas(rotas, rota -> {
            // Fechar o primeiro popup
            dialog.dismiss();

            // Abrir o popup completo
            LayoutInflater inflater2 = LayoutInflater.from(this);
            View popupView = inflater2.inflate(R.layout.popup_avaliacao_completo, null);

            // Encontre os campos do popup
            TextView txtDestinoPopup = popupView.findViewById(R.id.txtDestino);
            MaterialRatingBar starBar = popupView.findViewById(R.id.starBar);
            TextView txtPontoPartida = popupView.findViewById(R.id.txtPontoPartida);

            // Passar os dados
            if (txtDestinoPopup != null) {
                txtDestinoPopup.setText(rota.getLocalDestino().getLogradouro());
            }
            if (starBar != null && rota.getAvaliacoes() != null) {
                starBar.setRating(AvaliacaoUtils.calcularMediaAvaliacao(rota.getAvaliacoes()));
            }

            if (txtPontoPartida != null) {
                txtPontoPartida.setText(textoPartida);
            }

            // Ocultar containers
            View containerInicioViagem = findViewById(R.id.container_inicio_viagem_completo);
            View containerToolbar = findViewById(R.id.container_toobar_completo);

            if (containerInicioViagem != null) {
                containerInicioViagem.setVisibility(View.GONE);
            }
            if (containerToolbar != null) {
                containerToolbar.setVisibility(View.GONE);
            }

            // Criar e exibir o PopupWindow
            PopupWindow popupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true);

            // Ao fechar o popup, reexibir os containers
            popupWindow.setOnDismissListener(() -> {
                if (containerInicioViagem != null) {
                    containerInicioViagem.setVisibility(View.VISIBLE);
                }
                if (containerToolbar != null) {
                    containerToolbar.setVisibility(View.VISIBLE);
                }
            });

            // === AQUI ADICIONAMOS O BOTÃO btnSet PARA IR PARA MetodoPagamento ===
            Button btnSet = popupView.findViewById(R.id.btnSet);
            if (btnSet != null) {
                btnSet.setOnClickListener(v -> {
                    popupWindow.dismiss(); // fecha o popup

                    // Inicia a tela MetodoPagamento
                    Intent intent = new Intent(this, MetodoPagamento.class);
                    startActivity(intent);
                });
            }

            popupWindow.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);
        });

        recyclerView.setAdapter(adapter);

        // Limitar altura máxima do RecyclerView a altura de 3 itens
        recyclerView.post(() -> {
            int itemHeightDp = 90;
            int maxItems = 4;

            float scale = recyclerView.getResources().getDisplayMetrics().density;
            int maxHeightPx = (int) (itemHeightDp * maxItems * scale + 0.5f);

            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            if (recyclerView.getHeight() > maxHeightPx) {
                params.height = maxHeightPx;
                recyclerView.setLayoutParams(params);
            }
        });
    }

    private void calcularRota() {
        if (origemSelecionada.getLatitude() == 0 || destinoSelecionado.getLatitude() == 0) {
            Toast.makeText(this, "Por favor, selecione origem e destino", Toast.LENGTH_SHORT).show();
            return;
        }

        CalcularRotaRequestDTO routeRequest = new CalcularRotaRequestDTO(origemSelecionada, destinoSelecionado);
        Call<CalcularRotaResponseDTO> call = apiService.calcularRota(routeRequest);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<CalcularRotaResponseDTO> call, Response<CalcularRotaResponseDTO> response) {
                if (response.isSuccessful()) {
                    // Processar resposta de sucesso
                    // Você pode armazenar a resposta em um Bundle e passar para a próxima Activity
                    Log.d("RESPONSE API", "onResponse: " + response.body());
                    ArrayList<RotaModel> rotas = response.body().getRoutes();

                    rotas.forEach((rota) -> {
                        buscarAvaliacoes(rota);
                        //classificarRota();
                        desenharPolyline(rota.getPolyline(), Color.BLUE);
                    });

                    mostrarPopupRecycleView(rotas);
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

            private void buscarAvaliacoes(RotaModel rota) {
                Call<List<AvaliacoesRotaResponseDTO>> call = apiService.getAvaliacoesRota(rota.getIdRota());

                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<AvaliacoesRotaResponseDTO>> call, Response<List<AvaliacoesRotaResponseDTO>> response) {
                        if (response.isSuccessful()) {
                            assert response.body() != null;
                            rota.setAvaliacoes(response.body());
                            Log.d("ROTAS: ", rota.getDescricao() + response.body());
                        } else {
                            Toast.makeText(IniciarViagem.this,
                                    "Erro ao buscar avaliações da rota: " + response.message(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AvaliacoesRotaResponseDTO>> call, Throwable t) {

                    }
                });
            }
        });
    }

//    private void calcularRota() {
//        if (origemSelecionada.getLatitude() == 0 || destinoSelecionado.getLatitude() == 0) {
//            Toast.makeText(this, "Por favor, selecione origem e destino", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        CalcularRotaRequestDTO routeRequest = new CalcularRotaRequestDTO(origemSelecionada, destinoSelecionado);
//        Call<CalcularRotaResponseDTO> call = apiService.calcularRota(routeRequest);
//
//        call.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(Call<CalcularRotaResponseDTO> call, Response<CalcularRotaResponseDTO> response) {
//                if (response.isSuccessful()) {
//                    // Processar resposta de sucesso
//                    Log.d("RESPONSE API", "onResponse: " + response.body());
//                    ArrayList<RotaModel> rotas = response.body().getRoutes();
//
//                    // Solução para remover duplicatas baseada no endereço de destino
//                    ArrayList<RotaModel> rotasSemDuplicatas = new ArrayList<>();
//                    java.util.Set<String> enderecosVistos = new java.util.HashSet<>();
//
//                    for (RotaModel rota : rotas) {
//                        // Obter o endereço completo do destino
//                        String enderecoDestino = rota.getLocalDestino().getLogradouro();
//
//                        // Se o endereço ainda não foi visto, adiciona à lista filtrada
//                        if (!enderecosVistos.contains(enderecoDestino)) {
//                            enderecosVistos.add(enderecoDestino);
//                            rotasSemDuplicatas.add(rota);
//
//                            // Buscar avaliações e desenhar polyline apenas para rotas únicas
//                            buscarAvaliacoes(rota);
//                            desenharPolyline(rota.getPolyline(), Color.BLUE);
//                        }
//                    }
//
//                    // Usar a lista filtrada em vez da original
//                    mostrarPopupRecycleView(rotasSemDuplicatas);
//                } else {
//                    // Tratar erro na resposta
//                    Toast.makeText(IniciarViagem.this,
//                            "Erro ao calcular rota: " + response.message(),
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CalcularRotaResponseDTO> call, Throwable t) {
//                // Tratar falha na comunicação
//                Toast.makeText(IniciarViagem.this,
//                        "Falha na comunicação: " + t.getMessage(),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            private void buscarAvaliacoes(RotaModel rota) {
//                Call<List<AvaliacoesRotaResponseDTO>> call = apiService.getAvaliacoesRota(rota.getIdRota());
//
//                call.enqueue(new Callback<>() {
//                    @Override
//                    public void onResponse(Call<List<AvaliacoesRotaResponseDTO>> call, Response<List<AvaliacoesRotaResponseDTO>> response) {
//                        if (response.isSuccessful()) {
//                            assert response.body() != null;
//                            rota.setAvaliacoes(response.body());
//                            Log.d("ROTAS: ", rota.getDescricao() + response.body());
//                        } else {
//                            Toast.makeText(IniciarViagem.this,
//                                    "Erro ao buscar avaliações da rota: " + response.message(),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<AvaliacoesRotaResponseDTO>> call, Throwable t) {
//                    }
//                });
//            }
//        });
//    }

    private void desenharPolyline(String encodedPolyline, int color) {
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
                .geodesic(false); // Seguir a curvatura da Terra

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
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        // Mover a câmera suavemente
        mMap.animateCamera(cameraUpdate);
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
            sessionManager.logoutUser(); // Limpa os dados da sessão do usuário
            super.onBackPressed(); // Sai do app normalmente
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000); // Reseta em 2 segundos
    }

}