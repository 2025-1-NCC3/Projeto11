package br.fecap.pi.saferide_passageiro.models;

import com.google.gson.annotations.SerializedName;

public class FeedbackModel {
    @SerializedName("id_feedback")
    private String idFeedback;

    @SerializedName("categoria")
    private String categoria;

    @SerializedName("descricao")
    private String descricao;

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
}
