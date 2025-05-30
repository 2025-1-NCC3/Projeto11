package br.fecap.pi.saferide_passageiro.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.fecap.pi.saferide_passageiro.models.LocalizacaoModel;
import br.fecap.pi.saferide_passageiro.models.TrechoModel;

// RouteDTO.java
public class CalcularRotaResponseDTO {
    @SerializedName("id_rota")
    private int idRota;

    @SerializedName("maps_token")
    private String routeToken;

    @SerializedName("polyline")
    private String polyline;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("duracao")
    private int duracaoSegundos;

    @SerializedName("distancia")
    private int distanciaMetros;

    @SerializedName("local_partida")
    private LocalizacaoModel localPartida;

    @SerializedName("local_destino")
    private LocalizacaoModel localDestino;

    @SerializedName("trechos")
    private List<TrechoModel> trechos;

    public CalcularRotaResponseDTO() {}

    public CalcularRotaResponseDTO(int idRota, String routeToken, String polyline, String descricao, int duracaoSegundos, int distanciaMetros, LocalizacaoModel localPartida, LocalizacaoModel localDestino, List<TrechoModel> trechos) {
        this.idRota = idRota;
        this.routeToken = routeToken;
        this.polyline = polyline;
        this.descricao = descricao;
        this.duracaoSegundos = duracaoSegundos;
        this.distanciaMetros = distanciaMetros;
        this.localPartida = localPartida;
        this.localDestino = localDestino;
        this.trechos = trechos;
    }

    public int getIdRota() {
        return idRota;
    }

    public void setIdRota(int idRota) {
        this.idRota = idRota;
    }

    public String getRouteToken() {
        return routeToken;
    }

    public void setRouteToken(String routeToken) {
        this.routeToken = routeToken;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDuracaoSegundos() {
        return duracaoSegundos;
    }

    public void setDuracaoSegundos(int duracaoSegundos) {
        this.duracaoSegundos = duracaoSegundos;
    }

    public int getDistanciaMetros() {
        return distanciaMetros;
    }

    public void setDistanciaMetros(int distanciaMetros) {
        this.distanciaMetros = distanciaMetros;
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

    public List<TrechoModel> getTrechos() {
        return trechos;
    }

    public void setTrechos(List<TrechoModel> trechos) {
        this.trechos = trechos;
    }
}
