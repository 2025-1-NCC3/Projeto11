package br.fecap.pi.saferide_passageiro.dto;

import com.google.gson.annotations.SerializedName;

import br.fecap.pi.saferide_passageiro.models.UsuarioModel;

public class ResponseLoginUserDTO {
    @SerializedName("message")
    private String message;

    @SerializedName("usuario")
    private UsuarioModel usuario;

    @SerializedName("token")
    private String token;

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public UsuarioModel getUsuario() {
        return usuario;
    }
}
