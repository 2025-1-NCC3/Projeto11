package br.fecap.pi.saferide_passageiro.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import br.fecap.pi.saferide_passageiro.Feedback;
import br.fecap.pi.saferide_passageiro.models.FeedbackModel;

/**
 * DTO para representar o objeto de avaliação de corrida
 */
public class RegistrarAvaliacaoRequestDTO {
    @Expose(serialize = false)
    private Integer idAvaliacao;

    @SerializedName("id_usuario")
    private Integer idUsuario;

    @SerializedName("id_corrida")
    private Integer idCorrida;

    @SerializedName("id_rota")
    @Expose(serialize = true)
    private Integer idRota;

    @SerializedName("nota")
    @Expose(serialize = true)
    private Integer nota;

    @Expose(serialize = false)
    private Integer idTrecho;

    @SerializedName("comentario")
    private String comentario = "...";

    @SerializedName("feedbacks")
    private List<FeedbackModel> feedbacks;

    // Construtor padrão
    public RegistrarAvaliacaoRequestDTO() {}

    // Construtor com todos os campos
    public RegistrarAvaliacaoRequestDTO(Integer idUsuario, Integer idCorrida,
                                        Integer idRota, Integer nota,
                                        List<FeedbackModel> feedbacks) {
        this.idUsuario = idUsuario;
        this.idCorrida = idCorrida;
        this.idRota = idRota;
        this.nota = nota;
        this.feedbacks = feedbacks;
    }

    // Inner class para representar os feedbacks
    public static class FeedbackItemDTO {
        private Integer idFeedback;

        // Construtor padrão
        public FeedbackItemDTO() {}

        // Construtor com idFeedback
        public FeedbackItemDTO(Integer idFeedback) {
            this.idFeedback = idFeedback;
        }

        // Getter para idFeedback
        public Integer getIdFeedback() {
            return idFeedback;
        }

        // Setter para idFeedback
        public void setIdFeedback(Integer idFeedback) {
            this.idFeedback = idFeedback;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FeedbackItemDTO that = (FeedbackItemDTO) o;
            return Objects.equals(idFeedback, that.idFeedback);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idFeedback);
        }

        @Override
        public String toString() {
            return "FeedbackItemDTO{" +
                    "idFeedback=" + idFeedback +
                    '}';
        }
    }

    public Integer getIdAvaliacao() {
        return idAvaliacao;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public Integer getIdCorrida() {
        return idCorrida;
    }

    public Integer getIdRota() {
        return idRota;
    }

    public Integer getNota() {
        return nota;
    }

    public Integer getIdTrecho() {
        return idTrecho;
    }

    public String getComentario() {
        return comentario;
    }

    public List<FeedbackModel> getFeedbacks() {
        return feedbacks;
    }

    public void setIdAvaliacao(Integer idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdCorrida(Integer idCorrida) {
        this.idCorrida = idCorrida;
    }

    public void setIdRota(Integer idRota) {
        this.idRota = idRota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public void setIdTrecho(Integer idTrecho) {
        this.idTrecho = idTrecho;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setFeedbacks(List<FeedbackModel> feedbacks) {
        this.feedbacks = feedbacks;
    }

    // Métodos equals, hashCode e toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrarAvaliacaoRequestDTO that = (RegistrarAvaliacaoRequestDTO) o;
        return Objects.equals(idAvaliacao, that.idAvaliacao) &&
                Objects.equals(idUsuario, that.idUsuario) &&
                Objects.equals(idCorrida, that.idCorrida) &&
                Objects.equals(idRota, that.idRota) &&
                Objects.equals(nota, that.nota) &&
                Objects.equals(idTrecho, that.idTrecho) &&
                Objects.equals(comentario, that.comentario) &&
                Objects.equals(feedbacks, that.feedbacks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAvaliacao, idUsuario,
                idCorrida, idRota, nota, idTrecho, comentario, feedbacks);
    }

    @Override
    public String toString() {
        return "AvaliacaoCorridaDTO{" +
                ", idAvaliacao='" + idAvaliacao + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", idCorrida='" + idCorrida + '\'' +
                ", idRota='" + idRota + '\'' +
                ", nota=" + nota +
                ", idTrecho='" + idTrecho + '\'' +
                ", comentario='" + comentario + '\'' +
                ", feedbacks=" + feedbacks +
                '}';
    }
}