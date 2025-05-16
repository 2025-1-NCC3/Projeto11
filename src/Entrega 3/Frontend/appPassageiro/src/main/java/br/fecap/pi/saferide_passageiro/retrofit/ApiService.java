package br.fecap.pi.saferide_passageiro.retrofit;

import java.util.List;
import java.util.Map;

import br.fecap.pi.saferide_passageiro.dto.AvaliacoesRotaResponseDTO;
import br.fecap.pi.saferide_passageiro.dto.CalcularRotaRequestDTO;
import br.fecap.pi.saferide_passageiro.dto.CalcularRotaResponseDTO;
import br.fecap.pi.saferide_passageiro.dto.LoginRequestDTO;
import br.fecap.pi.saferide_passageiro.dto.ResponseCreateUsuarioDTO;
import br.fecap.pi.saferide_passageiro.dto.ResponseDTO;
import br.fecap.pi.saferide_passageiro.dto.ResponseLoginUserDTO;
import br.fecap.pi.saferide_passageiro.dto.UpdateUserRequestDTO;
import br.fecap.pi.saferide_passageiro.models.UsuarioModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @GET("usuarios")
    Call<List<UsuarioModel>> getUsers();

    @GET("usuarios/id/{id}")
    Call<UsuarioModel> getUserById();

    @GET("usuarios/cpf/{cpf}")
    Call<UsuarioModel> getUserByCpf();

    @POST("usuarios/add")
    Call<ResponseCreateUsuarioDTO> createUser(@Body UsuarioModel usuario);

    @POST("usuarios/passageiro/login")
    Call<ResponseLoginUserDTO> loginPassageiro(@Body LoginRequestDTO loginRequest);

    @PUT("usuarios/update")
    Call<ResponseDTO> updateUser(@Body UpdateUserRequestDTO updateUserRequestDTO);

    /*@DELETE("usuarios/{id}")
    Call<ResponseDTO> deleteUser(@Path("id") int userId);*/
    @HTTP(method = "DELETE", path = "usuarios/delete", hasBody = true)
    Call<ResponseDTO> deleteUser(@Body Map<String, Integer> body);

    @POST("rotas/calcular-rota")
    Call<CalcularRotaResponseDTO> calcularRota(@Body CalcularRotaRequestDTO calcularRotaRequestDTO);

    @GET("avaliacoes/{idRota}")
    Call<List<AvaliacoesRotaResponseDTO>> getAvaliacoesRota(@Path("idRota") int idRota);

    @Multipart
    @POST("usuarios/upload-foto")
    Call<ResponseBody> uploadFoto(
            @Part MultipartBody.Part foto,
            @Part("id") RequestBody id
    );

}