package br.fecap.pi.saferide_motorista.dto;

public class UpdateVeiculoRequestDTO {
    private int id_motorista;
    private String modelo;
    private String cor;
    private String placa;
    private String cnh;
    private String validade_carteira;

    public UpdateVeiculoRequestDTO(int id_motorista, String modelo, String cor,
                                   String placa, String cnh, String validade_carteira) {
        this.id_motorista = id_motorista;
        this.modelo = modelo;
        this.cor = cor;
        this.placa = placa;
        this.cnh = cnh;
        this.validade_carteira = validade_carteira;
    }

    // Getters e setters opcionais
}

