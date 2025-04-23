package br.com.fecapccp.uber_saferide.model;

import androidx.annotation.NonNull;

public class IniciarViagemModel {
    private String nomePassageiro;
    private String tempoEstimado;
    private String id;

    public IniciarViagemModel(@NonNull String nomePassageiro, @NonNull String tempoEstimado) {
        this.nomePassageiro = nomePassageiro;
        this.tempoEstimado = tempoEstimado;
    }

    public String getId() {
        return id;
    }

    @NonNull
    public String getNomePassageiro() {
        return nomePassageiro;
    }

    public void setNomePassageiro(String nomePassageiro) {
        this.nomePassageiro = nomePassageiro;
    }

    @NonNull
    public String getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(String tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }
}

