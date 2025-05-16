package br.fecap.pi.saferide_passageiro.dto;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

public class AvaliacoesRotaResponseDTO {
    @SerializedName("id_avaliacao")
    private String idAvaliacao;

    @SerializedName("id_usuario")
    private String idUsuario;

    @SerializedName("id_corrida")
    private String idCorrida;

    @SerializedName("id_rota")
    private String idRota;

    @SerializedName("nota")
    private int nota;

    @SerializedName("comentario")
    private String comentario;

    @SerializedName("criado_em")
    private Date criadoEm;

    @SerializedName("atualizado_em")
    private Date atualizadoEm;

    @SerializedName("rota")
    private RotaDTO rota;

    @SerializedName("usuario")
    private UsuarioDTO usuario;

    @SerializedName("feedbacks")
    private List<FeedbackDTO> feedbacks;

    // Getters e Setters
    public String getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(String idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdCorrida() {
        return idCorrida;
    }

    public void setIdCorrida(String idCorrida) {
        this.idCorrida = idCorrida;
    }

    public String getIdRota() {
        return idRota;
    }

    public void setIdRota(String idRota) {
        this.idRota = idRota;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Date criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Date getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(Date atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public RotaDTO getRota() {
        return rota;
    }

    public void setRota(RotaDTO rota) {
        this.rota = rota;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public List<FeedbackDTO> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackDTO> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public static class RotaDTO {
        @SerializedName("id_rota")
        private String idRota;

        @SerializedName("maps_token")
        private String mapsToken;

        @SerializedName("polyline")
        private String polyline;

        @SerializedName("id_local_partida")
        private String idLocalPartida;

        @SerializedName("id_local_destino")
        private String idLocalDestino;

        @SerializedName("descricao")
        private String descricao;

        @SerializedName("duracao")
        private int duracao;

        @SerializedName("distancia")
        private int distancia;

        // Getters e Setters
        public String getIdRota() {
            return idRota;
        }

        public void setIdRota(String idRota) {
            this.idRota = idRota;
        }

        public String getMapsToken() {
            return mapsToken;
        }

        public void setMapsToken(String mapsToken) {
            this.mapsToken = mapsToken;
        }

        public String getPolyline() {
            return polyline;
        }

        public void setPolyline(String polyline) {
            this.polyline = polyline;
        }

        public String getIdLocalPartida() {
            return idLocalPartida;
        }

        public void setIdLocalPartida(String idLocalPartida) {
            this.idLocalPartida = idLocalPartida;
        }

        public String getIdLocalDestino() {
            return idLocalDestino;
        }

        public void setIdLocalDestino(String idLocalDestino) {
            this.idLocalDestino = idLocalDestino;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public int getDuracao() {
            return duracao;
        }

        public void setDuracao(int duracao) {
            this.duracao = duracao;
        }

        public int getDistancia() {
            return distancia;
        }

        public void setDistancia(int distancia) {
            this.distancia = distancia;
        }
    }

    public static class UsuarioDTO {
        @SerializedName("id_usuario")
        private String idUsuario;

        @SerializedName("nome")
        private String nome;

        @SerializedName("email")
        private String email;

        @SerializedName("telefone")
        private String telefone;

        @SerializedName("cpf")
        private String cpf;

        @SerializedName("data_nascimento")
        private String dataNascimento;

        @SerializedName("tipo_usuario")
        private String tipoUsuario;

        @SerializedName("senha")
        private String senha;

        @SerializedName("criado_em")
        private Date criadoEm;

        @SerializedName("atualizado_em")
        private Date atualizadoEm;

        @SerializedName("foto")
        private String foto;

        // Getters e Setters
        public String getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(String idUsuario) {
            this.idUsuario = idUsuario;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }

        public String getCpf() {
            return cpf;
        }

        public void setCpf(String cpf) {
            this.cpf = cpf;
        }

        public String getDataNascimento() {
            return dataNascimento;
        }

        public void setDataNascimento(String dataNascimento) {
            this.dataNascimento = dataNascimento;
        }

        public String getTipoUsuario() {
            return tipoUsuario;
        }

        public void setTipoUsuario(String tipoUsuario) {
            this.tipoUsuario = tipoUsuario;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }

        public Date getCriadoEm() {
            return criadoEm;
        }

        public void setCriadoEm(Date criadoEm) {
            this.criadoEm = criadoEm;
        }

        public Date getAtualizadoEm() {
            return atualizadoEm;
        }

        public void setAtualizadoEm(Date atualizadoEm) {
            this.atualizadoEm = atualizadoEm;
        }

        public String getFoto() {
            return foto;
        }

        public void setFoto(String foto) {
            this.foto = foto;
        }
    }

    public static class FeedbackDTO {
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
}