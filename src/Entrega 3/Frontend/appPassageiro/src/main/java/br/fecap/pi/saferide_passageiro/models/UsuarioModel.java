package br.fecap.pi.saferide_passageiro.models;

import com.google.gson.annotations.SerializedName;

import br.fecap.pi.saferide_passageiro.enums.TipoUsuarioEnum;

public class UsuarioModel {
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

    @SerializedName("data_nascimento")
    private String dataNascimento;

    @SerializedName("tipo_usuario")
    private TipoUsuarioEnum tipoUsuario;

    @SerializedName("senha")
    private String senha;

    @SerializedName("cnh")
    private String cnh;

    @SerializedName("ativo")
    private boolean ativo;

    @SerializedName("validade_carteira")
    private String validadeCarteira;

    @SerializedName("foto")
    private String foto;


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

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public TipoUsuarioEnum getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuarioEnum tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public String getValidadeCarteira() {
        return validadeCarteira;
    }

    public void setValidadeCarteira(String validadeCarteira) {
        this.validadeCarteira = validadeCarteira;
    }
}
