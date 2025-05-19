package br.fecap.pi.saferide_passageiro;

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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import br.fecap.pi.saferide_passageiro.R;
import br.fecap.pi.saferide_passageiro.retrofit.ApiService;
import br.fecap.pi.saferide_passageiro.retrofit.RetrofitClient;
import br.fecap.pi.saferide_passageiro.session.SessionManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ModificarFoto extends AppCompatActivity {

    private Button btnVoltar;
    private ImageView imgCamera;
    private ImageView imgGaleria;
    private ImageView imageView6; // Adicione esta linha
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_GALLERY_PERMISSION = 200;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private LottieAnimationView animationView;
    private FrameLayout loadingLayout;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_foto);

        loadingLayout = findViewById(R.id.loadingLayout);
        animationView = findViewById(R.id.animationView);
        loadingLayout.bringToFront();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnVoltar = findViewById(R.id.btnVoltar);
        imgCamera = findViewById(R.id.imgCamera);
        imgGaleria = findViewById(R.id.imgGaleria);
        imageView6  = findViewById(R.id.imageView6); // Adicione esta linha
        sessionManager = new SessionManager(this);

        String urlFoto = sessionManager.getUserFoto();

        if (urlFoto != null && !urlFoto.isEmpty()) {
            Glide.with(this)
                    .load(urlFoto)
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .into(imageView6);
        } else {
            imageView6.setImageResource(R.drawable.default_avatar); // fallback
        }

        // Callbacks
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    hideLoading();
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        imageView6.setImageBitmap(imageBitmap);

                        // Salvar imagem em arquivo temporário
                        File imagemFile = salvarBitmapTemporariamente(imageBitmap);
                        if (imagemFile != null) {
                            fazerUploadImagem(imagemFile);
                        }

                        Toast.makeText(this, "Imagem Capturada!", Toast.LENGTH_SHORT).show();
                    }
                });


        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    hideLoading();
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        imageView6.setImageURI(selectedImageUri);

                        File imagemFile = converterUriParaFile(selectedImageUri);
                        if (imagemFile != null) {
                            fazerUploadImagem(imagemFile);
                        }

                        Toast.makeText(this, "Imagem da Galeria Selecionada!", Toast.LENGTH_SHORT).show();
                    }
                });

        // Click Listeners
        btnVoltar.setOnClickListener(v -> finish());

        imgCamera.setOnClickListener(v -> {
            Log.d("CameraClick", "imgCamera foi clicada!");
            if (ContextCompat.checkSelfPermission(ModificarFoto.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.d("CameraPermission", "Permissão NÃO concedida - Solicitando...");
                ActivityCompat.requestPermissions(ModificarFoto.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                Log.d("CameraPermission", "Permissão Solicitada");
            } else {
                Log.d("CameraPermission", "Permissão JÁ concedida");
                abrirCamera();
            }
        });

        imgGaleria.setOnClickListener(v -> {
            Log.d("GalleryClick", "imgGaleria foi clicada!");
            if (ContextCompat.checkSelfPermission(ModificarFoto.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                Log.d("GalleryPermission", "Permissão de Galeria NÃO concedida - Solicitando...");
                ActivityCompat.requestPermissions(ModificarFoto.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_GALLERY_PERMISSION);
                Log.d("GalleryPermission", "Permissão de Galeria Solicitada");
            } else {
                Log.d("GalleryPermission", "Permissão de Galeria JÁ concedida");
                abrirGaleria();
            }
        });
    }

    private void fazerUploadImagem(File file) {
        showLoading();

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        int userId = sessionManager.getUserId();

        Log.d("UploadImagem", "ID do usuário enviado: " + userId);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part foto = MultipartBody.Part.createFormData("foto", file.getName(), requestFile);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));

        ApiService apiService = RetrofitClient.getApiService();
        Call<ResponseBody> call = apiService.uploadFoto(foto, id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    Toast.makeText(ModificarFoto.this, "Foto enviada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ModificarFoto.this, "Falha ao enviar imagem", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoading();
                Toast.makeText(ModificarFoto.this, "Erro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File salvarBitmapTemporariamente(Bitmap bitmap) {
        try {
            File arquivo = new File(getCacheDir(), "imagem_temp.jpg");
            FileOutputStream fos = new FileOutputStream(arquivo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return arquivo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private File converterUriParaFile(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File arquivo = new File(getCacheDir(), "imagem_galeria.jpg");
            FileOutputStream outputStream = new FileOutputStream(arquivo);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            outputStream.close();
            inputStream.close();
            return arquivo;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Métodos
    private void abrirCamera() {
        showLoading();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            cameraLauncher.launch(takePictureIntent);
        }
        hideLoading();
    }

    private void abrirGaleria() {
        showLoading();
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(pickPhoto);
        hideLoading();
    }

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

    private void showLoading() {
        runOnUiThread(() -> {
            loadingLayout.setVisibility(View.VISIBLE);
            animationView.playAnimation();
            loadingLayout.setClickable(true);
        });
    }

    private void hideLoading() {
        runOnUiThread(() -> {
            if (loadingLayout.getVisibility() == View.VISIBLE) {
                loadingLayout.setVisibility(View.GONE);
                animationView.pauseAnimation();
                loadingLayout.setClickable(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }
}