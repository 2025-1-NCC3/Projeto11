package br.fecap.pi.saferide_passageiro.dto;

import com.google.gson.annotations.SerializedName;

public class UpdateUserRequestDTO {
    @SerializedName("id_usuario")
    private int idUsuario;

    @SerializedName("cpf")
    private String cpf;

    @SerializedName("nome")
    private String nome;

    @SerializedName("email")
    private String email;

    @SerializedName("telefone")
    private String telefone;

    // Construtor
    public UpdateUserRequestDTO(int idUsuario, String cpf, String nome, String email, String telefone) {
        this.idUsuario = idUsuario;
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    // Getters
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }
}
