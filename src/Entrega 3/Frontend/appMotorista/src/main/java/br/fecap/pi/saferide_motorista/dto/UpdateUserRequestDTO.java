package br.fecap.pi.saferide_motorista.dto;

public class UpdateUserRequestDTO {
    private int id_usuario;
    private String cpf;
    private String nome;
    private String email;
    private String telefone;

    public UpdateUserRequestDTO(int id_usuario, String cpf, String nome, String email, String telefone) {
        this.id_usuario = id_usuario;
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    // Getters e setters opcionais
}
