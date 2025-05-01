package br.fecap.pi.saferide_passageiro.retrofit;

import java.util.List;

import br.fecap.pi.saferide_passageiro.dto.CalcularRotaRequestDTO;
import br.fecap.pi.saferide_passageiro.dto.CalcularRotaResponseDTO;
import br.fecap.pi.saferide_passageiro.dto.ResponseCreateUsuarioDTO;
import br.fecap.pi.saferide_passageiro.dto.ResponseDTO;
import br.fecap.pi.saferide_passageiro.dto.ResponseLoginUserDTO;
import br.fecap.pi.saferide_passageiro.models.UsuarioModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("usuarios")
    Call<List<UsuarioModel>> getUsers();

    @GET("usuarios/id/{id}")
    Call<UsuarioModel> getUserById();

    @GET("usuarios/cpf/{cpf}")
    Call<UsuarioModel> getUserByCpf();

    /* @POST("usuarios/add")
    @FormUrlEncoded
    Call<ResponseCreateUsuarioDTO> createUser(
            @Field("nome") String nome,
            @Field("email") String email,
            @Field("telefone") String telefone,
            @Field("cpf") String cpf,
            @Field("data_nascimento") String dataNascimento,
            @Field("tipo_usuario") String tipoUsuario,
            @Field("senha") String senha
    ); */

    @POST("usuarios/add")
    Call<ResponseCreateUsuarioDTO> createUser(@Body UsuarioModel usuario);

    @POST("usuarios/passageiro/login")
    @FormUrlEncoded
    Call<ResponseLoginUserDTO> loginPassageiro(
            @Field("email") String email,
            @Field("senha") String senha
    );

    @PUT("usuarios/update")
    @FormUrlEncoded
    Call<ResponseDTO> updateUser (
        @Field("id_usuario") int idUsuario,
        @Field("cpf") String cpf,
        @Field("nome") String nome,
        @Field("email") String email,
        @Field("telefone") String telefone
    );

    @DELETE("usuarios/{id}")
    Call<ResponseDTO> deleteUser(@Path("id") int userId);

    @POST("rotas/calcular-rota")
    Call<CalcularRotaResponseDTO> calcularRota(@Body CalcularRotaRequestDTO calcularRotaRequestDTO);
}