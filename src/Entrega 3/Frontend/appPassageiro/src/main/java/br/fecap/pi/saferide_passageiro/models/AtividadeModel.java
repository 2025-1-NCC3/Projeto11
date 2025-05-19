package br.fecap.pi.saferide_passageiro.models;

import androidx.annotation.NonNull;

public class AtividadeModel{
    private final String descricao;
    private final String data;
    private final String horario;

    public AtividadeModel(@NonNull String descricao, @NonNull String data, @NonNull String horario) {
        this.descricao = descricao;
        this.data = data;
        this.horario = horario;
    }

    @NonNull
    public String getEndereco() {
        return descricao;
    }

    @NonNull
    public String getData() {
        return data;
    }

    @NonNull
    public String getHorario() {
        return horario;
    }
}
