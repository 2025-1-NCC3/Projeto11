package br.fecap.pi.saferide_passageiro.models;

import com.google.gson.annotations.SerializedName;

import br.fecap.pi.saferide_passageiro.Feedback;

public class FeedbackModel {
    @SerializedName("id_feedback")
    private String idFeedback;

    @SerializedName("categoria")
    private String categoria;

    @SerializedName("descricao")
    private String descricao;

    private int frequencia;

    public FeedbackModel(){}

    public FeedbackModel(String idFeedback, String categoria, String descricao, int frequencia) {
        this.idFeedback = idFeedback;
        this.categoria = categoria;
        this.descricao = descricao;
        this.frequencia = frequencia;
    }
    // Getters e Setters
    public String getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(String idFeedback) {
        this.idFeedback = idFeedback;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }
}
