package br.fecap.pi.saferide_motorista.dto;

public class CreateUserRequestDTO {
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String data_nascimento;
    private String tipo_usuario;
    private String senha;
    private String cnh;
    private String validade_carteira;
    private String placa;
    private String cor;
    private String modelo;

    public CreateUserRequestDTO(String nome, String email, String telefone, String cpf,
                                String data_nascimento, String tipo_usuario, String senha,
                                String cnh, String validade_carteira, String placa,
                                String cor, String modelo) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.data_nascimento = data_nascimento;
        this.tipo_usuario = tipo_usuario;
        this.senha = senha;
        this.cnh = cnh;
        this.validade_carteira = validade_carteira;
        this.placa = placa;
        this.cor = cor;
        this.modelo = modelo;
    }

    // Getters e setters opcionais
}
