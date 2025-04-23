package br.com.fecapccp.uber_saferide.models;

import com.google.gson.annotations.SerializedName;

public class LocalizacaoModel {
    @SerializedName("id_localizacao")
    private int idLocalizacao;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("logradouro")
    private String logradouro;

    @SerializedName("bairro")
    private String bairro;

    @SerializedName("cidade")
    private String cidade;

    @SerializedName("estado")
    private String estado;

    @SerializedName("cep")
    private String cep;

    @SerializedName("pais")
    private String pais;

    public LocalizacaoModel(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocalizacaoModel(int idLocalizacao, String placeId, double latitude, double longitude, String logradouro, String bairro, String cidade, String estado, String cep, String pais) {
        this.idLocalizacao = idLocalizacao;
        this.placeId = placeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.pais = pais;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getIdLocalizacao() {
        return idLocalizacao;
    }

    public void setIdLocalizacao(int idLocalizacao) {
        this.idLocalizacao = idLocalizacao;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}
