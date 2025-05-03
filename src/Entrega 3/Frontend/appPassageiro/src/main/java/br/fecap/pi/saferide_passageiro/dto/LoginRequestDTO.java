package br.fecap.pi.saferide_passageiro.dto;

import com.google.gson.annotations.SerializedName;

public class LoginRequestDTO {

    @SerializedName("email")
    private String email;

    @SerializedName("senha")
    private String senha;

    public LoginRequestDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
