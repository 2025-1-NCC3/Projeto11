package br.fecap.pi.saferide_passageiro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class ViagemAceitaActivity extends AppCompatActivity {

    private BottomSheetBehavior<View> bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_viagem_aceita);

        View rootView = findViewById(R.id.main_content_container);
        View containerInformacao = findViewById(R.id.container_informacaoMotorista);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // BottomSheet setup
        bottomSheetBehavior = BottomSheetBehavior.from(containerInformacao);
        bottomSheetBehavior.setHideable(false);

        int peekHeightDp = 50;
        int peekHeightPx = (int) android.util.TypedValue.applyDimension(
                android.util.TypedValue.COMPLEX_UNIT_DIP, peekHeightDp, getResources().getDisplayMetrics());
        bottomSheetBehavior.setPeekHeight(peekHeightPx);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {}
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapViagemAceita);

        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                googleMap.setOnMapClickListener(latLng -> {
                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
            });
        }

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(ViagemAceitaActivity.this, Feedback.class);
            startActivity(intent);
            finish(); // Finaliza a tela atual
        }, 10000); // 10 segundos
    }
}
