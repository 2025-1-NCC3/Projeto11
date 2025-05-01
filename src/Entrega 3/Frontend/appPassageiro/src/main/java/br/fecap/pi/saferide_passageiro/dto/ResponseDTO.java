package br.fecap.pi.saferide_passageiro.dto;

import com.google.gson.annotations.SerializedName;

public class ResponseDTO {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
