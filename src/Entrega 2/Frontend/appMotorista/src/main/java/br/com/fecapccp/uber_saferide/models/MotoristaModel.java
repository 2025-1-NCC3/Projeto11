package br.com.fecapccp.uber_saferide.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;

public class MotoristaModel implements Serializable {

    @SerializedName("id_motorista")
    private String idMotorista;

    @SerializedName("cnh")
    private String cnh;

    @SerializedName("validade_carteira")
    private Date validadeCarteira;

    @SerializedName("avaliacao_media_motorista")
    private double avaliacaoMediaMotorista;

    @SerializedName("id_usuario")
    private UsuarioModel idUsuario;

    // Novos atributos relacionados ao veículo
    @SerializedName("modelo")
    private String modelo;

    @SerializedName("cor")
    private String cor;

    @SerializedName("placa")
    private String placa;

    // Getters e Setters

    public String getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(String idMotorista) {
        this.idMotorista = idMotorista;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public Date getValidadeCarteira() {
        return validadeCarteira;
    }

    public void setValidadeCarteira(Date validadeCarteira) {
        this.validadeCarteira = validadeCarteira;
    }

    public double getAvaliacaoMediaMotorista() {
        return avaliacaoMediaMotorista;
    }

    public void setAvaliacaoMediaMotorista(double avaliacaoMediaMotorista) {
        this.avaliacaoMediaMotorista = avaliacaoMediaMotorista;
    }

    public UsuarioModel getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(UsuarioModel idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}