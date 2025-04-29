package br.fecap.pi.saferide_motorista.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import br.fecap.pi.saferide_motorista.enums.TipoUsuarioEnum;

public class UsuarioModel implements Serializable {

    @SerializedName("id_usuario")
    private int idUsuario;

    @SerializedName("nome")
    private String nome;

    @SerializedName("email")
    private String email;

    @SerializedName("telefone")
    private String telefone;

    @SerializedName("cpf")
    private String cpf;

    @SerializedName("senha")
    private String senha;

    @SerializedName("data_nascimento")
    private Date dataNascimento;

    @SerializedName("tipo_usuario")
    private TipoUsuarioEnum tipoUsuario;

    // Getters e Setters

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public TipoUsuarioEnum getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuarioEnum tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
