package br.fecap.pi.saferide_motorista.model;

import androidx.annotation.NonNull;

public class AtividadeModel{
    private final String endereco;
    private final String data;
    private final String horario;

    public AtividadeModel(@NonNull String endereco, @NonNull String data, @NonNull String horario) {
        this.endereco = endereco;
        this.data = data;
        this.horario = horario;
    }

    @NonNull
    public String getEndereco() {
        return endereco;
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
