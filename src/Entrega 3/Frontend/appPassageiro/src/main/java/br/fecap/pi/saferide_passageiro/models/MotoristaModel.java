package br.fecap.pi.saferide_passageiro.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class MotoristaModel {
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

    public String getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(String idMotorista) {
        this.idMotorista = idMotorista;
    }
}
