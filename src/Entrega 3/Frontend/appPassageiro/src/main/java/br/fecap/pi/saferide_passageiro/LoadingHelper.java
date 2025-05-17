package br.fecap.pi.saferide_passageiro;

import android.view.View;
import com.airbnb.lottie.LottieAnimationView;

public class LoadingHelper {
    private View loadingView;
    private LottieAnimationView animationView;

    public LoadingHelper(View rootView) {
        loadingView = rootView.findViewById(R.id.loadingLayout);
        animationView = rootView.findViewById(R.id.animationView);
    }

    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        animationView.playAnimation();
        // Bloqueia interação com a tela
        loadingView.setClickable(true);
        loadingView.setFocusable(true);
    }

    public void hideLoading() {
        animationView.cancelAnimation();
        loadingView.setVisibility(View.GONE);
        // Restaura interação com a tela
        loadingView.setClickable(false);
        loadingView.setFocusable(false);
    }
}