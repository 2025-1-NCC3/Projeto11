package br.fecap.pi.saferide_passageiro.models;


public class RotasModel {
    private String rua;
    private int nota;

    public RotasModel(String rua, int nota) {
        this.rua = rua;
        this.nota = nota;
    }

    public String getRua() {
        return rua;
    }

    public int getNota() {
        return nota;
    }
}
