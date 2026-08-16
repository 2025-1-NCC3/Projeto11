package br.fecap.pi.saferide_passageiro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import br.fecap.pi.saferide_passageiro.Feedback;

public class FeedbackModel {
    @SerializedName("id_feedback")
    private Integer idFeedback;

    @Expose(serialize = false)
    private String categoria;

    @Expose(serialize = false)
    private String descricao;

    @Expose(serialize = false)
    private int frequencia;

    public FeedbackModel(){}

    public FeedbackModel(Integer idFeedback, String categoria, String descricao, int frequencia) {
        this.idFeedback = idFeedback;
        this.categoria = categoria;
        this.descricao = descricao;
        this.frequencia = frequencia;
    }

    public FeedbackModel(Integer idFeedback, String categoria) {
        this.idFeedback = idFeedback;
        this.categoria = categoria;
    }
    // Getters e Setters
    public Integer getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(Integer idFeedback) {
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
