package br.fecap.pi.saferide_passageiro.dto;

import com.google.gson.annotations.SerializedName;

import br.fecap.pi.saferide_passageiro.models.LocalizacaoModel;

public class CalcularRotaRequestDTO {
    @SerializedName("origem")
    private LocalizacaoModel origem;
    @SerializedName("destino")
    private LocalizacaoModel destino;

    public CalcularRotaRequestDTO(LocalizacaoModel origem, LocalizacaoModel destino) {
        this.origem = origem;
        this.destino = destino;
    }

    public LocalizacaoModel getOrigem() {
        return origem;
    }

    public LocalizacaoModel getDestino() {
        return destino;
    }
}

