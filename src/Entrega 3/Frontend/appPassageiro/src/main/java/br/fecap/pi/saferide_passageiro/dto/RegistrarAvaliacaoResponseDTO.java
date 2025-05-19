package br.fecap.pi.saferide_passageiro.dto;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * DTO para representar o objeto de avaliação de corrida
 */
public class RegistrarAvaliacaoResponseDTO {
    private Instant criadoEm;
    private Instant atualizadoEm;
    private String idAvaliacao;
    private String idUsuario;
    private String idCorrida;
    private String idRota;
    private Integer nota;
    private String idTrecho;
    private String comentario;
    private List<FeedbackItemDTO> feedbacks;

    // Construtor padrão
    public RegistrarAvaliacaoResponseDTO() {}

    // Construtor com todos os campos
    public RegistrarAvaliacaoResponseDTO(Instant criadoEm, Instant atualizadoEm,
                                         String idAvaliacao, String idUsuario,
                                         String idCorrida, String idRota,
                                         Integer nota, String idTrecho,
                                         String comentario,
                                         List<FeedbackItemDTO> feedbacks) {
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.idAvaliacao = idAvaliacao;
        this.idUsuario = idUsuario;
        this.idCorrida = idCorrida;
        this.idRota = idRota;
        this.nota = nota;
        this.idTrecho = idTrecho;
        this.comentario = comentario;
        this.feedbacks = feedbacks;
    }

    // Inner class para representar os feedbacks
    public static class FeedbackItemDTO {
        private Long idFeedback;

        // Construtor padrão
        public FeedbackItemDTO() {}

        // Construtor com idFeedback
        public FeedbackItemDTO(Long idFeedback) {
            this.idFeedback = idFeedback;
        }

        // Getter para idFeedback
        public Long getIdFeedback() {
            return idFeedback;
        }

        // Setter para idFeedback
        public void setIdFeedback(Long idFeedback) {
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

    // Getters
    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    public String getIdAvaliacao() {
        return idAvaliacao;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getIdCorrida() {
        return idCorrida;
    }

    public String getIdRota() {
        return idRota;
    }

    public Integer getNota() {
        return nota;
    }

    public String getIdTrecho() {
        return idTrecho;
    }

    public String getComentario() {
        return comentario;
    }

    public List<FeedbackItemDTO> getFeedbacks() {
        return feedbacks;
    }

    // Setters
    public void setCriadoEm(Instant criadoEm) {
        this.criadoEm = criadoEm;
    }

    public void setAtualizadoEm(Instant atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public void setIdAvaliacao(String idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdCorrida(String idCorrida) {
        this.idCorrida = idCorrida;
    }

    public void setIdRota(String idRota) {
        this.idRota = idRota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public void setIdTrecho(String idTrecho) {
        this.idTrecho = idTrecho;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setFeedbacks(List<FeedbackItemDTO> feedbacks) {
        this.feedbacks = feedbacks;
    }

    // Métodos equals, hashCode e toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrarAvaliacaoResponseDTO that = (RegistrarAvaliacaoResponseDTO) o;
        return Objects.equals(criadoEm, that.criadoEm) &&
                Objects.equals(atualizadoEm, that.atualizadoEm) &&
                Objects.equals(idAvaliacao, that.idAvaliacao) &&
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
        return Objects.hash(criadoEm, atualizadoEm, idAvaliacao, idUsuario,
                idCorrida, idRota, nota, idTrecho, comentario, feedbacks);
    }

    @Override
    public String toString() {
        return "AvaliacaoCorridaDTO{" +
                "criadoEm=" + criadoEm +
                ", atualizadoEm=" + atualizadoEm +
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