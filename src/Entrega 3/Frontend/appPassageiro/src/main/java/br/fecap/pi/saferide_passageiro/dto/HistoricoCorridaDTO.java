package br.fecap.pi.saferide_passageiro.dto;

import com.google.gson.annotations.SerializedName;

public class HistoricoCorridaDTO {
    @SerializedName("data_corrida")
    private String data_corrida;

    @SerializedName("data_hora_inicio")
    private String data_hora_inicio;

    @SerializedName("descricao")
    private String descricao;
    public String getData_corrida() {
        return data_corrida;
    }

    public void setData_corrida(String data_corrida) {
        this.data_corrida = data_corrida;
    }

    public String getData_hora_inicio() {
        return data_hora_inicio;
    }

    public void setData_hora_inicio(String data_hora_inicio) {
        this.data_hora_inicio = data_hora_inicio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
