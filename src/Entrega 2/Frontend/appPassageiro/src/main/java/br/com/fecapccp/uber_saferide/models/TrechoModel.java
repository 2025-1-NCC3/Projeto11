package br.com.fecapccp.uber_saferide.models;

import com.google.gson.annotations.SerializedName;

public class TrechoModel {
    @SerializedName("id_trecho")
    private int idTrecho;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("distancia")
    private int distanciaMetros;

    @SerializedName("duracao")
    private int duracaoSegundos;

    @SerializedName("polyline")
    private String polyline;

    @SerializedName("order_number")
    private int orderNumber;

    @SerializedName("local_partida")
    private LocalizacaoModel localPartida;

    @SerializedName("local_destino")
    private LocalizacaoModel localDestino;

    public TrechoModel(int idTrecho, String descricao, int distanciaMetros, int duracaoSegundos, String polyline, int orderNumber, LocalizacaoModel localPartida, LocalizacaoModel localDestino) {
        this.idTrecho = idTrecho;
        this.descricao = descricao;
        this.distanciaMetros = distanciaMetros;
        this.duracaoSegundos = duracaoSegundos;
        this.polyline = polyline;
        this.orderNumber = orderNumber;
        this.localPartida = localPartida;
        this.localDestino = localDestino;
    }

    public int getIdTrecho() {
        return idTrecho;
    }

    public void setIdTrecho(int idTrecho) {
        this.idTrecho = idTrecho;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDistanciaMetros() {
        return distanciaMetros;
    }

    public void setDistanciaMetros(int distanciaMetros) {
        this.distanciaMetros = distanciaMetros;
    }

    public int getDuracaoSegundos() {
        return duracaoSegundos;
    }

    public void setDuracaoSegundos(int duracaoSegundos) {
        this.duracaoSegundos = duracaoSegundos;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalizacaoModel getLocalPartida() {
        return localPartida;
    }

    public void setLocalPartida(LocalizacaoModel localPartida) {
        this.localPartida = localPartida;
    }

    public LocalizacaoModel getLocalDestino() {
        return localDestino;
    }

    public void setLocalDestino(LocalizacaoModel localDestino) {
        this.localDestino = localDestino;
    }
}
