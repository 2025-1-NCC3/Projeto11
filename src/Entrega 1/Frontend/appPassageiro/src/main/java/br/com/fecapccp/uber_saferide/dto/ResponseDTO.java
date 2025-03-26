package br.com.fecapccp.uber_saferide.dto;

import com.google.gson.annotations.SerializedName;

public class ResponseDTO {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}
