package br.fecap.pi.saferide_motorista.dto;

public class LoginRequestDTO {
    private String email;
    private String senha;

    public LoginRequestDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters e setters opcionais
}
