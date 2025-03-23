package br.com.fecapccp.uber_saferide;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ModificarFoto extends AppCompatActivity {

    private Button btnVoltar;
    private ImageView imgCamera;
    private ImageView imgGaleria; // Adicionado
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_GALLERY_PERMISSION = 200; // Novo código de requisição
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher; // Adicionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar_foto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialize o botão Voltar
        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Inicialize a ImageView da Câmera
        imgCamera = findViewById(R.id.imgCamera);
        // Inicializa a ImageView da Galeria
        imgGaleria = findViewById(R.id.imgGaleria);

        // Criar o ActivityResultLauncher para capturar a imagem da câmera
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        // Aqui você pode exibir a imagem no ImageView ou fazer o que precisar com ela
                        Toast.makeText(this, "Imagem Capturada!", Toast.LENGTH_SHORT).show();
                    }
                });

        // Criar o ActivityResultLauncher para selecionar a imagem da galeria
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        // Aqui você pode usar a imagem selecionada
                        Toast.makeText(this, "Imagem da Galeria Selecionada!", Toast.LENGTH_SHORT).show();
                    }
                });

        // Adiciona o OnClickListener para a ImageView da Câmera
        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CameraClick", "imgCamera foi clicada!");
                if (ContextCompat.checkSelfPermission(ModificarFoto.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("CameraPermission", "Permissão NÃO concedida - Solicitando...");
                    ActivityCompat.requestPermissions(ModificarFoto.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    Log.d("CameraPermission", "Permissão Solicitada");
                } else {
                    Log.d("CameraPermission", "Permissão JÁ concedida");
                    abrirCamera();
                }
            }
        });

        // Adiciona o OnClickListener para a ImageView da Galeria
        imgGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GalleryClick", "imgGaleria foi clicada!");
                if (ContextCompat.checkSelfPermission(ModificarFoto.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("GalleryPermission", "Permissão de Galeria NÃO concedida - Solicitando...");
                    ActivityCompat.requestPermissions(ModificarFoto.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY_PERMISSION);
                    Log.d("GalleryPermission", "Permissão de Galeria Solicitada");
                } else {
                    Log.d("GalleryPermission", "Permissão de Galeria JÁ concedida");
                    abrirGaleria();
                }
            }
        });
    }

    // Método para abrir a câmera
    private void abrirCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent);
        }
    }

    // Método para abrir a galeria
    private void abrirGaleria() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(pickPhoto);
    }

    // Método chamado após a solicitação de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("PermissionResult", "onRequestPermissionsResult chamado");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PermissionResult", "Permissão de Câmera concedida!");
                abrirCamera();
            } else {
                Log.d("PermissionResult", "Permissão de Câmera negada");
                Toast.makeText(this, "Permissão de câmera negada", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_GALLERY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("PermissionResult", "Permissão de Galeria concedida!");
                abrirGaleria();
            } else {
                Log.d("PermissionResult", "Permissão de Galeria negada");
                Toast.makeText(this, "Permissão de galeria negada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}