package br.fecap.pi.saferide_passageiro.models;

import com.google.gson.annotations.SerializedName;

public class PassageiroModel {

    @SerializedName("id_passageiro")
    private int idPassageiro;

    @SerializedName("avaliacao_media_passageiro")
    private double avaliacaoMediaPassageiro;

    @SerializedName("id_usuario")
    private UsuarioModel idUsuario;

    public int getIdPassageiro() {
        return idPassageiro;
    }

    public void setIdPassageiro(int idPassageiro) {
        this.idPassageiro = idPassageiro;
    }

    public double getAvaliacaoMediaPassageiro() {
        return avaliacaoMediaPassageiro;
    }

    public void setAvaliacaoMediaPassageiro(double avaliacaoMediaPassageiro) {
        this.avaliacaoMediaPassageiro = avaliacaoMediaPassageiro;
    }

    public UsuarioModel getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(UsuarioModel idUsuario) {
        this.idUsuario = idUsuario;
    }
}
