package br.fecap.pi.saferide_motorista.dto;

import com.google.gson.annotations.SerializedName;

public class ResponseCreateUsuarioDTO {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
