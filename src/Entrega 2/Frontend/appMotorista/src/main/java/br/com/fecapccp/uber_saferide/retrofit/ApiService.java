package br.com.fecapccp.uber_saferide.retrofit;

import java.util.List;

import br.com.fecapccp.uber_saferide.dto.ResponseCreateUsuarioDTO;
import br.com.fecapccp.uber_saferide.dto.ResponseDTO;
import br.com.fecapccp.uber_saferide.dto.ResponseLoginUserDTO;
import br.com.fecapccp.uber_saferide.models.MotoristaModel;
import br.com.fecapccp.uber_saferide.models.UsuarioModel;
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

    /*POST("usuarios/add")
    Call<ResponseCreateUsuarioDTO> createMotorista(@Body MotoristaModel motorista);*/
    @GET("usuarios")
    Call<List<UsuarioModel>> getUsers();

    @GET("usuarios/id/{id}")
    Call<UsuarioModel> getUserById();

    @GET("usuarios/cpf/{cpf}")
    Call<UsuarioModel> getUserByCpf();

    @POST("usuarios/add")
    @FormUrlEncoded
    Call<ResponseCreateUsuarioDTO> createUser(
            @Field("nome") String nome,
            @Field("email") String email,
            @Field("telefone") String telefone,
            @Field("cpf") String cpf,
            @Field("data_nascimento") String dataNascimento,
            @Field("tipo_usuario") String tipoUsuario,
            @Field("senha") String senha,
            @Field("cnh") String cnh,
            @Field("validade_carteira") String validade_carteira,
            @Field("placa") String placa,
            @Field("cor") String cor,
            @Field("modelo") String modelo
    );

//    @POST("usuarios/add")
//    Call<ResponseCreateUsuarioDTO> createUser(@Body UsuarioModel usuario);


    @POST("usuarios/login")
    @FormUrlEncoded
    Call<ResponseLoginUserDTO> loginUser(
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
}