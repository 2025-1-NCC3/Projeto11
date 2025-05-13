package br.fecap.pi.saferide_motorista.retrofit;

import java.util.List;

import br.fecap.pi.saferide_motorista.dto.CreateUserRequestDTO;
import br.fecap.pi.saferide_motorista.dto.LoginRequestDTO;
import br.fecap.pi.saferide_motorista.dto.ResponseCreateUsuarioDTO;
import br.fecap.pi.saferide_motorista.dto.ResponseDTO;
import br.fecap.pi.saferide_motorista.dto.ResponseLoginUserDTO;
import br.fecap.pi.saferide_motorista.dto.UpdateUserRequestDTO;
import br.fecap.pi.saferide_motorista.dto.UpdateVeiculoRequestDTO;
import br.fecap.pi.saferide_motorista.models.UsuarioModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    /*@POST("usuarios/add")
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

    @POST("usuarios/motorista/login")
    @FormUrlEncoded
    Call<ResponseLoginUserDTO> loginMotorista(
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
    @PUT("usuarios/updateVeiculoMotorista")
    @FormUrlEncoded
    Call<ResponseDTO> updateVeiculoMotorista (
            @Field("id_motorista") int idMotorista,
            @Field("modelo") String modelo,
            @Field("cor") String cor,
            @Field("placa") String placa,
            @Field("cnh") String cnh,
            @Field("validade_carteira") String ValCNH
);*/
    @POST("usuarios/add")
    Call<ResponseCreateUsuarioDTO> createUser(@Body CreateUserRequestDTO request);

    @Multipart
    @POST("usuarios/upload-foto")
    Call<ResponseBody> uploadFoto(
            @Part MultipartBody.Part foto,
            @Part("id") RequestBody id
    );

    @POST("usuarios/motorista/login")
    Call<ResponseLoginUserDTO> loginMotorista(@Body LoginRequestDTO request);

    @PUT("usuarios/update")
    Call<ResponseDTO> updateUser(@Body UpdateUserRequestDTO request);

    @PUT("usuarios/updateVeiculoMotorista")
    Call<ResponseDTO> updateVeiculoMotorista(@Body UpdateVeiculoRequestDTO request);


    @DELETE("usuarios/{id}")
    Call<ResponseDTO> deleteUser(@Path("id") int userId);
}