package br.fecap.pi.saferide_motorista.dto;

import com.google.gson.annotations.SerializedName;

import br.fecap.pi.saferide_motorista.models.MotoristaModel;
import br.fecap.pi.saferide_motorista.models.UsuarioModel;

public class ResponseLoginUserDTO {
    @SerializedName("message")
    private String message;
    @SerializedName("usuario")
    private UsuarioModel usuario;
    @SerializedName("motorista")
    private MotoristaModel motorista;
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
    public MotoristaModel getMotorista(){
        return motorista;
    }
}
