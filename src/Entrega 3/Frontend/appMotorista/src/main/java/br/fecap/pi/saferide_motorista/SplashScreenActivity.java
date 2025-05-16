package br.fecap.pi.saferide_motorista;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        GifImageView gifView = findViewById(R.id.gifView);

        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.saferide);
            gifDrawable.setLoopCount(1); // Exibe o GIF apenas uma vez
            gifView.setImageDrawable(gifDrawable);

            // Listener para detectar o fim da animação
            gifDrawable.addAnimationListener(loopNumber -> {
                // Quando o loop termina (nesse caso, só vai acontecer 1 vez)
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}